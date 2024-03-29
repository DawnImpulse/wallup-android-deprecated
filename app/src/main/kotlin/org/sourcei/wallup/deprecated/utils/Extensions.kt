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

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.FragmentManager
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import org.json.JSONObject
import org.sourcei.wallup.deprecated.R
import java.io.File


/**
 * @info - custom kotlin extension functions
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2018-12-04 by Saksham
 * @note Updates :
 */

// int color to hexa string
fun Int.toHexa(): String {
    return String.format("#%06X", 0xFFFFFF and this)
}

// gone view
fun View.gone() {
    visibility = View.GONE
}

// hide view
fun View.hide() {
    visibility = View.INVISIBLE
}

// gone view
fun View.show() {
    visibility = View.VISIBLE
}

// open activity
fun <T> Context.openActivity(it: Class<T>) {
    startActivity(Intent(this, it))
}

// open activity
fun <T> Context.openActivity(it: Class<T>, bundle: Bundle.() -> Unit = {}) {
    var intent = Intent(this, it)
    intent.putExtras(Bundle().apply(bundle))
    startActivity(intent)
}

// json put params
fun jsonOf(vararg pairs: Pair<String, Any>) = JSONObject().apply {
    pairs.forEach {
        put(it.first, it.second)
    }
}

// file path string to uri
fun String.toFileUri(): Uri {
    return Uri.fromFile(File(this))
}

// file path string to content uri
@Throws(Exception::class)
fun String.toContentUri(context: Context): Uri {
    try {
        return FileProvider.getUriForFile(context, "com.dawnimpulse.unsplash.wallup", toFile())
    } catch (e: Exception) {
        throw e
    }
    //return this.toFileUri().toContentUri(context)
}

// tree uri path to file uri path
fun String.toFileString(): String {
    return if (this.contains(":")) {
        val substring = split(":")
        val tree = substring[0]

        if (tree.contains("primary"))
            Environment.getExternalStorageDirectory().path + "/${substring[1]}"
        else
            "/storage/${tree.replace("/tree/", "")}/${substring[1]}"
    } else
        this
}

//convert to content uri
fun Uri.toContentUri(context: Context): Uri {
    val cr = context.contentResolver
    val file = File(this.path)
    val imagePath = file.absolutePath
    val imageName: String? = null
    val imageDescription: String? = null
    val uriString = MediaStore.Images.Media.insertImage(cr, imagePath, imageName, imageDescription)
    return Uri.parse(uriString)
}

// get mime type
fun Uri.getMime(context: Context): String? {
    val cr = context.contentResolver
    return cr.getType(this)
}

//get display ratio a/b
fun Context.displayRatio(): Pair<Int, Int> {
    /*fun gcd(p: Int, q: Int): Int {
        return if (q == 0) p;
        else gcd(q, p % q);
    }

    val point = F.displayDimensions(this)
    val x = point.x
    val y = point.y
    val gcd = gcd(x, y)

    val a = x / gcd
    val b = y / gcd

    return if (x > y)
        Pair(a, b)
    else
        Pair(b, a)*/

    fun calculateHcf(width1: Int, height1: Int): Int {
        var width = width1
        var height = height1
        while (height != 0) {
            val t = height
            height = width % height
            width = t
        }
        return width
    }

    val point = F.displayDimensions(this)
    val hcf = calculateHcf(point.x, point.y)

    return Pair(point.y / hcf, point.x / hcf)
}

// toast
fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

//covert to file type
fun String.toFile(): File {
    return File(this)
}

//modal sheet show
fun org.sourcei.wallup.deprecated.sheets.RoundedBottomSheetDialogFragment.show(supportFragmentManager: FragmentManager) {
    this.show(supportFragmentManager, this.tag)
}

//start web
fun Context.startWeb(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}

//show target
fun showTarget(context: AppCompatActivity, view: View, title: String, description: String, callback: () -> Unit) {
    var called = false
    TapTargetView.showFor(context,
            TapTarget.forView(view, title, description)
                    .outerCircleColor(R.color.colorAccent)
                    .targetCircleColor(R.color.black)
                    .titleTextSize(18)
                    .titleTextColor(R.color.black)
                    .descriptionTextSize(16)
                    .descriptionTextColor(R.color.black)
                    .drawShadow(true)
                    .cancelable(true)
                    .transparentTarget(true),
            object : TapTargetView.Listener() {
                override fun onOuterCircleClick(view: TapTargetView?) {
                    super.onOuterCircleClick(view)
                    if (!called) {
                        callback()
                        called = true
                        onTargetCancel(view)
                    }
                }

                override fun onTargetClick(view: TapTargetView?) {
                    super.onTargetClick(view)
                    if (!called) {
                        callback()
                        called = true
                    }
                }

                override fun onTargetDismissed(view: TapTargetView?, userInitiated: Boolean) {
                    super.onTargetDismissed(view, userInitiated)
                    if (!called) {
                        callback()
                        called = true
                    }
                }

                override fun onTargetCancel(view: TapTargetView?) {
                    super.onTargetCancel(view)
                    if (!called) {
                        callback()
                        called = true
                    }
                }
            }
    )
}