package ru.dmisuvorov.currencyconverter

import dagger.Component
import ru.dmisuvorov.currencyconverter.di.modules.*
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidModule::class, ClientModule::class, NetworkModule::class,
    CurrencyModule::class, DatabaseModule::class])
interface TestComponent {
    fun inject(test: ExampleInstrumentedTest)
}