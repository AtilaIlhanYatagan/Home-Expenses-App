package com.atila.home.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime
import java.util.*

//to be able to serialize the firebase object, fields have to have a first value!!

@Entity
data class User(
    @PrimaryKey
    val userId: String = "null",

    @ColumnInfo(name = "userName")
    val userName: String = "null",

    @ColumnInfo(name = "joinDate")
    val joinDate: OffsetDateTime? = null,
)