package kr.ac.konkuk.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kr.ac.konkuk.roomdemo.databinding.ActivityMainBinding
import kr.ac.konkuk.roomdemo.db.Subscriber
import kr.ac.konkuk.roomdemo.db.SubscriberDatabase
import kr.ac.konkuk.roomdemo.db.SubscriberRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel
    private lateinit var adapter: MyRecyclerViewAdapter

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

        initRecyclerView()

        subscriberViewModel.message.observe(this, Observer{
            it.getContentIfNotHandled()?.let{
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initRecyclerView(){
        binding.subscriberRecyclerview.layoutManager = LinearLayoutManager(this)
        adapter = MyRecyclerViewAdapter({selectedItem:Subscriber -> listItemClicked(selectedItem)})
        binding.subscriberRecyclerview.adapter = adapter
        displaySubscribersList()
    }

    //function to observe the list of subscriber's data in the database
    private fun displaySubscribersList() {
        //observe the list of subscribers, which is in livedata format.
        //fun getAllSubscribers() in SubscriberDAO return LiveData type

        //observe that livedata from the MainActivity

        //now we are just re using the initially created adapter object
        subscriberViewModel.subscribers.observe(this, Observer {
            Log.i("MyTag", it.toString())

            adapter.setList(it)
            //So, we must tell recycler view,that there is a new update
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(subscriber: Subscriber){
        //Toast.makeText(this, "selected name is ${subscriber.name}", Toast.LENGTH_SHORT).show()
        subscriberViewModel.initUpdateAndDelete(subscriber)
    }
}