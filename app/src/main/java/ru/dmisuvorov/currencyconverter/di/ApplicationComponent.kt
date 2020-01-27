package ru.dmisuvorov.currencyconverter.di

import dagger.Component
import ru.dmisuvorov.currencyconverter.di.modules.*
import ru.dmisuvorov.currencyconverter.ui.CurrencyActivity
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidModule::class, ClientModule::class, CurrencyModule::class, DatabaseModule::class, NetworkModule::class]
)
interface ApplicationComponent {
    fun inject(currencyActivity: CurrencyActivity)
}