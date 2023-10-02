package com.comprehensivetutorial.udemyroomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.comprehensivetutorial.udemyroomdemo.database.SubscriberDatabase
import com.comprehensivetutorial.udemyroomdemo.database.SubscriberRepository
import com.comprehensivetutorial.udemyroomdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val subscribersViewModel: SubscribersViewModel by viewModels {
        SubscribersViewModel.Factory
    }
    private lateinit var subscribersListView: RecyclerView
    private lateinit var subscriberListAdapter: SubscribersListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.subscriberViewModel = subscribersViewModel
        binding.lifecycleOwner = this

//        var dao = SubscriberDatabase.getDatabase(applicationContext).subscriberDao
//        val repository = SubscriberRepository(dao)
//        val factory = SubscriberViewModelFactory(repository)

        subscribersViewModel.modelState.observe(this, Observer {
            when(it) {
                SubscriberModelState.INSERTING -> {
                    subscribersViewModel.clearAllOrDeleteBtnText.value = getString(R.string.clear_all)
                    subscribersViewModel.saveOrUpdateBtnText.value = getString(R.string.save)
                }
                SubscriberModelState.UPDATING -> {
                    subscribersViewModel.clearAllOrDeleteBtnText.value = getString(R.string.delete)
                    subscribersViewModel.saveOrUpdateBtnText.value = getString(R.string.update)
                }
            }
        })

        initListView()
    }

    private fun initListView() {
        subscribersListView = binding.subscribersListView

        subscribersListView.layoutManager = LinearLayoutManager(applicationContext)
        subscriberListAdapter = SubscribersListView {
            Toast.makeText(this, "Subscriber : name - ${it.name}, email - ${it.email}", Toast.LENGTH_SHORT).show()
            subscribersViewModel.initUpdateOrDelete(it)
        }
        subscribersListView.adapter = subscriberListAdapter

        displaySubscribersList()
    }

    private fun displaySubscribersList() {
        subscribersViewModel.subscribers.observe(this) {
            subscriberListAdapter.submitList(it)
            subscriberListAdapter.notifyDataSetChanged()
        }
    }
}

