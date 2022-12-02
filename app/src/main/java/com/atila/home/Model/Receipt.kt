package com.atila.home.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime
import java.util.*

//to be able to serialize the firebase object, fields have to have a first value!!

@Entity
data class Receipt(
    @PrimaryKey
    val id: String = "null",

    @ColumnInfo(name = "amount")
    val amount: Int = 0,

    @ColumnInfo(name = "description")
    val description: String = "null",

    @ColumnInfo(name = "type")
    val type: String = "null",

    @ColumnInfo(name = "date")
    val receiptDate: String = "null",

    @ColumnInfo(name = "addedUser")
    val addedUser: String = "null",
    )
