package ru.dmisuvorov.currencyconverter.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.dmisuvorov.currencyconverter.domain.CurrencyUseCases
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class CurrencyViewModelFactory @Inject constructor(private val currencyUseCases: CurrencyUseCases) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrencyViewModel(currencyUseCases) as T
    }
}