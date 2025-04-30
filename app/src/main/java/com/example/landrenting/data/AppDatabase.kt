package com.example.landrenting.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.landrenting.data.dao.LandDao
import com.example.landrenting.data.dao.UserDao
import com.example.landrenting.data.dao.UserSessionDao
import com.example.landrenting.data.entities.LandEntity
import com.example.landrenting.data.entities.UserEntity
import com.example.landrenting.data.entities.UserSessionEntity

@Database(
    entities = [LandEntity::class, UserEntity::class, UserSessionEntity::class],
    version = 5
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun landDao(): LandDao
    abstract fun userDao(): UserDao
    abstract fun userSessionDao(): UserSessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val converters = Converters()
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "land_database"
                )
                .addTypeConverter(converters)
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}