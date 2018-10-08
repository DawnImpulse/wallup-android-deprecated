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
package com.dawnimpulse.wallup.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.widget.toast
import androidx.palette.graphics.Palette
import com.dawnimpulse.permissions.android.Permissions
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.handlers.*
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.sheets.ModalSheetExif
import com.dawnimpulse.wallup.sheets.ModalSheetUnsplash
import com.dawnimpulse.wallup.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_image.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

/**
 * @author Saksham
 *
 * @note Last Branch Update - recent
 * @note Created on 2018-05-24 by Saksham
 *
 * @note Updates :
 *  Saksham - 2018 05 25 - recent - working with single image display
 *  Saksham - 2018 07 20 - recent - adding listeners
 *  Saksham - 2018 07 26 - recent - downloading
 *  Saksham - 2018 09 01 - master - exif bottom sheet
 *  Saksham - 2018 09 06 - master - unsplash & image share handling
 *  Saksham - 2018 09 15 - master - handling direct unsplash links
 *  Saksham - 2018 09 20 - master - ML for tags
 */
class ImageActivity : AppCompatActivity(), View.OnClickListener {
    private val NAME = "ImageActivity"
    private var setBitmap = false
    private var bitmap: Bitmap? = null
    private var fullDetails: ImagePojo? = null
    private var color: Int = 0
    private var position = -1
    private var like = false //state of like button
    private var likeStateChange = false //since we make multiple calls we set like once
    private lateinit var details: ImagePojo
    private lateinit var model: UnsplashModel
    private lateinit var exifSheet: ModalSheetExif
    private lateinit var loginSheet: ModalSheetUnsplash

    // On create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        model = UnsplashModel(lifecycle)
        exifSheet = ModalSheetExif()
        loginSheet = ModalSheetUnsplash()

        // checking to handle the app links
        if (intent.hasExtra(C.IMAGE_POJO)) {
            var params = intent.extras
            details = Gson().fromJson(params.getString(C.IMAGE_POJO), ImagePojo::class.java)

            getImageDetails(details.id)
            setImageDetails(details)
        } else {
            val appLinkData = intent.data
            getImageDetails(appLinkData.lastPathSegment)
        }

        position = intent.getIntExtra(C.POSITION, -1)

        imagePreviewWallpaper.setOnClickListener(this)
        imagePreviewDownload.setOnClickListener(this)
        imagePreviewAuthorL.setOnClickListener(this)
        imagePreviewExif.setOnClickListener(this)
        imagePreviewShare.setOnClickListener(this)
        imagePreviewStats.setOnClickListener(this)
        imagePreviewUnsplash.setOnClickListener(this)
        imagePreviewFab.setOnClickListener(this)

