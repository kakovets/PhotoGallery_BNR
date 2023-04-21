package com.kakovets.photogallery

import android.os.HandlerThread
import android.util.Log
import androidx.lifecycle.*

private const val TAG = "ThumbnailDownloader"

class ThumbnailDownloader<in T> : HandlerThread(TAG), LifecycleObserver, LifecycleEventObserver {

    private var hasQuit = false

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                Log.i(TAG, "Starting background thread")
                start()
                looper
            }
            Lifecycle.Event.ON_DESTROY -> {
                Log.i(TAG, "Destroying background thread")
                quit()
            }
            else -> Log.i(TAG, "Another event")

        }
    }

    override fun quit(): Boolean {
        hasQuit = true
        return super.quit()
    }

    fun queueThumbNail(target: T, url: String) {
        Log.i(TAG, "Got a url: $url")
    }
}
/*@OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
fun setup() {
    Log.i(TAG, "Starting background thread")
    start()
    looper
}

@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
fun tearDown() {
    Log.i(TAG, "Destroying background thread")
    quit()
}*/