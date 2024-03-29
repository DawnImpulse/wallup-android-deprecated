/*
ISC License

Copyright 2018-2019, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/package org.sourcei.wallup.deprecated.handlers

import java.text.SimpleDateFormat

/**
 * @author Saksham
 *
 * @note Last Branch Update -
 * @note Created on 2018-05-27 by Saksham
 *
 * @note Updates :
 */
object DateHandler {
    private val NAME = "DateHandler"

    /**
     * Convert date string to image preview format activity
     */
    fun convertForImagePreview(date: String): String {
        var sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(date)
        return SimpleDateFormat("dd MMM ''yy").format(sdf)
    }
}