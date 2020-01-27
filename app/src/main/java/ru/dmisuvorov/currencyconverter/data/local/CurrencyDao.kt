package ru.dmisuvorov.currencyconverter.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import ru.dmisuvorov.currencyconverter.data.local.entities.ExchangeRates

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExchangeRates(exchangeRates: List<ExchangeRates>)

    @Query("SELECT * FROM exchangeRates WHERE base=:base")
    fun getExchangeRatesByCurrency(base: String): Single<ExchangeRates>

    @Query("SELECT COUNT(*) FROM exchangeRates")
    fun getCountEntity(): Single<Int>
}