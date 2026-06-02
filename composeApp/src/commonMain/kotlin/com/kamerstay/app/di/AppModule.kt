package com.kamerstay.app.di

import com.kamerstay.app.viewmodel.AuthViewModel
import com.kamerstay.app.viewmodel.ManagerViewModel
import com.kamerstay.app.viewmodel.TravelerViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { AuthViewModel() }
    viewModel { TravelerViewModel() }
    viewModel { ManagerViewModel() }
}