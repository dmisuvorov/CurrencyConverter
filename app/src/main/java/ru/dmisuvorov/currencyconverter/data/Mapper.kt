package ru.dmisuvorov.currencyconverter.data

import ru.dmisuvorov.currencyconverter.data.local.entities.ExchangeRates
import ru.dmisuvorov.currencyconverter.data.local.entities.Rates
import ru.dmisuvorov.currencyconverter.data.remote.response.ExchangeRateResponse

fun ExchangeRateResponse.toExchangeRates() : ExchangeRates =
ExchangeRates(
    rates = Rates(rate = rates.rate.toMap()),
    base = base
)