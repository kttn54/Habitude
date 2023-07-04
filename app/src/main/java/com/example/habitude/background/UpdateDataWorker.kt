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

/**
 * This class contains the WorkerManager class performs a background task to update habit data in the Firestore database.
 * It updates the completion status of each habit for the previous days, and resets the current day.
 */

@HiltWorker
class UpdateDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    ) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val firestore = FirebaseFirestore.getInstance()
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
                Result.failure()
            }
        }
    }
}