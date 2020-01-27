package ru.dmisuvorov.currencyconverter.di.modules

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.dmisuvorov.currencyconverter.APP_CONFIG.BASE_URL
import ru.dmisuvorov.currencyconverter.data.local.entities.Rates
import ru.dmisuvorov.currencyconverter.data.remote.CurrencyApi
import ru.dmisuvorov.currencyconverter.data.remote.response.RatesResponse
import java.lang.reflect.Type
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideCurrencyApi(retrofit: Retrofit): CurrencyApi =
        retrofit.create(CurrencyApi::class.java)


    @Provides
    @Singleton
    fun provideRetrofit(
        converterFactory: Converter.Factory,
        callAdapterFactory: CallAdapter.Factory,
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideConverterFactory(gson: Gson): Converter.Factory = GsonConverterFactory.create(gson)


    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder().registerTypeAdapter(RatesResponse::class.java, object : JsonDeserializer<RatesResponse> {
            override fun deserialize(
                json: JsonElement?,
                typeOfT: Type?,
                context: JsonDeserializationContext?
            ): RatesResponse =
                RatesResponse(Gson().fromJson(json!!.asJsonObject, object : TypeToken<Map<String, Double>>() {}.type))
        }).create()

    @Provides
    @Singleton
    fun provideCallAdapterFactory(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()
}