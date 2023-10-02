package com.comprehensivetutorial.udemyroomdemo.database

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscriber_data")
data class Subscriber(
    @PrimaryKey(true)
    @ColumnInfo(name = "subscriber_id")
    val id: Int,
    @ColumnInfo(name = "subscriber_name")
    val name: String,
    @ColumnInfo(name = "subscriber_email")
    val email: String,


)
//System.currentTimeMillis()
//@ColumnInfo(name = "join_date", defaultValue = "0")
//    val joinDate: Long = 0