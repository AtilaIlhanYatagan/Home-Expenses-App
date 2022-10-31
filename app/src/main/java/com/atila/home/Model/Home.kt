package com.atila.home.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime
import java.util.*

@Entity
data class Home(
    @PrimaryKey
    val homeId: String,

    @ColumnInfo(name = "homeName")
    val homeName: String,

    @ColumnInfo(name = "joinDate")
    val homeCreationDate: OffsetDateTime,

    @ColumnInfo(name = "userList")
    val userList: List<User>




) {
}