package ru.ic218.mychatapp

import androidx.multidex.MultiDexApplication
import org.koin.android.ext.android.startKoin
import ru.ic218.mychatapp.di.myModuleDatabase
import ru.ic218.mychatapp.di.myModuleNetwork
import ru.ic218.mychatapp.di.myModuleRepository
import ru.ic218.mychatapp.di.myModuleViewModel

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(myModuleViewModel, myModuleDatabase, myModuleNetwork, myModuleRepository))
    }
}