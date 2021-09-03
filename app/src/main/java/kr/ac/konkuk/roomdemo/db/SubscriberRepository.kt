package kr.ac.konkuk.roomdemo.db

import androidx.lifecycle.LiveData
import androidx.room.*

class SubscriberRepository(private val dao : SubscriberDAO) {

    val subscribers = dao.getAllSubscribers()

    //return Long (rowId) to validate user input
    suspend fun insert(subscriber: Subscriber): Long {
        return dao.insertSubscriber(subscriber)
    }

    suspend fun update(subscriber: Subscriber): Int {
        return dao.updateSubscriber(subscriber)
    }

    suspend fun delete(subscriber: Subscriber) : Int{
        return dao.deleteSubscriber(subscriber)
    }

    suspend fun deleteAll() : Int {
        return dao.deleteAll()
    }
}