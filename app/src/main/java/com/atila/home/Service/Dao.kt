package com.atila.home.Service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.atila.home.Model.Receipt

@Dao
interface Dao {

    //to insert one receipt
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceiptToDatabase(receipt: Receipt): Long

    // to get all receipts
    @Query("SELECT * FROM Receipt")
    suspend fun getAllReceipts(): List<Receipt>

    //to get single receipt
    @Query("SELECT * FROM Receipt WHERE id = :receiptId")
    suspend fun getReceipt(receiptId: String): Receipt

    // to delete one receipt
    @Query("DELETE FROM Receipt WHERE id = :receiptId")
    suspend fun removeReceiptFromDatabase(receiptId: String): Int

    //to delete all pokemons
    @Query("DELETE FROM Receipt")
    suspend fun deleteAllFavoritePokemons(): Int

    // to retrieve the count for receipts
    @Query("SELECT COUNT(*) FROM Receipt")
    suspend fun getReceiptCount(): Int

    // query to calculate the total spending amount
    @Query("SELECT SUM(Receipt.amount) FROM Receipt")
    suspend fun getTotalSpending(): Int

    /*
        @Query("SELECT COUNT(*) FROM PokemonDetail WHERE name = :name")
        suspend fun isFavorite(name: String): Int
    */
    //To sort the receipt list (https://medium.com/androiddevelopers/room-time-2b4cf9672b98)
    @Query("SELECT * FROM Receipt ORDER BY datetime(Receipt.date)")
    suspend fun getOldUsers(): List<Receipt>

}