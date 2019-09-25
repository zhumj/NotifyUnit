package com.zhumj.notifyunit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startNotify(view: View) {
        NotifyUnit.getInstance(this).openNotify(R.mipmap.ic_launcher_round, "我的通知-标题", "我的通知-内容", "我的通知-Tiker", 0, NotificationCompat.DEFAULT_ALL, true, null)
    }

    fun closeNotify(view: View) {
        NotifyUnit.getInstance(this).closeNotifyById()
    }
}
