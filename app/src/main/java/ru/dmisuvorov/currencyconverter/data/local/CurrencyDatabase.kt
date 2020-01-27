package ru.dmisuvorov.currencyconverter.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.dmisuvorov.currencyconverter.data.local.entities.ExchangeRates

@Database(
    entities = [ExchangeRates::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao


    companion object {
        @Volatile
        private var instance: CurrencyDatabase? = null

        fun getInstance(context: Context): CurrencyDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): CurrencyDatabase {
            return Room.databaseBuilder(
                context,
                CurrencyDatabase::class.java,
                "currency-db"
            ).build()
        }
    }
}