package org.gabrieal.simplefirebaseauth.data.di

import org.gabrieal.simplefirebaseauth.feature.auth.viewmodel.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    //ViewModel
    viewModel { AuthViewModel() }
}