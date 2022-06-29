package com.example.basicandroid.data.roomdb.table

import android.annotation.SuppressLint
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Entity(tableName = "location")
data class LocationTable(
    @PrimaryKey(autoGenerate = false) var id: String,
    @ColumnInfo var name: String? = null,
    @ColumnInfo var dimension: String? = null,
    @ColumnInfo var created: String? = null,
) {

    @ColumnInfo var createdTime: Long? = null

    @ColumnInfo var index: Int ? = null

    init {
        kotlin.runCatching {
//            2017-11-10T12:42:04.162Z
            val simpleFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            val d: Date = simpleFormat.apply {
                this.setTimeZone(TimeZone.getTimeZone("UTC"))
            }.parse(created)
            createdTime = d.time
        }.getOrElse {
        }
    }
}
