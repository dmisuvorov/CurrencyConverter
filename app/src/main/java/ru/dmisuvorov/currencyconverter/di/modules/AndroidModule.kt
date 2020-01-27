package ru.dmisuvorov.currencyconverter.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.dmisuvorov.currencyconverter.App
import javax.inject.Singleton

@Module
class AndroidModule(private val application: App) {

    @Provides
    @Singleton
    fun provideContext(): Context = application.applicationContext
}