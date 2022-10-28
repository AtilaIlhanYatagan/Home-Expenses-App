package com.atila.home.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime
import java.util.*

@Entity
data class Home(
    @PrimaryKey
    val homeId: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "userName")
    val homeName: String,

    @ColumnInfo(name = "totalSpending")
    val totalSpending: Int,

    @ColumnInfo(name = "joinDate")
    val homeCreationDate: OffsetDateTime,

    @ColumnInfo(name = "userList")
    val userList: List<User>

) {
}