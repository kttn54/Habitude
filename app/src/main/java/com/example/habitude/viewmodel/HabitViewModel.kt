package com.example.habitude.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitude.data.Habit
import com.example.habitude.utils.Constants.HABIT_COLLECTION
import com.example.habitude.utils.Resource
import com.example.habitude.utils.Serializer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): ViewModel() {

    private val _addHabit = MutableStateFlow<Resource<Habit>>(Resource.Unspecified())
    val addHabit: Flow<Resource<Habit>> = _addHabit

    private val _habits = MutableStateFlow<Resource<ArrayList<Habit>>>(Resource.Unspecified())
    val habits: Flow<Resource<ArrayList<Habit>>> = _habits

    private val _updateHabitDay = MutableStateFlow<Resource<Boolean>>(Resource.Unspecified())
    val updateHabitDay: Flow<Resource<Boolean>> = _updateHabitDay

    private val _updateHabit = MutableStateFlow<Resource<Habit>>(Resource.Unspecified())
    val updateHabit: Flow<Resource<Habit>> = _updateHabit

    private val _deleteHabit = MutableStateFlow<Resource<Boolean>>(Resource.Unspecified())
    val deleteHabit: Flow<Resource<Boolean>> = _deleteHabit

    fun addHabit(habit: Habit) {
        viewModelScope.launch { _addHabit.emit(Resource.Loading()) }

        val userId = firebaseAuth.currentUser?.uid

        val query = firestore.collection(HABIT_COLLECTION)
            .whereEqualTo("userId", userId)
            .whereEqualTo("name", habit.name)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    val habitRef = firestore.collection(HABIT_COLLECTION).document()
                    val updatedHabit = habit.copy(userId = userId.orEmpty(), habitId = habitRef.id)

                    habitRef.set(updatedHabit)
                        .addOnSuccessListener {
                            viewModelScope.launch { _addHabit.emit(Resource.Success(updatedHabit)) }
                        }.addOnFailureListener {
                            viewModelScope.launch { _addHabit.emit(Resource.Error(it.message.toString())) }
                        }
                } else {
                    viewModelScope.launch { _addHabit.emit(Resource.Error("A habit with the same name already exists.")) }
                }
            }.addOnFailureListener {
                viewModelScope.launch { _addHabit.emit(Resource.Error(it.message.toString())) }
            }
    }

    fun getHabits() {
        viewModelScope.launch { _habits.emit(Resource.Loading()) }

        val userId = firebaseAuth.currentUser?.uid
        val query = firestore.collection(HABIT_COLLECTION)
            .whereEqualTo("userId", userId)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                val habitList = mutableListOf<Habit>()
                for (document in querySnapshot) {
                    val habitData = document.data
                    Log.e("test","Type of selectedDates: ${habitData["selectedDates"]?.javaClass}")

                    @Suppress("UNCHECKED_CAST")
                    val selectedDates = habitData["selectedDates"] as ArrayList<Map<String, Any>>

                    // Deserialize the JSON string back to MutableList<CalendarDay>
                    val deserializedList = Serializer.deserializeList(selectedDates)

                    val habit = Habit(
                        name = habitData["name"] as String,
                        habitId = habitData["habitId"] as String,
                        userId = habitData["userId"] as String,
                        selectedDates = deserializedList
                    )

                    //val habit = createHabitFromData(habitData)
                    //val habit = document.toObject(Habit::class.java)

                    habit.habitId = document.id
                    habitList.add(habit)
                }
                viewModelScope.launch { _habits.emit(Resource.Success(ArrayList(habitList))) }
            }.addOnFailureListener {
                viewModelScope.launch { _habits.emit(Resource.Error(it.message.toString())) }
            }
    }

    // Update the habit data in Firebase
    fun updateHabitDay(habit: Habit) {
        viewModelScope.launch { _updateHabitDay.emit(Resource.Loading()) }

        firestore.runTransaction { transaction ->
            val documentRef = firestore.collection(HABIT_COLLECTION).document(habit.habitId)
            transaction.set(documentRef, habit)
        }.addOnSuccessListener {
            viewModelScope.launch { _updateHabitDay.emit(Resource.Success(true)) }
        }.addOnFailureListener {
            viewModelScope.launch { _updateHabitDay.emit(Resource.Error("Update failed.")) }
        }
    }

    fun updateHabit(habit: Habit) {
        viewModelScope.launch { _updateHabit.emit(Resource.Loading()) }

        // Serialize the MutableList<CalendarDay> to List<Map<String, Any>>
        val serializedList = Serializer.serializeList(habit.selectedDates)

        val habitData = hashMapOf(
            "name" to habit.name,
            "habitId" to habit.habitId,
            "userId" to habit.userId,
            "selectedDates" to serializedList
        )

        firestore.runTransaction { transaction ->
            val documentRef = firestore.collection(HABIT_COLLECTION).document(habit.habitId)
            transaction.update(documentRef, habitData as Map<String, Any>)
        }.addOnSuccessListener {
            viewModelScope.launch { _updateHabit.emit(Resource.Success(habit)) }
        }.addOnFailureListener {
            viewModelScope.launch { _updateHabit.emit(Resource.Error(it.message.toString())) }
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch { _deleteHabit.emit(Resource.Loading()) }

        firestore.collection(HABIT_COLLECTION).document(habit.habitId)
            .delete()
            .addOnSuccessListener {
                viewModelScope.launch { _deleteHabit.emit(Resource.Success(true)) }
            }.addOnFailureListener {
                viewModelScope.launch { _deleteHabit.emit(Resource.Error(it.message.toString())) }
            }
    }
}