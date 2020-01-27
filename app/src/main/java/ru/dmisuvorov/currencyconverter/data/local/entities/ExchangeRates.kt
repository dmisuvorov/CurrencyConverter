package ru.dmisuvorov.currencyconverter.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchangeRates")
data class ExchangeRates(
    @Embedded
    val rates: Rates,

    @PrimaryKey
    val base: String
)

@Entity(tableName = "rates")
data class Rates(
    val rate: Map<String, Double>
)