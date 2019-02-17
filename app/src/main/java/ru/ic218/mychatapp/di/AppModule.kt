package ru.ic218.mychatapp.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import ru.ic218.mychatapp.data.db.AppDatabase
import ru.ic218.mychatapp.data.repository.MessageRepositoryImpl
import ru.ic218.mychatapp.data.repository.FirebaseRepositoryImpl
import ru.ic218.mychatapp.data.repository.PrefRepositoryImpl
import ru.ic218.mychatapp.domain.interfaces.MessageRepository
import ru.ic218.mychatapp.domain.interfaces.FirebaseRepository
import ru.ic218.mychatapp.domain.model.PrefRepository
import ru.ic218.mychatapp.feature.main.MainInteractor
import ru.ic218.mychatapp.feature.main.MainViewModel

val myModuleDatabase: Module = module {
    single { AppDatabase.get(get()) }
}

val myModuleNetwork: Module = module {
    single { FirebaseFirestore.getInstance() }
}

val myModuleRepository: Module = module {
    factory { MainInteractor(get(), get(), get()) }

    single { MessageRepositoryImpl(get(), get(), get()) as MessageRepository }
    single { FirebaseRepositoryImpl(get()) as FirebaseRepository }
    single {
        PrefRepositoryImpl(
            androidApplication().getSharedPreferences(
                androidApplication().packageName + "_preferences",
                Context.MODE_PRIVATE
            )
        ) as PrefRepository
    }
}

val myModuleViewModel: Module = module {
    viewModel { MainViewModel(get(), get()) }
}