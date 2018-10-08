/*
ISC License

Copyright 2018, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/package com.dawnimpulse.wallup.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog


/**
 * @author Saksham
 *
 * @note Last Branch Update -
 * @note Created on 2018-10-04 by Saksham
 *
 * @note Updates :
 */
object Dialog {
    private lateinit var alertDialog: AlertDialog

    // simple ok dialog
    fun simpleOk(context: Context, title: String,message: String, positive: DialogInterface.OnClickListener) {
        var builder = AlertDialog.Builder(context)
        builder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", positive)
                .setNegativeButton("CANCEL") { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(false)

        alertDialog = builder.create()
        alertDialog.show()

    }

    // dismiss
    fun dismiss(){
        alertDialog.dismiss()
    }
}