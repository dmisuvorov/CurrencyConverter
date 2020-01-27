package ru.dmisuvorov.currencyconverter.data.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.dmisuvorov.currencyconverter.data.remote.response.ExchangeRateResponse

interface CurrencyApi {

    @GET("latest")
    fun getLatestRatesByCurrency(@Query("base") base: String): Observable<ExchangeRateResponse>
}