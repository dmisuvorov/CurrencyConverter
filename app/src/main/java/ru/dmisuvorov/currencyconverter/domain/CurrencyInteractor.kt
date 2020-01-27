package ru.dmisuvorov.currencyconverter.domain

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.dmisuvorov.currencyconverter.data.local.entities.ExchangeRates
import ru.dmisuvorov.currencyconverter.data.toExchangeRates
import ru.dmisuvorov.currencyconverter.repositories.CurrencyRepository
import javax.inject.Inject

class CurrencyInteractor @Inject constructor(private val currencyRepository: CurrencyRepository) :
    CurrencyUseCases {
    override fun getLatestExchangeRates(vararg currencies: String): Observable<ExchangeRates> =
        currencyRepository.getLatestRates(currencies.toList())
            .map { exchangeRateResponse ->
                exchangeRateResponse.toExchangeRates()
            }

    override fun cacheDataToDatabase(list: List<ExchangeRates>): Completable =
        currencyRepository.cacheDataToDatabase(list)

    override fun getCountEntitiesFromDatabase(): Single<Int> =
        currencyRepository.getCountEntitiesFromDatabase()

    override fun getExchangeRateByCurrency(
        fromCurrency: String,
        toCurrency: String
    ): Single<Double> =
        currencyRepository.getExchangeRateByCurrency(fromCurrency)
            .flatMap { exchangeRate ->
                if (exchangeRate.rates.rate.containsKey(toCurrency)) {
                    Single.just(exchangeRate.rates.rate[toCurrency])
                } else {
                    throw IllegalArgumentException("No such currency $toCurrency")
                }
            }

}