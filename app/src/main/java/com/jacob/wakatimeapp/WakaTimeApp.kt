package com.jacob.wakatimeapp

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class WakaTimeApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(WtaLogger())
        }
    }
}

class WtaLogger : Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (!message.contains("flow:")) return super.log(priority, tag, message, t)

        Log.println(
            priority,
            tag?.split("$$")?.first(),
            message.replace("flow:", ""),
        )
    }
}
