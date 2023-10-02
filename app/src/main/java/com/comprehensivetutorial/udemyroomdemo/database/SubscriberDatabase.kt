package com.comprehensivetutorial.udemyroomdemo.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec

@Database(
    entities = [Subscriber::class],
    version = 5,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(
            from = 4,
            to = 5,
            spec = SubscriberDatabase.SubscriberAutoMigrationSpec::class
        ),
    ]
)
abstract class SubscriberDatabase: RoomDatabase() {

    abstract val subscriberDao: SubscriberDao

    @DeleteColumn(tableName = "subscriber_data", columnName = "join_date")
    class SubscriberAutoMigrationSpec: AutoMigrationSpec

    companion object {
        @Volatile
        private var INSTANCE: SubscriberDatabase? = null
        fun getDatabase(context: Context): SubscriberDatabase = INSTANCE ?: synchronized(this) {
            var instance = Room.databaseBuilder(
                context,
                SubscriberDatabase::class.java,
                "subscriber_details_db"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}