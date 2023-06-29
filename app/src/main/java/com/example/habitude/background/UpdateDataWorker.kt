package com.example.habitude.background

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.habitude.data.Habit
import com.example.habitude.utils.Constants.HABIT_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@HiltWorker
class UpdateDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val firestore: FirebaseFirestore
    ) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.e("test", "this worked22")
        return withContext(Dispatchers.IO) {
            try {
                Log.e("test", "this worked")
                val habitsCollection = firestore.collection(HABIT_COLLECTION)
                val habitsQuery = habitsCollection.get().await()
                val habits = habitsQuery.toObjects(Habit::class.java)

                for (habit in habits) {
                    habit.isDaySevenComplete = habit.isDaySixComplete
                    habit.isDaySixComplete = habit.isDayFiveComplete
                    habit.isDayFiveComplete = habit.isDayFourComplete
                    habit.isDayFourComplete = habit.isDayThreeComplete
                    habit.isDayThreeComplete = habit.isDayTwoComplete
                    habit.isDayTwoComplete = habit.isDayOneComplete
                    //Reset the value in the current day
                    habit.isDayOneComplete = false

                    habitsCollection.document(habit.habitId).set(habit).await()
                }
                Result.success()
            } catch (e: Exception) {
                Log.e("test", "worker failed")
                Result.failure()
            }
        }
    }
}