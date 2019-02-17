package ru.ic218.mychatapp.feature.main

import androidx.lifecycle.*
import ru.ic218.mychatapp.common.BaseViewModel
import ru.ic218.mychatapp.common.EditNameDialogFragment
import ru.ic218.mychatapp.common.Event
import ru.ic218.mychatapp.domain.model.PrefRepository
import kotlin.random.Random

class MainViewModel(
    private val interactor: MainInteractor,
    private val prefRepository: PrefRepository
) : BaseViewModel(), LifecycleObserver {

    private val handleOpenDialog = MutableLiveData<Event<EditNameDialogFragment>>()
    private val accountName = MutableLiveData<String>()
    private val accountCount = MutableLiveData<Int>()

    fun getMessagesLiveData() = interactor.getMessagesLiveData()
    fun getAccountNameLiveData() = accountName
    fun getAccountCountLiveData() = accountCount
    fun getHandleOpenDialogLiveData() = handleOpenDialog

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        if (!prefRepository.isCompleteInstall) {
            prefRepository.ownerId = Random.nextInt()
            createEditAccountNameDialog()
        } else {
            accountName.value = prefRepository.ownerName
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        handleSubscribeFirebase()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        handleUnSubscribeFirebase()
    }

    fun sendMessage(messageText: String) {
        compositeDisposable.add(
            interactor.sendMessage(messageText)
                .subscribe({ "Message Send" }, { println(it.localizedMessage) })
        )
    }

    fun setAccountName(name: String) {
        prefRepository.ownerName = name

        if (!prefRepository.isCompleteInstall) {
            finishInitApplication()
        } else {
            interactor.updateNameToFirebase(name)
        }
    }

    fun handleSubscribeFirebase() {
        interactor.subscribeFirebase(
            accountCallback = { accountCount.postValue(it) }
        )
    }

    fun handleUnSubscribeFirebase() {
        interactor.unSubscribeFirebase()
    }

    private fun finishInitApplication() {
        prefRepository.isCompleteInstall = true
        handleSubscribeFirebase()
    }

    fun createEditAccountNameDialog(name: String = "") {
        handleOpenDialog.value = Event(EditNameDialogFragment.newInstance(name).apply {
            isCancelable = false
        })
    }
}