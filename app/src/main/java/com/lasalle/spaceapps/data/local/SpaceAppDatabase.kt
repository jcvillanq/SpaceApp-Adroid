package com.lasalle.spaceapps.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lasalle.spaceapps.data.model.Converters
import com.lasalle.spaceapps.data.model.Rocket

@Database(
    entities = [Rocket::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SpaceAppsDatabase : RoomDatabase() {
    abstract fun rocketDao(): RocketDao

    companion object {
        @Volatile
        private var INSTANCE: SpaceAppsDatabase? = null

        fun getDatabase(context: Context): SpaceAppsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SpaceAppsDatabase::class.java,
                    "spaceapps_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}