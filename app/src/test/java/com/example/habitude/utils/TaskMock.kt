package com.example.habitude.utils

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

object TaskMock {
    fun <T> makeSuccessfulTask(result: T): Task<T> {
        val task = mock(Task::class.java) as Task<T>
        whenever(task.isSuccessful).thenReturn(true)
        whenever(task.result).thenReturn(result)
        return task
    }

    fun <TResult> makeFailedTask(exception: Exception): Task<TResult> {
        return Tasks.forException(exception)
    }
}