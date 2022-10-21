package com.atila.home.Service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.atila.home.Model.Receipt

@Database(entities = [Receipt::class], version = 1)
abstract class ReceiptDatabase : RoomDatabase() {

    //Database dao connection
    abstract fun receiptDao(): Dao

    //Singleton to make one and only one thread can access the database
    companion object {
        //Anlatım : btk akademi ileri room database oluşturmak
        @Volatile
        private var instance: ReceiptDatabase? = null
        private var lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, ReceiptDatabase::class.java, "receiptDatabase"
        ).fallbackToDestructiveMigration().build()

    }
}