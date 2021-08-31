package kr.ac.konkuk.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kr.ac.konkuk.roomdemo.databinding.ActivityMainBinding
import kr.ac.konkuk.roomdemo.db.SubscriberDatabase
import kr.ac.konkuk.roomdemo.db.SubscriberRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //create DAO Instance
        val dao = SubscriberDatabase.getInstance(applicationContext).subscriberDAO

        val repository = SubscriberRepository(dao)

        val factory = SubscriberViewModelFactory(repository)

        subscriberViewModel = ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)

        binding.myViewModel = subscriberViewModel
        binding.lifecycleOwner = this

        displaySubscribersList()
    }

    //function to observe the list of subscriber's data in the database
    //will display these data on a recyclerview
    private fun displaySubscribersList() {
        //observe the list of subscribers, which is in livedata format.
        //fun getAllSubscribers() in SubscriberDAO return LiveData type

        //observe that livedata from the MainActivity
        subscriberViewModel.subscribers.observe(this, Observer {
            Log.i("MyTag", it.toString())
        })
    }
}