package com.example.habitude.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitude.data.Habit
import com.example.habitude.utils.Constants.HABIT_COLLECTION
import com.example.habitude.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

    fun saveHabit(habit: Habit) {
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
                            _addHabit.value = Resource.Success(updatedHabit)
                        }.addOnFailureListener {
                            _addHabit.value = Resource.Error(it.message.toString())
                        }
                } else {
                    _addHabit.value = Resource.Error("A habit with the same name already exists.")
                }
            }.addOnFailureListener {
                _addHabit.value = Resource.Error(it.message.toString())
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
                    val habit = document.toObject(Habit::class.java)
                    habit.habitId = document.id
                    habitList.add(habit)
                }
                _habits.value = Resource.Success(ArrayList(habitList))
            }.addOnFailureListener {
                _habits.value = Resource.Error(it.message.toString())
            }
    }

    // Update the habit data in Firebase
    fun updateHabitDay(habit: Habit, dayIndex: Int) {
        firestore.runTransaction { transaction ->
            val documentRef = firestore.collection(HABIT_COLLECTION).document(habit.habitId)
            transaction.set(documentRef, habit)
        }.addOnSuccessListener {
            _updateHabitDay.value = Resource.Success(true)
        }.addOnFailureListener {
            _updateHabitDay.value = Resource.Success(false)
        }
    }
}