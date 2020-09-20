package com.mankovskaya.githubtest.system.di

import android.content.Context
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.mankovskaya.githubtest.BuildConfig
import com.mankovskaya.githubtest.core.android.ResourceManager
import com.mankovskaya.githubtest.model.feature.LoginViewModel
import com.mankovskaya.githubtest.model.feature.RepositoriesViewModel
import com.mankovskaya.githubtest.model.mock.AuthMockService
import com.mankovskaya.githubtest.model.network.AuthApi
import com.mankovskaya.githubtest.model.network.BasicAuthenticator
import com.mankovskaya.githubtest.model.network.ErrorInterceptor
import com.mankovskaya.githubtest.model.network.SearchApi
import com.mankovskaya.githubtest.model.repository.AuthRepository
import com.mankovskaya.githubtest.model.repository.CredentialsHolder
import com.mankovskaya.githubtest.model.repository.RepoRepository
import com.mankovskaya.githubtest.system.scheduler.AppSchedulers
import com.mankovskaya.githubtest.system.scheduler.SchedulersProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

val appModule = module {
    single { AuthMockService() }
    single { ResourceManager(androidContext()) }
    single { CredentialsHolder(androidContext()) }
    single<SchedulersProvider> { AppSchedulers() }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RepositoriesViewModel(get(), get()) }
}

val networkModule = module {
    factory { BasicAuthenticator(get()) }
    factory { provideOkHttpClient(get(), get()) }
    factory { provideAuthApi(get()) }
    factory { provideSearchApi(get()) }
    single { provideRetrofit(get()) }
    single { AuthRepository(get(), get(), get()) }
    single { RepoRepository(get(), get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(BuildConfig.API_ROOT).client(okHttpClient)
        .addConverterFactory(JacksonConverterFactory.create(provideObjectMapper()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
}

fun provideOkHttpClient(authInterceptor: BasicAuthenticator, context: Context): OkHttpClient {
    return OkHttpClient()
        .newBuilder()
        .addInterceptor(authInterceptor)
        .addInterceptor(ErrorInterceptor(context))
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
}

fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

fun provideSearchApi(retrofit: Retrofit): SearchApi = retrofit.create(SearchApi::class.java)

fun provideObjectMapper() =
    ObjectMapper().apply {
        propertyNamingStrategy = PropertyNamingStrategy.LOWER_CASE
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        registerModule(KotlinModule())
    }
