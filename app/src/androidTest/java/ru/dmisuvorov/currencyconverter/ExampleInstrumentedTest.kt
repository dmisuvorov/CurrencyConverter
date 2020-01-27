package ru.dmisuvorov.currencyconverter

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import ru.dmisuvorov.currencyconverter.data.remote.response.ExchangeRateResponse
import ru.dmisuvorov.currencyconverter.data.remote.response.RatesResponse
import ru.dmisuvorov.currencyconverter.data.toExchangeRates
import ru.dmisuvorov.currencyconverter.di.modules.AndroidModule
import ru.dmisuvorov.currencyconverter.domain.CurrencyUseCases
import ru.dmisuvorov.currencyconverter.repositories.CurrencyRepository
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Inject
    lateinit var currencyUseCases: CurrencyUseCases

    @Inject
    lateinit var currencyRepository: CurrencyRepository

    private val stubExchangeRatesRUB = ExchangeRateResponse(
        rates = RatesResponse(
            mapOf(
                "CAD" to 0.0210575255,
                "HKD" to 0.1241885633,
                "ISK" to 1.9900295753,
                "PHP" to 0.8122912569,
                "DKK" to 0.108232118,
                "HUF" to 4.8886075876,
                "CZK" to 0.365432578,
                "GBP" to 0.0122179705,
                "RON" to 0.0692426011,
                "SEK" to 0.1532279323,
                "IDR" to 217.2450401337,
                "INR" to 1.1404650934,
                "BRL" to 0.067300367,
                "RUB" to 1.0,
                "HRK" to 0.1077787852,
                "JPY" to 1.7396102786,
                "THB" to 0.490540842,
                "CHF" to 0.0154813873,
                "EUR" to 0.0144834758,
                "MYR" to 0.0649178352,
                "BGN" to 0.028326782,
                "TRY" to 0.0949623574,
                "CNY" to 0.1107652779,
                "NOK" to 0.1453605082,
                "NZD" to 0.0243525162,
                "ZAR" to 0.2323598506,
                "USD" to 0.0159680321,
                "MXN" to 0.3022918652,
                "SGD" to 0.0216745215,
                "AUD" to 0.0235950304,
                "ILS" to 0.0552559085,
                "KRW" to 18.8044759734,
                "PLN" to 0.0618661669
            )
        ),
        base = "RUB",
        date = "2020-01-27"
    )

    private val stubExchangeRatesEUR = ExchangeRateResponse(
        rates = RatesResponse(
            mapOf(
                "CAD" to 1.4539,
                "HKD" to 8.5745,
                "ISK" to 137.4,
                "PHP" to 56.084,
                "DKK" to 7.4728,
                "HUF" to 337.53,
                "CZK" to 25.231,
                "AUD" to 1.6291,
                "RON" to 4.7808,
                "SEK" to 10.5795,
                "IDR" to 14999.51,
                "INR" to 78.7425,
                "BRL" to 4.6467,
                "RUB" to 69.0442,
                "HRK" to 7.4415,
                "JPY" to 120.11,
                "THB" to 33.869,
                "CHF" to 1.0689,
                "SGD" to 1.4965,
                "PLN" to 4.2715,
                "BGN" to 1.9558,
                "TRY" to 6.5566,
                "CNY" to 7.6477,
                "NOK" to 10.0363,
                "NZD" to 1.6814,
                "ZAR" to 16.0431,
                "USD" to 1.1025,
                "MXN" to 20.8715,
                "ILS" to 3.8151,
                "GBP" to 0.84358,
                "KRW" to 1298.34,
                "MYR" to 4.4822
            )
        ),
        base = "EUR",
        date = "2020-01-27"
    )

    @Before
    fun setup() {
        val app =
            InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as App
        DaggerTestComponent.builder()
            .androidModule(AndroidModule(app))
            .build()
            .inject(this)
    }

    @Test
    fun insertExchangeRatesAndDropDb() {
        var isEmpty = true
        val lock0 = Object()
        currencyUseCases.cacheDataToDatabase(listOf(stubExchangeRatesRUB.toExchangeRates()))
            .subscribe {
                currencyUseCases.getCountEntitiesFromDatabase()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { count ->
                        isEmpty = count == 0
                        synchronized(lock0) { lock0.notify() }
                    }
            }
        synchronized(lock0) { lock0.wait() }
        assertEquals(false, isEmpty)

        //дропаем базу
        val lock = Object()
        currencyRepository.dropDb()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                currencyUseCases.getCountEntitiesFromDatabase()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { count ->
                        isEmpty = count == 0
                        synchronized(lock) { lock.notify() }
                    }
            }
        synchronized(lock) { lock.wait() }
        assertEquals(true, isEmpty)

    }

    @Test
    fun insertExchangeRatesAndConvertValue() {
        //дроп базы
        val lock = Object()
        currencyRepository.dropDb()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { synchronized(lock) { lock.notify() } }
        synchronized(lock) { lock.wait() }

        //вставлем стабы
        val lock0 = Object()
        currencyUseCases.cacheDataToDatabase(
            listOf(
                stubExchangeRatesRUB.toExchangeRates(),
                stubExchangeRatesEUR.toExchangeRates()
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { synchronized(lock0) { lock0.notify() } }
        synchronized(lock0) { lock0.wait() }

        var actualResult = ""
        val lock1 = Object()
        currencyUseCases.getExchangeRateByCurrency("RUB", "EUR")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ rate ->
                actualResult = "%4.2f".format((rate * 69.0442))
                synchronized(lock1) { lock1.notify() }
            }
        synchronized(lock1) { lock1.wait() }

        assertEquals("1,00", actualResult)
    }
}
