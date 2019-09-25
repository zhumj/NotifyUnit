package com.zhumj.notifyunit

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotifyUnit(private val context: Context) {

    private var notifyID = 0x10086 //通知id
    private var chanelId = "com.zhumj.notify.chanel_id"
    private var chanelDescription = "com.zhumj.notify.chanel_description"
    private var chanelName = "com.zhumj.notify.chanel_name"

    private var notifyManagerCompat: NotificationManagerCompat? = null

    companion object : SingletonHolder<NotifyUnit, Context>(::NotifyUnit)

    /**
     * 初始化并显示通知
     * @param icon       图标
     * @param title      标题
     * @param text       内容
     * @param ticker     悬挂式通知内容
     * @param time       时间
     * @param voiceTF    是否设置声音振动
     * @param autoCancel 设置通知打开后自动消失
     * @param cla        设置跳转的 页面
     * @return 返回 通知类
     */
    fun openNotify(icon: Int, title: String, text: String, ticker: String, time: Int, notifyDefault: Int, autoCancel: Boolean, cla: Class<*>?) {
        /**
         * 由于 Notification.Builder 仅支持 Android 4.1及之后的版本，为了解决兼容性问题， Notification.Builder 仅支持 API 26 与 26 之前的版本
         * Google 在 Android Support v4 中加入了 NotificationCompat.Builder 类
         */
        val channelId = createNotificationChannel(context.applicationContext)//创建Notification Channel
        val builder = NotificationCompat.Builder(context.applicationContext, channelId)//创建Notification并与Channel关联

        builder.setSmallIcon(icon)//设置通知图标
        builder.setAutoCancel(autoCancel)//设置通知打开后自动消失
        builder.setContentTitle(title)//设置标题
        builder.setContentText(text)//设置内容
        //悬挂式通知需设置setTicker
        builder.setTicker(ticker)
        //8.0以下悬挂式通知需要设置setDefaults和setPriority，缺一不可，且 priority 必须设置为 PRIORITY_HIGH
        builder.setDefaults(notifyDefault)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.priority = NotificationCompat.PRIORITY_HIGH
        }

        if (time == 0) {
            builder.setWhen(System.currentTimeMillis())//设置系统当前时间为发送时间
        } else {
            builder.setWhen(time.toLong())//设置用户设置的发送时间
        }

        if (cla != null) {    //如果 cla 不为空就设置跳转的页面
            val pi = PendingIntent.getActivity(context, 0, Intent(context, cla), PendingIntent.FLAG_CANCEL_CURRENT)
            builder.setContentIntent(pi)//设置通知栏 点击跳转
        }

        //发布通知
        notifyManagerCompat = NotificationManagerCompat.from(context.applicationContext)
        notifyManagerCompat?.notify(notifyID, builder.build())
    }

    /**
     * 创建通知
     * @param context
     * @return
     */
    private fun createNotificationChannel(context: Context): String {
        // O (API 26)及以上版本的通知需要NotificationChannels。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 初始化NotificationChannel。
            val notificationChannel =
                NotificationChannel(chanelId, chanelName, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description = chanelDescription

            // 向系统添加 NotificationChannel。试图创建现有通知
            // 通道的初始值不执行任何操作，因此可以安全地执行
            // 启动顺序
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)

            return chanelId
        } else {
            return ""
        }
    }

    fun setNotifyId(notifyId: Int): NotifyUnit {
        this.notifyID = notifyId
        return this
    }

    fun setChanelId(chanelId: String): NotifyUnit {
        this.chanelId = chanelId
        return this
    }

    fun setChanelDescription(chanelDescription: String): NotifyUnit {
        this.chanelDescription = chanelDescription
        return this
    }

    fun setChanelName(chanelName: String): NotifyUnit {
        this.chanelName = chanelName
        return this
    }

    fun closeNotifyById(notifyID: Int = this.notifyID) {
        notifyManagerCompat?.cancel(notifyID)
    }

    fun closeAllNotify() {
        notifyManagerCompat?.cancelAll()
    }
}