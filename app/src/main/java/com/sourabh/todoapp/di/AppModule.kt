package com.sourabh.todoapp.di

import android.content.Context
import androidx.room.Room
import com.sourabh.todoapp.data.local.dao.TaskDao
import com.sourabh.todoapp.data.local.database.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(@ApplicationContext appContext: Context): TaskDatabase {
        return Room.databaseBuilder(
            appContext,
            TaskDatabase::class.java,
            "task_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(taskDatabase: TaskDatabase): TaskDao {
        return taskDatabase.taskDao()
    }
}