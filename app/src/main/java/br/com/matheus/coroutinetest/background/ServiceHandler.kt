package br.com.matheus.coroutinetest.background

import android.os.Handler
import android.os.Looper
import android.os.Message

class ServiceHandler(val lp: Looper) : Handler(lp) {

    override fun handleMessage(msg: Message?) {
        super.handleMessage(msg)
    }

}