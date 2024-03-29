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
package org.sourcei.wallup.deprecated.handlers

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.net.Uri
import android.view.Display
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.sourcei.wallup.deprecated.utils.getMime
import org.sourcei.wallup.deprecated.utils.toast

/**
 * @author Saksham
 *
 * @note Last Branch Update -
 * @note Created on 2018-08-03 by Saksham
 *
 * @note Updates :
 *  Saksham - 2018 31 12 - master - crop & set wallpaper
 *  Saksham - 2019 02 06 - master - exception handling
 */
object WallpaperHandler {

    // set wallpaper with bitmap
    fun setHomescreenWallpaper(context: Context, bitmap: Bitmap, shouldCrop: Boolean = true) {
        var bitmap2 = bitmap
        if (shouldCrop)
            bitmap2 = org.sourcei.wallup.deprecated.handlers.WallpaperHandler.bitmapCropper(bitmap, context)!!

        GlobalScope.launch {
            val wallpaperManager = WallpaperManager.getInstance(context)
            wallpaperManager.setBitmap(bitmap2)

            (context as AppCompatActivity).runOnUiThread {
                context.toast("Wallpaper Applied")
            }
        }
    }

    fun cropAndSetWallpaper(context: Context, uri: Uri) {
        val mime = uri.getMime(context)
        if (mime != null && mime.contains("image")) {
            val wallpaperManager = WallpaperManager.getInstance(context)
            try {
                context.startActivity(wallpaperManager.getCropAndSetWallpaperIntent(uri))
            } catch (e: Exception) {
                e.printStackTrace()
                context.toast("Issue with getting image from storage!! Kindly switch to Internal Storage if you have selected External SD Card.")
            }
        } else
            context.toast("Issue with getting image from storage!! Kindly switch to Internal Storage if you have selected External SD Card.")
    }

    /**
     * Handling of bitmap cropping based on device screen
     * Could be used in future for external cropping too
     *
     * @param originalBitmap
     * @return
     */
    private fun bitmapCropper(originalBitmap: Bitmap, context: Context): Bitmap? {

        val scaleHcf: Int
        val scaleX: Int
        val scaleY: Int
        val originalWidth: Int
        val originalHeight: Int
        var width = 0
        var height = 0

        val point: Point
        val mWindowManager: WindowManager
        val display: Display
        var modifiedBitmap: Bitmap? = null
        val scaledBitmap: Bitmap? = null

        point = Point()
        mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        display = mWindowManager.defaultDisplay
        display.getSize(point) //The point now has display dimens

        originalWidth = originalBitmap.width
        originalHeight = originalBitmap.height
        scaleHcf = org.sourcei.wallup.deprecated.handlers.WallpaperHandler.calculateHcf(point.x, point.y)

        // If bitmap is null or some other problem
        if (originalWidth == 0) {
            return null
        }

        /* Get X & Y scaling increment factor
    *  If ratio i.e. hcf is less than 20 then use it else divide it by 8
    */
        scaleX = if (point.x / scaleHcf > 20) point.x / scaleHcf / 8 else point.x / scaleHcf
        scaleY = if (point.y / scaleHcf > 20) point.y / scaleHcf / 8 else point.y / scaleHcf

        //Loop while incrementing width and height by scaling factors
        while (width < originalWidth && height < originalHeight) {
            width += scaleX
            height += scaleY
        }

        //Decrease one scaling factor so it wont exceed the max bitmap length
        width -= scaleX
        height -= scaleY

        //Get the starting point to crop the original Bitmap
        var startingPointX = (originalWidth - width) / 2
        var startingPointY = (originalHeight - height) / 2

        // if we get starting point less than 0 then make it 0
        startingPointX = if (startingPointX < 0) 0 else startingPointX
        startingPointY = if (startingPointY < 0) 0 else startingPointY

        //Create cropped version of original bitmap
        modifiedBitmap = Bitmap.createBitmap(originalBitmap, startingPointX, startingPointY, width, height)
        //Create final scaled bitmap based on exact screen size
        val finalBitmap = Bitmap.createScaledBitmap(modifiedBitmap!!, point.x, point.y, false)

        modifiedBitmap.recycle()
        return finalBitmap
    }

    /**
     * Get hcf of width & height of an image
     *
     * @param width  - Width of device
     * @param height - Height of device
     * @return
     */
    private fun calculateHcf(width: Int, height: Int): Int {
        var width = width
        var height = height
        while (height != 0) {
            val t = height
            height = width % height
            width = t
        }
        return width
    }
}