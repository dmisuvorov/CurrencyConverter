package ru.dmisuvorov.currencyconverter.repositories

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.dmisuvorov.currencyconverter.data.local.entities.ExchangeRates
import ru.dmisuvorov.currencyconverter.data.remote.response.ExchangeRateResponse

interface CurrencyRepository {
    fun getLatestRates(currencies: List<String>): Observable<ExchangeRateResponse>

    fun cacheDataToDatabase(list: List<ExchangeRates>): Completable

    fun getCountEntitiesFromDatabase(): Single<Int>

    fun getExchangeRateByCurrency(currency: String): Single<ExchangeRates>

    fun dropDb(): Completable
}