        //Ripple.add(Colors(this).GREY_400, imagePreviewStats)

    }

    // On click for various buttons
    override fun onClick(v: View) {
        when (v.id) {
            imagePreviewWallpaper.id -> {
                imagePreviewProgress.visibility = View.VISIBLE
                Permissions.askWriteExternalStoragePermission(this) { no, _ ->
                    if (no != null) {
                        Toast.short(this@ImageActivity, "Kindly provide external storage permission in Settings")
                        imagePreviewProgress.visibility = View.GONE
                    } else {
                        if (bitmap != null) {
                            Config.imageBitmap = bitmap!!
                            WallpaperHandler.setHomescreenWallpaper(this@ImageActivity)
                            imagePreviewProgress.visibility = View.GONE
                        } else {
                            toast("Waiting for High Quality Image ...")
                            ImageHandler.getImageAsBitmap(lifecycle, this, details.urls!!.full) {
                                bitmap = it
                                Config.imageBitmap = bitmap!!
                                WallpaperHandler.setHomescreenWallpaper(this@ImageActivity)
                                imagePreviewProgress.visibility = View.GONE
                            }
                        }
                        model.downloadedPhoto(details.links!!.download_location)
                    }
                }
            }
            imagePreviewDownload.id -> {
                Permissions.askWriteExternalStoragePermission(this) { no, _ ->
                    if (no != null)
                        Toast.short(this@ImageActivity, "Kindly provide external storage permission in Settings")
                    else {
                        DownloadHandler.downloadData(this, details.links!!.download, details.id)
                        Toast.short(this, "Downloading Image in /Downloads/Wallup/${details.id}.jpg .Check notification for progress.")
                        model.downloadedPhoto(details.links!!.download_location)
                    }
                }
            }
            imagePreviewAuthorL.id -> {
                var intent = Intent(this, ArtistProfileActivity::class.java)
                intent.putExtra(C.USERNAME, details.user!!.username)
                startActivity(intent)
            }
            imagePreviewExif.id -> {
                var bundle = bundleOf(Pair(C.IMAGE_POJO, Gson().toJson(details)))
                if (color != 0) bundle.putInt(C.COLOR, color)

                exifSheet.arguments = bundle
                exifSheet.show(supportFragmentManager, exifSheet.tag)
            }
            imagePreviewShare.id -> {
                imagePreviewProgress.visibility = View.VISIBLE
                Permissions.askWriteExternalStoragePermission(this) { no, _ ->
                    if (no != null) {
                        Toast.short(this@ImageActivity, "Kindly provide external storage permission in Settings")
                        imagePreviewProgress.visibility = View.GONE
                    } else {
                        if (bitmap != null) {
                            ImageHandler.shareImage(this, bitmap!!, details.id, F.unsplashImage(details.id))
                            imagePreviewProgress.visibility = View.GONE
                        } else {
                            toast("Waiting for High Quality Image ...")
                            ImageHandler.getImageAsBitmap(lifecycle, this, details.urls!!.full) {
                                bitmap = it
                                ImageHandler.shareImage(this, it, details.id, F.unsplashImage(details.id))
                                imagePreviewProgress.visibility = View.GONE
                            }
                        }
                        model.downloadedPhoto(details.links!!.download_location)
                    }
                }
            }
            imagePreviewStats.id -> toast("Upcoming feature")
            imagePreviewUnsplash.id -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(F.unsplashImage(details.id))))
            imagePreviewFab.id -> {
                if (Config.USER_API_KEY.isNotEmpty()) {
                    details?.let {
                        like = !like
                        F.like(this, imagePreviewFabI, it.id, like, true)
                        if (like) {
                            imagePreviewLikesCount.text = (it.likes + 1).toString()
                            details.likes = details.likes + 1
                        } else {
                            imagePreviewLikesCount.text = (it.likes - 1).toString()
                            details.likes = details.likes - 1
                        }

                        var obj = JSONObject()
                        obj.put(C.TYPE, C.LIKE)
                        obj.put(C.LIKE, like)
                        obj.put(C.ID, details.id)
                        EventBus.getDefault().postSticky(Event(obj))
                    }
                } else
                    loginSheet.show(supportFragmentManager, loginSheet.tag)
            }
        }
    }

    // On back pressed
    override fun onBackPressed() {
        finish()
    }

    // get image details
    private fun getImageDetails(id: String) {
        model.getImage(id) { _, details ->
            details?.let {
                this.details = it as ImagePojo
                fullDetails = it
                setImageDetails(fullDetails!!)
            }

        }
    }

    // set image details on views
    private fun setImageDetails(details: ImagePojo) {
        imagePreviewAuthorName.text = details.user!!.name
        imagePreviewLikesCount.text = F.withSuffix(details.likes)
        imagePreviewAuthorImages.text = F.withSuffix(details.user!!.total_photos)
        imagePreviewAuthorCollections.text = F.withSuffix(details.user!!.total_collections)
        imagePreviewViewsCount.text = F.withSuffix(details.views)
        imagePreviewDownloadCount.text = F.withSuffix(details.downloads)
        imagePreviewPublishedOn.text = "Published on ${DateHandler.convertForImagePreview(details.created_at)}"

        ImageHandler.setImageInView(lifecycle, imagePreviewAuthorImage, details.user!!.profile_image!!.large)
        ImageHandler.getImageAsBitmap(lifecycle, this, details.urls!!.full + Config.IMAGE_HEIGHT) {
            color = ColorHandler.getNonDarkColor(Palette.from(it).generate(), this)
            color()
            movingImage.setImageBitmap(it)
            imagePreviewProgress.visibility = View.GONE
            /*ML.labels(it) {
                setTags(it)
            }*/
        }
        //F.underline(imagePreviewStatistics)

        if (details.downloads == 0) {
            imagePreviewDownloadCount.visibility = View.GONE
        } else
            imagePreviewDownloadCount.visibility = View.VISIBLE

        if (details.liked_by_user && !likeStateChange)
            F.like(this, imagePreviewFabI, true, true)

        if (!likeStateChange)
            like = details.liked_by_user

        likeStateChange = true
    }

    // set color on a resources
    private fun color() {
        var down = imagePreviewDownload.background.current as GradientDrawable
        var wall = imagePreviewWallpaper.background.current as GradientDrawable
        var fab = imagePreviewFab.background.current as GradientDrawable

        imagePreviewAuthorImagesL.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        imagePreviewAuthorCollectionsL.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        imagePreviewShareI.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        imagePreviewViewsI.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        imagePreviewExifI.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        imagePreviewStatsI.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        imagePreviewUnsplashI.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        //imagePreviewLikeI.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)

        down.setColor(color)
        fab.setColor(color)
        wall.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        //imagePreviewWallpaperT.setTextColor(color)
    }

    /* // set tags
     private fun setTags(tags: List<FirebaseVisionLabel>) {
         imagePreviewTags.visibility = View.VISIBLE
         val sortedTags = F.sortLabels(tags)
         imagePreviewTags.clipToPadding = false
         imagePreviewTags.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
         imagePreviewTags.adapter = TagsAdapter(lifecycle, sortedTags)
     }*/
}
