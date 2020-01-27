package ru.dmisuvorov.currencyconverter.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.dmisuvorov.currencyconverter.data.local.CurrencyDao
import ru.dmisuvorov.currencyconverter.data.local.CurrencyDatabase
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): CurrencyDatabase = CurrencyDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideCurrencyDao(currencyDatabase: CurrencyDatabase): CurrencyDao =
        currencyDatabase.currencyDao()
}