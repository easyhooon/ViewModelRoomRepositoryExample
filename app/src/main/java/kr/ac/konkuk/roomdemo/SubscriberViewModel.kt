package kr.ac.konkuk.roomdemo

import android.util.Patterns
import androidx.lifecycle.LiveData
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

    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete : Subscriber

    //private data
    private val statusMessage = MutableLiveData<Event<String>>()

    //public getter
    val message : LiveData<Event<String>>
        get() = statusMessage

    val inputName = MutableLiveData<String?>()

    val inputEmail = MutableLiveData<String?>()

    val saveOrUpdateButtonText = MutableLiveData<String>()

    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {

        if(inputName.value == null){
            statusMessage.value = Event("Please enter subscriber's name")
        } else if (inputEmail.value == null) {
            statusMessage.value = Event("Please enter subscriber's email")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()){
            statusMessage.value = Event("Please enter subscriber's email")
        } else {
            if (isUpdateOrDelete) {
                subscriberToUpdateOrDelete.name = inputName.value!!
                subscriberToUpdateOrDelete.email = inputEmail.value!!

                update(subscriberToUpdateOrDelete)
            } else {
                val name = inputName.value!!
                val email = inputEmail.value!!
                insert(Subscriber(0, name, email))
                //Since, we set the primary key subscriber id as a auto incremental value,
                // we can just add zero as the id for all Subscriber entities.
                // Room will just ignore it and add the auto incremental value.
                inputName.value = null
                inputEmail.value = null
            }
        }
    }

    fun clearAllOrDelete() {
        if(isUpdateOrDelete) {
            delete(subscriberToUpdateOrDelete)
        }else {
            clearAll()
        }
    }

    fun insert(subscriber: Subscriber) =
        viewModelScope.launch {
            val newRowId = repository.insert(subscriber)
            if(newRowId > -1){
                statusMessage.value = Event("Subscriber Inserted Successfully")
            } else {
                //정상적인 Input 에 의한 return 값이 아닌 이유
                statusMessage.value = Event("Error Occurred")
            }

        }

    fun update(subscriber: Subscriber) = viewModelScope.launch {
        val noOfRows = repository.update(subscriber)

        if(noOfRows > 0) {
            inputName.value = null
            inputEmail.value = null
            isUpdateOrDelete = false

            saveOrUpdateButtonText.value = "save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRows Row Updated Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }

    }

    fun delete(subscriber: Subscriber) = viewModelScope.launch {
        val noOfRowsDeleted = repository.delete(subscriber)

        if(noOfRowsDeleted > 0) {
            inputName.value = null
            inputEmail.value = null
            isUpdateOrDelete = false

            saveOrUpdateButtonText.value = "save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRowsDeleted Row Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }



    }

    fun clearAll() = viewModelScope.launch {
        val noOfRowsDeleted = repository.deleteAll()

        if(noOfRowsDeleted > 0) {
            statusMessage.value = Event("$noOfRowsDeleted Subscriber Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }


    }

    fun initUpdateAndDelete(subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber

        //test
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }
}