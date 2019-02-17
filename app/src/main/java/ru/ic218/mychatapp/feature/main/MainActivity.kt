package ru.ic218.mychatapp.feature.main

import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.ic218.mychatapp.R
import ru.ic218.mychatapp.common.EditNameDialogFragment
import ru.ic218.mychatapp.common.EventObserver
import ru.ic218.mychatapp.common.InitialsView

class MainActivity : AppCompatActivity(), EditNameDialogFragment.EditNameDialogListener {

    private val viewModel by viewModel<MainViewModel>()
    private val adapter = MessageAdapter()

    private val networkBroadcastListener = NetworkBroadcastListener(callback = {
        viewModel.handleUnSubscribeFirebase()
        if(!it) {
            accountLiveCount.text = getString(R.string.network_state_hint)
        } else {
            viewModel.handleSubscribeFirebase()
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle.addObserver(viewModel)

        setupToolbar()
        initViews()
        subscribe()
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(networkBroadcastListener, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(networkBroadcastListener)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
    }

    private fun initViews() {
        messagesList.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        messagesList.setHasFixedSize(true)
        messagesList.adapter = adapter

        replyImg.setOnClickListener {
            viewModel.sendMessage(messageEdit.text.toString())
            messageEdit.text.clear()
        }
        accountLayout.setOnClickListener {
            viewModel.createEditAccountNameDialog(accountName.text.toString())
        }
    }

    private fun subscribe() {
        viewModel.getMessagesLiveData().observe(this, Observer {
            it?.let {
                adapter.submitList(it)
                if (it.isNotEmpty()) Handler().postDelayed({ messagesList.smoothScrollToPosition(it.size - 1) }, 150)
            }
        })
        viewModel.getAccountNameLiveData().observe(this, Observer {
            it?.let { setAccountInfo(it) }
        })
        viewModel.getAccountCountLiveData().observe(this, Observer {
            it?.let { accountLiveCount.text = getString(R.string.account_count_hint, it) }
        })
        viewModel.getHandleOpenDialogLiveData().observe(this, EventObserver {
            it.show(supportFragmentManager, "edit_name")
        })
    }

    private fun setAccountInfo(name: String) {
        accountName.text = name
        (accountIcon as InitialsView).populateInitials(name)
    }

    override fun onFinishEditDialog(inputText: String) {
        setAccountInfo(inputText)
        viewModel.setAccountName(inputText)
    }
}
