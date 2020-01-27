package ru.dmisuvorov.currencyconverter.data.remote.response

data class ExchangeRateResponse(
    val rates: RatesResponse,
    val base: String,
    val date: String
)

data class RatesResponse(
    val rate: Map<String, Double>
)