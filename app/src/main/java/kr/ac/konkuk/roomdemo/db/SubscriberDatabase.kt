package kr.ac.konkuk.roomdemo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kr.ac.konkuk.roomdemo.db.SubscriberDAO


@Database(entities = [Subscriber::class],version = 1)
abstract class SubscriberDatabase : RoomDatabase() {

    abstract val subscriberDAO : SubscriberDAO

    //singleton creation code part(boilerplate code)
    companion object{
        @Volatile
        private var INSTANCE : SubscriberDatabase? = null
        //Volatile annotation makes the field immediately made visible to other threads
        fun getInstance(context: Context):SubscriberDatabase{
            synchronized(this){
                var instance = INSTANCE
                    if(instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            SubscriberDatabase::class.java,
                            "subscriber_data_database"
                        ).build()
                    }
                return instance
            }
        }
    }
}