package ru.dmisuvorov.currencyconverter.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_currency.*
import ru.dmisuvorov.currencyconverter.App
import ru.dmisuvorov.currencyconverter.R
import ru.dmisuvorov.currencyconverter.presentation.CurrencyViewModel
import javax.inject.Inject

class CurrencyActivity : AppCompatActivity() {
    @Inject
    lateinit var currencyViewModel: CurrencyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency)
        injectDependencies()
        initViewModels()
        btn_convert.setOnClickListener {
            currencyViewModel.convertCurrency(
                spinner_from.selectedItem.toString(),
                spinner_to.selectedItem.toString(),
                et_fromCurrency.text.toString()
            )
        }
    }

    private fun injectDependencies() {
        App.component.inject(this)
    }

    private fun initViewModels() {
        currencyViewModel.state.observe(this, Observer { state ->
            when (state) {
                is CurrencyViewModel.CurrencyState.LOADING -> {
                    progress.visibility = View.VISIBLE
                    btn_convert.isClickable = false
                }
                is CurrencyViewModel.CurrencyState.ERROR -> {
                    progress.visibility = View.GONE
                    btn_convert.isClickable = true
                    et_toCurrency.setText("")
                }
                is CurrencyViewModel.CurrencyState.DEFAULT -> {
                    progress.visibility = View.GONE
                    btn_convert.isClickable = true
                    et_toCurrency.setText("")
                }
                is CurrencyViewModel.CurrencyState.SUCCESS_CONVERT -> {
                    progress.visibility = View.GONE
                    btn_convert.isClickable = true
                    et_toCurrency.setText(state.convertCurrency)
                }
            }
        })
        currencyViewModel.notifications.observe(
            this,
            CurrencyViewModel.EventObserver { notification ->
                Snackbar.make(
                    constraint_container,
                    getString(notification.messageRes),
                    Snackbar.LENGTH_LONG
                )
                    .show()
            })
    }

}
