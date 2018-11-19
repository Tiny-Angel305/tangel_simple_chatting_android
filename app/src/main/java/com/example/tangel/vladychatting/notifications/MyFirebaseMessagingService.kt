package com.example.tangel.vladychatting.notifications

import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import co.centroida.notifications.Constants
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseInstanceIDService : FirebaseMessagingService() {

    override fun onNewToken(p0: String?) {
        //super.onNewToken(p0)
        val refreshedToken = FirebaseInstanceId.getInstance().instanceId
        Log.d(TAG, "Refreshed token: " + refreshedToken!!)
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        preferences.edit().putString(Constants.FIREBASE_TOKEN, refreshedToken).apply()
    }

    companion object {
        private val TAG = "MyFirebaseIIDService"
    }
}