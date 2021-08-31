package kr.ac.konkuk.roomdemo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.ac.konkuk.roomdemo.db.Subscriber
import kr.ac.konkuk.roomdemo.db.SubscriberRepository

//need to have a reference to SubscriberRepository
//so add an instance of SubscriberRepository
//as a constructor parameter
class SubscriberViewModel(private val repository: SubscriberRepository): ViewModel(){

    val subscribers = repository.subscribers

    val inputName = MutableLiveData<String?>()

    val inputEmail = MutableLiveData<String?>()

    val saveOrUpdateButtonText = MutableLiveData<String>()

    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "ClearAll"
    }

    fun saveOrUpdate() {
        val name = inputName.value!!
        val email = inputEmail.value!!
        insert(Subscriber(0, name, email))
        //Since, we set the primary key subscriber id as a auto incremental value,
        // we can just add zero as the id for all Subscriber entities.
        // Room will just ignore it and add the auto incremental value.
        inputName.value = null
        inputEmail.value = null
    }

    fun clearAllOrDelete() {
        clearAll()
    }

    fun insert(subscriber: Subscriber) =
        viewModelScope.launch {
            repository.insert(subscriber)
        }

    fun update(subscriber: Subscriber) = viewModelScope.launch {
        repository.update(subscriber)
    }

    fun delete(subscriber: Subscriber) = viewModelScope.launch {
        repository.delete(subscriber)
    }

    fun clearAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}