package com.eyeronic09.freepdf.di

import com.eyeronic09.freepdf.homescreen.data.reposistory.ReposistoryImpl
import com.eyeronic09.freepdf.homescreen.domain.Repository.PdfReposistory
import com.eyeronic09.freepdf.homescreen.ui.HomeScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<PdfReposistory> { ReposistoryImpl(androidContext()) }
    viewModel { HomeScreenViewModel(get()) }
}
