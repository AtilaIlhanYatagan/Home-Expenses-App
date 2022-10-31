package com.atila.home.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime
import java.util.*

@Entity
data class User(
    @PrimaryKey
    val userId: String,

    @ColumnInfo(name = "userName")
    val userName: String,

    @ColumnInfo(name = "homeId")
    val homeId: String,

    @ColumnInfo(name = "totalSpending")
    val totalSpending: Int,

    @ColumnInfo(name = "joinDate")
    val joinDate: OffsetDateTime,
) {
}