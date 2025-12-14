package com.outracoisa.avaliacao3appn2.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.outracoisa.avaliacao3appn2.data.local.dao.MovieDao
import com.outracoisa.avaliacao3appn2.data.local.dao.UserDao
import com.outracoisa.avaliacao3appn2.data.local.entity.Movie
import com.outracoisa.avaliacao3appn2.data.local.entity.User

@Database(
    entities = [User::class, Movie::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
