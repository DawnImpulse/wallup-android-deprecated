/*
ISC License

Copyright 2018-2019, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/
package org.sourcei.wallup.deprecated.utils

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import org.sourcei.wallup.deprecated.BuildConfig
import org.sourcei.wallup.deprecated.R

/**
 * @author Saksham
 *
 * @note Last Branch Update -
 * @note Created on 2018-09-15 by Saksham
 *
 * @note Updates :
 */
object RemoteConfig {

    // update to the app
    fun update() {
        var cacheExpiration: Long = 3600 // 1 hour in seconds.
        val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()

        if (mFirebaseRemoteConfig.info.configSettings.isDeveloperModeEnabled) {
            cacheExpiration = 0
        }

        mFirebaseRemoteConfig.setConfigSettings(configSettings)
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_defaults)
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        mFirebaseRemoteConfig.activateFetched()
                    } else
                        Log.d("Test", "Fetch failed")
                }

    }

    // get remote config update value
    fun getProductionUpdateValues(): org.sourcei.wallup.deprecated.pojo.UpdatePojo? {
        val value = FirebaseRemoteConfig.getInstance().getString(org.sourcei.wallup.deprecated.utils.C.UPDATE_PRODUCTION)
        value?.let {
            return Gson().fromJson(value, org.sourcei.wallup.deprecated.pojo.UpdatePojo::class.java)
        }
        return null;
    }

    // get remote config privacy
    fun getPrivacyLink(): String {
        return FirebaseRemoteConfig.getInstance().getString(org.sourcei.wallup.deprecated.utils.C.PRIVACY)
    }

    // get remote config tnc
    fun getTnC(): String {
        return FirebaseRemoteConfig.getInstance().getString(org.sourcei.wallup.deprecated.utils.C.TNC)
    }

    // get about background image
    fun getAboutImage(): String {
        return FirebaseRemoteConfig.getInstance().getString(org.sourcei.wallup.deprecated.utils.C.REMOTE_IMAGE)
    }

    // get bug url
    fun getBugUrl(): String {
        return FirebaseRemoteConfig.getInstance().getString(org.sourcei.wallup.deprecated.utils.C.BUG)
    }

    // get bug url
    fun getFeatureUrl(): String {
        return FirebaseRemoteConfig.getInstance().getString(org.sourcei.wallup.deprecated.utils.C.FEATURE)
    }

}