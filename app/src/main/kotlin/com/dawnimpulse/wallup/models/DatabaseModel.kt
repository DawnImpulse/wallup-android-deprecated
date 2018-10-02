/*
ISC License

Copyright 2018, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/
package com.dawnimpulse.wallup.models

import androidx.lifecycle.Lifecycle
import com.dawnimpulse.wallup.repositories.DatabaseRepository

/**
 * @author Saksham
 *
 * @note Last Branch Update - recent
 * @note Created on 2018-05-20 by Saksham
 *
 * @note Updates :
 */
class DatabaseModel() {
    lateinit var lifecycle: Lifecycle

    /**
     * constructor with lifecycle
     */
    constructor(lifecycle: Lifecycle) : this() {
        this.lifecycle = lifecycle
    }

    /**
     * get trending images
     * @param timestamp
     * @param callback
     */
    fun getTrendingImages(timestamp: Int?, callback: (Any?, Any?) -> Unit) {
        DatabaseRepository.getTrendingImages(timestamp) { error, response ->
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                callback(error, response)

        }
    }
}