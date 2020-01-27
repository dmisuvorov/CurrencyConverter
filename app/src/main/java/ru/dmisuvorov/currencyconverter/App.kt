package ru.dmisuvorov.currencyconverter

import android.app.Application
import ru.dmisuvorov.currencyconverter.di.ApplicationComponent
import ru.dmisuvorov.currencyconverter.di.DaggerApplicationComponent
import ru.dmisuvorov.currencyconverter.di.modules.AndroidModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        createComponent()
    }

    private fun createComponent() {
        component = DaggerApplicationComponent
            .builder()
            .androidModule(AndroidModule(this))
            .build()
    }

    companion object {
        lateinit var component: ApplicationComponent
    }
}