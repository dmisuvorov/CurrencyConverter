package ru.dmisuvorov.currencyconverter.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.dmisuvorov.currencyconverter.APP_CONFIG.NEED_CURRENCIES
import ru.dmisuvorov.currencyconverter.R
import ru.dmisuvorov.currencyconverter.data.local.entities.ExchangeRates
import ru.dmisuvorov.currencyconverter.domain.CurrencyUseCases
import javax.inject.Inject

class CurrencyViewModel @Inject constructor(private val currencyUseCases: CurrencyUseCases) :
    ViewModel() {
    val state = MutableLiveData<CurrencyState>()
    val notifications = MutableLiveData<Event>()

    init {
        getLatestExchangeRates()
    }

    @SuppressLint("CheckResult")
    private fun getLatestExchangeRates() {
        state.value = CurrencyState.LOADING
        val listOfExchangeRates = mutableListOf<ExchangeRates>()
        currencyUseCases.getLatestExchangeRates(*NEED_CURRENCIES)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response -> listOfExchangeRates.add(response) },
                { error ->
                    error.printStackTrace()
                    state.value = CurrencyState.ERROR
                    notifications.value =
                        Event(Notify.ErrorMessage(R.string.error_text_on_load_data))
                },
                {
                    cacheDataToDatabase(listOfExchangeRates,
                        {
                            state.value = CurrencyState.DEFAULT
                        },
                        {
                            state.value = CurrencyState.ERROR
                            notifications.value =
                                Event(Notify.ErrorMessage(R.string.error_text_on_insert_data))
                        })
                }
            )
    }

    @SuppressLint("CheckResult")
    private fun cacheDataToDatabase(
        list: List<ExchangeRates>,
        onComplete: () -> Unit,
        onError: () -> Unit
    ) {
        currencyUseCases.cacheDataToDatabase(list)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onComplete() },
                { error ->
                    error.printStackTrace()
                    onError()
                }
            )
    }

    fun convertCurrency(fromCurrency: String, toCurrency: String, value: String) {
        state.value = CurrencyState.LOADING
        checkConditionBeforeConvertCurrency(value) { isCheck ->
            if (isCheck) {
                currencyUseCases.getExchangeRateByCurrency(fromCurrency, toCurrency)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { rate ->
                            state.value = CurrencyState.SUCCESS_CONVERT(
                                "%4.2f".format((rate * value.toDouble()))
                            )
                        },
                        { error ->
                            error.printStackTrace()
                            state.value = CurrencyState.ERROR
                            notifications.value =
                                Event(Notify.ErrorMessage(R.string.error_text_on_convert_currency))
                        }
                    )
            } else {
                state.value = CurrencyState.ERROR
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun checkConditionBeforeConvertCurrency(
        value: String,
        result: (isCheck: Boolean) -> Unit
    ) {
        if (value.isEmpty()) {
            notifications.value =
                Event(Notify.ErrorMessage(R.string.error_text_on_empty_value))
            result(false)
            return
        }
        currencyUseCases.getCountEntitiesFromDatabase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { countSum ->
                    val isEmpty = countSum == 0
                    if (isEmpty) notifications.value =
                        Event(Notify.ErrorMessage(R.string.error_text_on_empty_database))
                    result(isEmpty.not())
                },
                { error ->
                    error.printStackTrace()
                    notifications.value =
                        Event(Notify.ErrorMessage(R.string.error_text_on_check_initial_condition))
                    result(false)
                })
    }


    sealed class CurrencyState {
        object LOADING : CurrencyState()
        object DEFAULT : CurrencyState()
        data class SUCCESS_CONVERT(val convertCurrency: String) : CurrencyState()
        object ERROR : CurrencyState()
    }


    class EventObserver(private val onEventUnhandledContent: (Notify) -> Unit) :
        Observer<Event> {
        override fun onChanged(t: Event?) {
            //если есть необработанное событие передай в качестве аргумента в лямбду
            // onEventUnhandledContent
            t?.getContentIfNotHandled()?.let {
                onEventUnhandledContent(it)
            }

        }
    }

    //Специальный класс обертка для событий, которые обрабатываются один раз (snackbar, toast и различные уведомления)
    class Event(private val content: Notify) {
        private var hasBeenHandled = false

        fun getContentIfNotHandled(): Notify? =
            if (hasBeenHandled) null
            else {
                hasBeenHandled = true
                content
            }

    }

    sealed class Notify(val messageRes: Int) {
        data class ErrorMessage(val msg: Int) : Notify(msg)
    }
}

