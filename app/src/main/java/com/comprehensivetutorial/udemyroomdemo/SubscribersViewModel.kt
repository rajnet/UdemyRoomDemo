package com.comprehensivetutorial.udemyroomdemo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.comprehensivetutorial.udemyroomdemo.database.Subscriber
import com.comprehensivetutorial.udemyroomdemo.database.SubscriberDatabase
import com.comprehensivetutorial.udemyroomdemo.database.SubscriberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException

class SubscribersViewModel(
    private val repository: SubscriberRepository
): ViewModel() {
    val subscribers = repository.subscribers

    val inputName = MutableLiveData<String>()
    val inputEmail = MutableLiveData<String>()

    val saveOrUpdateBtnText = MutableLiveData<String>()
    val clearAllOrDeleteBtnText = MutableLiveData<String>()

    private var _modelState = MutableLiveData<SubscriberModelState>()
    init {
        _modelState.value = SubscriberModelState.INSERTING
    }

    val modelState: LiveData<SubscriberModelState>
        get() = _modelState

    private var isUpdateOrDelete = false
    private var selectedSubscriber: Subscriber? = null


    fun initUpdateOrDelete(subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        _modelState.value = SubscriberModelState.UPDATING
        isUpdateOrDelete = true
        selectedSubscriber = subscriber

    }

    fun onSaveOrUpdate() {
        val name = inputName.value!!
        val email = inputEmail.value!!

        if(isUpdateOrDelete) {
            updateSubscriber(
                Subscriber(
                    selectedSubscriber!!.id,
                    name,
                    email
                )
            )
        }
        else {
            insertSubscriber(
                Subscriber(
                    0,
                    name,
                    email
                )
            )
        }


        inputName.value = ""
        inputEmail.value = ""
    }

    fun onClearAllOrDelete() {
        if(isUpdateOrDelete) {
            deleteSubscriber(selectedSubscriber!!)
        }
        else {
            deleteAllSubscribers()
        }
    }

    fun insertSubscriber(subscriber: Subscriber) = viewModelScope.launch(
        Dispatchers.IO
    ) {
        val newRowId = repository.insert(subscriber)
        Log.i("MYTAG", newRowId.toString())
    }

    fun updateSubscriber(subscriber: Subscriber) = viewModelScope.launch(
        Dispatchers.IO
    ) {
        repository.update(subscriber)
        withContext(Dispatchers.Main) {
            inputName.value = ""
            inputEmail.value = ""
            isUpdateOrDelete = false
            selectedSubscriber = null
            _modelState.value = SubscriberModelState.INSERTING
        }
    }

    fun deleteSubscriber(subscriber: Subscriber) = viewModelScope.launch(
        Dispatchers.IO
    ) {
        repository.delete(subscriber)
        withContext(Dispatchers.Main) {
            inputName.value = ""
            inputEmail.value = ""
            isUpdateOrDelete = false
            selectedSubscriber = null
            _modelState.value = SubscriberModelState.INSERTING
        }
    }

    private fun deleteAllSubscribers() = viewModelScope.launch(
        Dispatchers.IO
    ) {
        try {
            val deletedSubsCount = repository.deleteAll()
            Log.i("MYTAG", "DELETED COUNT - $deletedSubsCount")
        }
        catch (e: Exception) {
            Log.i("MYTAG", "ERROR::" + e.message.toString())
        }

    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                if(modelClass.isAssignableFrom(SubscribersViewModel::class.java)) {
                    val application = checkNotNull(extras[APPLICATION_KEY])
                    var dao = SubscriberDatabase.getDatabase(
                        application.applicationContext
                    ).subscriberDao
                    return SubscribersViewModel(
                        SubscriberRepository(dao)
                    ) as T
                }

                throw IllegalArgumentException("Wrong view Model")
            }
        }
    }

}

