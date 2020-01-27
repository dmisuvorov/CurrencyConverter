package ru.dmisuvorov.currencyconverter.domain

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.dmisuvorov.currencyconverter.data.local.entities.ExchangeRates

interface CurrencyUseCases {

    fun getLatestExchangeRates(vararg currencies: String): Observable<ExchangeRates>

    fun cacheDataToDatabase(list: List<ExchangeRates>): Completable

    fun getCountEntitiesFromDatabase(): Single<Int>

    fun getExchangeRateByCurrency(fromCurrency: String, toCurrency: String): Single<Double>
}