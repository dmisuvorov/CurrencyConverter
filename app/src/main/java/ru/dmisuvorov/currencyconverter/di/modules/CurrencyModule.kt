package ru.dmisuvorov.currencyconverter.di.modules

import dagger.Module
import dagger.Provides
import ru.dmisuvorov.currencyconverter.domain.CurrencyInteractor
import ru.dmisuvorov.currencyconverter.domain.CurrencyUseCases
import ru.dmisuvorov.currencyconverter.presentation.CurrencyViewModel
import ru.dmisuvorov.currencyconverter.presentation.CurrencyViewModelFactory
import ru.dmisuvorov.currencyconverter.repositories.CurrencyRepository
import ru.dmisuvorov.currencyconverter.repositories.CurrencyRepositoryImpl
import javax.inject.Singleton

@Module
class CurrencyModule {

    @Provides
    @Singleton
    fun provideCurrencyViewModelFactory(currencyUseCases: CurrencyUseCases): CurrencyViewModelFactory =
        CurrencyViewModelFactory(currencyUseCases)

    @Provides
    @Singleton
    fun provideCurrencyViewModel(currencyViewModelFactory: CurrencyViewModelFactory): CurrencyViewModel =
        currencyViewModelFactory.create(CurrencyViewModel::class.java)

    @Provides
    @Singleton
    fun provideCurrencyUseCases(currencyInteractor: CurrencyInteractor): CurrencyUseCases =
        currencyInteractor

    @Provides
    @Singleton
    fun provideCurrencyRepository(currencyRepositoryImpl: CurrencyRepositoryImpl): CurrencyRepository =
        currencyRepositoryImpl
}