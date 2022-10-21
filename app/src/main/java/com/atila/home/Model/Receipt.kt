package com.atila.home.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Receipt(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "amount")
    val amount: Int,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "type")
    val type: String
) {
}