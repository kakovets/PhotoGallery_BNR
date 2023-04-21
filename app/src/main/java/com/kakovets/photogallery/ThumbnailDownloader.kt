package com.kakovets.photogallery

import android.annotation.SuppressLint
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import androidx.lifecycle.*
import java.util.concurrent.ConcurrentHashMap

private const val TAG = "ThumbnailDownloader"
private const val MESSAGE_DOWNLOAD = 0

class ThumbnailDownloader<in T> : HandlerThread(TAG), LifecycleObserver, LifecycleEventObserver {

    private var hasQuit = false
    private lateinit var requestHandler: Handler
    private val requestMap = ConcurrentHashMap<T, String>()
    private val flickrFetchr = FlickrFetchr()

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("HandlerLeak")
    override fun onLooperPrepared() {
        requestHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    val target = msg.obj as T
                    Log.i(TAG, "Got a request for URL: ${requestMap[target]}")
                    handleRequest(target)
                }
            }
        }
    }

    private fun handleRequest(target: T) {
        val url = requestMap[target] ?: return
        val bitmap = flickrFetchr.fetchPhoto(url) ?: return
    }

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
        requestMap[target] = url
        requestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget()
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