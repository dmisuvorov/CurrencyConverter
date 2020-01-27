package ru.dmisuvorov.currencyconverter.repositories

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.dmisuvorov.currencyconverter.data.local.CurrencyDao
import ru.dmisuvorov.currencyconverter.data.local.CurrencyDatabase
import ru.dmisuvorov.currencyconverter.data.local.entities.ExchangeRates
import ru.dmisuvorov.currencyconverter.data.remote.CurrencyApi
import ru.dmisuvorov.currencyconverter.data.remote.response.ExchangeRateResponse
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyApi: CurrencyApi,
    private val currencyDao: CurrencyDao,
    private val database: CurrencyDatabase
) : CurrencyRepository {
    override fun getLatestRates(currencies: List<String>): Observable<ExchangeRateResponse> =
        Observable.fromArray(currencies)
            .flatMapIterable { currencyNames -> currencyNames }
            .flatMap { currency -> currencyApi.getLatestRatesByCurrency(currency) }

    override fun cacheDataToDatabase(list: List<ExchangeRates>): Completable =
        Completable.fromAction {
            currencyDao.insertExchangeRates(list)
        }

    override fun getCountEntitiesFromDatabase(): Single<Int> =
        currencyDao.getCountEntity()

    override fun getExchangeRateByCurrency(currency: String): Single<ExchangeRates> =
        currencyDao.getExchangeRatesByCurrency(currency)

    override fun dropDb(): Completable =
        Completable.fromAction {
            database.clearAllTables()
        }


}