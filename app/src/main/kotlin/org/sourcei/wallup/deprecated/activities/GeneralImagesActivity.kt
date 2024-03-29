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
package org.sourcei.wallup.deprecated.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_general_images.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.sourcei.wallup.deprecated.R
import org.sourcei.wallup.deprecated.utils.L
import org.sourcei.wallup.deprecated.utils.toast

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09 by Saksham
 *
 * @note Updates :
 * Saksham - 2018 09 09 - master - collection images
 * Saksham - 2018 09 22 - master - random images tag
 * Saksham - 2018 11 28 - master - connection handling
 * Saksham - 2018 12 04 - master - new reload / progress
 * Saksham - 2018 12 21 - master - fix issue with random adapter collection add
 */
class GeneralImagesActivity : AppCompatActivity(), View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, org.sourcei.wallup.deprecated.interfaces.OnLoadMoreListener {

    private val NAME = "GeneralImagesActivity"
    private var current: Int = 0
    private var nextPage = 2
    private var randomImages = false //if random images are set
    private lateinit var model: org.sourcei.wallup.deprecated.models.UnsplashModel
    private lateinit var mainAdapter: org.sourcei.wallup.deprecated.adapters.MainAdapter
    private lateinit var username: String
    private lateinit var images: MutableList<org.sourcei.wallup.deprecated.pojo.ImagePojo?>
    private lateinit var colId: String
    private lateinit var colType: String
    private lateinit var tag: String
    private lateinit var type: String

    // on create
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_images)

        model = org.sourcei.wallup.deprecated.models.UnsplashModel(lifecycle)
        type = intent.extras.getString(org.sourcei.wallup.deprecated.utils.C.TYPE)
        when (type) {
            org.sourcei.wallup.deprecated.utils.C.RANDOM -> randomImages()
            org.sourcei.wallup.deprecated.utils.C.ARTIST_IMAGES -> {
                current = 1
                username = intent.extras.getString(org.sourcei.wallup.deprecated.utils.C.USERNAME)
                paginatedImages()
            }
            org.sourcei.wallup.deprecated.utils.C.COLLECTION -> {
                current = 2
                colId = intent.extras.getString(org.sourcei.wallup.deprecated.utils.C.ID)
                colType = intent.extras.getString(org.sourcei.wallup.deprecated.utils.C.COLLECTION)
                paginatedImages()
            }
            org.sourcei.wallup.deprecated.utils.C.TAG -> {
                current = 3
                tag = intent.extras.getString(org.sourcei.wallup.deprecated.utils.C.TAG)
                randomImages()
            }
            org.sourcei.wallup.deprecated.utils.C.LIKE -> {
                current = 4
                username = intent.extras.getString(org.sourcei.wallup.deprecated.utils.C.USERNAME)
                L.d(NAME, username)
                paginatedImages()
                generalImagesFab.visibility = View.GONE
            }
        }

        generalImagesFab.setOnClickListener(this)
        generalImagesSwipe.setOnRefreshListener(this)
        generalBack.setOnClickListener(this)
        generalImagesReload.setOnClickListener(this)

        generalImagesProgressI.animation = AnimationUtils.loadAnimation(this, R.anim.rotation_progress)
    }

    // on start
    override fun onStart() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
        super.onStart()
    }

    // on destroy
    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    // on click
    override fun onClick(v: View) {
        when (v.id) {
            generalImagesFab.id -> {
                generalImagesFab.isVisible = false
                randomImages()
            }
            generalBack.id -> finish()
            generalImagesReload.id -> {
                generalImagesReload.visibility = View.GONE
                generalImagesProgress.visibility = View.VISIBLE
                when (type) {
                    org.sourcei.wallup.deprecated.utils.C.RANDOM, org.sourcei.wallup.deprecated.utils.C.TAG -> randomImages()
                    org.sourcei.wallup.deprecated.utils.C.ARTIST_IMAGES, org.sourcei.wallup.deprecated.utils.C.COLLECTION, org.sourcei.wallup.deprecated.utils.C.LIKE -> paginatedImages()
                }
            }
        }
    }

    // on swipe refresh
    override fun onRefresh() {
        when (current) {
            0 -> randomImages()
            1 -> paginatedImages()
            2 -> paginatedImages()
            3 -> randomImages()
            4 -> paginatedImages()
        }
    }

    // on load more
    override fun onLoadMore() {
        images.add(null)
        mainAdapter.notifyItemInserted(images.size)
        when (current) {
            1 -> model.userPhotos(nextPage, 30, username, callbackPaginated)
            2 -> {
                if (colType == org.sourcei.wallup.deprecated.utils.C.FEATURED)
                    model.collectionPhotos(colId, nextPage, 30, callbackPaginated)
                else
                    model.curatedCollectionPhotos(colId, nextPage, 30, callbackPaginated)
            }
            4 -> model.userLikedPhotos(username, nextPage, callbackPaginated)
        }
    }

    // back press
    override fun onBackPressed() {
        finish()
    }

    // on message event
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: org.sourcei.wallup.deprecated.utils.Event) {
        if (event.obj.has(org.sourcei.wallup.deprecated.utils.C.TYPE)) {
            if (event.obj.getString(org.sourcei.wallup.deprecated.utils.C.TYPE) == org.sourcei.wallup.deprecated.utils.C.LIKE) {
                // get id of the image
                val id = event.obj.getString(org.sourcei.wallup.deprecated.utils.C.ID)
                // get position array for the image
                var position = images.asSequence().withIndex().filter { it.value!!.id == id }.map { it.index }.toList()
                // if position found
                if (position.isNotEmpty()) {
                    // change like state in images array
                    for (pos in position) {
                        L.d(NAME, pos)
                        images[pos]!!.liked_by_user = event.obj.getBoolean(org.sourcei.wallup.deprecated.utils.C.LIKE)
                        mainAdapter.notifyItemChanged(pos)
                    }
                }
            }
            if (event.obj.getString(org.sourcei.wallup.deprecated.utils.C.TYPE) == org.sourcei.wallup.deprecated.utils.C.IMAGE_TO_COLLECTION) {
                // if image is added to a collection
                if (event.obj.getBoolean(org.sourcei.wallup.deprecated.utils.C.IS_ADDED)) {
                    var col = Gson().fromJson(event.obj.getString(org.sourcei.wallup.deprecated.utils.C.COLLECTION), org.sourcei.wallup.deprecated.pojo.CollectionPojo::class.java)
                    var list = images
                            .asSequence()
                            .withIndex()
                            .filter { it.value!!.id == event.obj.getString(org.sourcei.wallup.deprecated.utils.C.IMAGE) }
                            .map { it.index }
                            .toList()

                    if (list.isNotEmpty()) {
                        for (l in list) {
                            if (images[l]!!.current_user_collections == null)
                                images[l]!!.current_user_collections = arrayListOf()
                            images[l]!!.current_user_collections!!.add(col)
                            mainAdapter.notifyItemChanged(l)
                        }
                    }
                } else {
                    //if image is removed from collection
                    var list = images
                            .asSequence()
                            .withIndex()
                            .filter { it.value!!.id == event.obj.getString(org.sourcei.wallup.deprecated.utils.C.IMAGE) }
                            .map { it.index }
                            .toList()

                    if (list.isNotEmpty()) {
                        for (l in list) {
                            var cid = images[l]!!.current_user_collections!!
                                    .asSequence()
                                    .withIndex()
                                    .filter { it.value.id == event.obj.getString(org.sourcei.wallup.deprecated.utils.C.COLLECTION_ID) }
                                    .map { it.index }
                                    .toList()
                            images[l]!!.current_user_collections!!.removeAt(cid[0])
                            mainAdapter.notifyItemChanged(l)
                        }
                    }
                }
            }
            if (event.obj.getString(org.sourcei.wallup.deprecated.utils.C.TYPE) == org.sourcei.wallup.deprecated.utils.C.NETWORK) {
                runOnUiThread {
                    if (event.obj.getBoolean(org.sourcei.wallup.deprecated.utils.C.NETWORK)) {
                        generalConnLayout.setBackgroundColor(org.sourcei.wallup.deprecated.utils.Colors(this).GREEN)
                        generalConnText.text = "Back Online"
                        GlobalScope.launch {
                            delay(1500)
                            runOnUiThread {
                                generalConnLayout.visibility = View.GONE
                            }
                        }
                    } else {
                        generalConnLayout.visibility = View.VISIBLE
                        generalConnLayout.setBackgroundColor(org.sourcei.wallup.deprecated.utils.Colors(this).LIKE)
                        generalConnText.text = "No Internet"
                    }
                }
            }
        }
    }

    // random images
    private fun randomImages() {
        randomImages = true
        generalImagesProgress.visibility = View.VISIBLE
        generalImagesSwipe.visibility = View.GONE

        when (current) {
            0 -> {
                model.randomImages(callbackR)
            }
            1 -> {
                toast("shuffling images")
                model.randomUserImages(username, callbackR)
            }
            2 -> {
                toast("shuffling images")
                if (colType == org.sourcei.wallup.deprecated.utils.C.FEATURED)
                    model.randomCollectionPhotos(colId, callbackR)
                else // we cant get curated random images hence shuffling normal ones
                    model.randomImages(callbackR)
            }
            3 -> {
                model.randomImagesTag(tag, callbackR)
            }
        }
    }

    // paginated images
    private fun paginatedImages() {
        when (current) {
            1 -> {
                model.userPhotos(1, 30, username, callback)
            }
            2 -> {
                if (colType == org.sourcei.wallup.deprecated.utils.C.FEATURED)
                    model.collectionPhotos(colId, 1, 30, callback)
                else
                    model.curatedCollectionPhotos(colId, 1, 30, callback)
            }
            4 -> {
                model.userLikedPhotos(username, 1, callback)
            }
        }
    }

    // set random images in adapter
    private var callbackR = object : (Any?, Any?) -> Unit {
        override fun invoke(e: Any?, r: Any?) {
            L.d(NAME, "called")
            e?.let {
                L.d(NAME, e)
                generalImagesSwipe.isRefreshing = false
                generalImagesProgress.visibility = View.GONE
                generalImagesReload.visibility = View.VISIBLE
                toast("Error while fetching random images")
            }
            r?.let {
                when (type) {
                    org.sourcei.wallup.deprecated.utils.C.RANDOM, org.sourcei.wallup.deprecated.utils.C.ARTIST_IMAGES, org.sourcei.wallup.deprecated.utils.C.COLLECTION, org.sourcei.wallup.deprecated.utils.C.TAG -> generalImagesFab.isVisible = true
                }
                images = (r as List<org.sourcei.wallup.deprecated.pojo.ImagePojo?>).toMutableList()
                generalImagesProgress.visibility = View.GONE
                generalImagesSwipe.visibility = View.VISIBLE
                generalImagesReload.visibility = View.GONE

                mainAdapter = org.sourcei.wallup.deprecated.adapters.MainAdapter(lifecycle, images, generalImagesRecycler)
                generalImagesSwipe.isRefreshing = false
                generalImagesRecycler.layoutManager = LinearLayoutManager(this@GeneralImagesActivity)
                generalImagesRecycler.adapter = mainAdapter
                (generalImagesRecycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            }
        }
    }

    // callback for setting images in adapter
    private var callback = object : (Any?, Any?) -> Unit {
        override fun invoke(error: Any?, response: Any?) {
            randomImages = false
            error?.let {
                L.d(NAME, error)
                generalImagesProgress.visibility = View.GONE
                generalImagesSwipe.isRefreshing = false
                generalImagesReload.visibility = View.VISIBLE
                toast("error fetching images")
            }
            response?.let {
                when (type) {
                    org.sourcei.wallup.deprecated.utils.C.RANDOM, org.sourcei.wallup.deprecated.utils.C.ARTIST_IMAGES, org.sourcei.wallup.deprecated.utils.C.COLLECTION, org.sourcei.wallup.deprecated.utils.C.TAG -> generalImagesFab.isVisible = true
                }
                if ((response as List<org.sourcei.wallup.deprecated.pojo.ImagePojo>).size < 30) {
                    randomImages = true
                    callbackR(error, response)
                } else {
                    images = response.toMutableList()
                    mainAdapter = org.sourcei.wallup.deprecated.adapters.MainAdapter(lifecycle, images, generalImagesRecycler)
                    generalImagesRecycler.layoutManager = LinearLayoutManager(this@GeneralImagesActivity)
                    generalImagesRecycler.adapter = mainAdapter

                    generalImagesSwipe.isRefreshing = false
                    generalImagesSwipe.visibility = View.VISIBLE
                    generalImagesProgress.visibility = View.GONE
                    generalImagesReload.visibility = View.GONE
                    (generalImagesRecycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

                    mainAdapter.setOnLoadMoreListener(this@GeneralImagesActivity)
                }
            }
        }
    }

    // callback for setting images in adapter
    private var callbackPaginated = object : (Any?, Any?) -> Unit {
        override fun invoke(error: Any?, response: Any?) {
            error?.let {
                L.d(NAME, error)
                toast("Error loading images")
            }
            response?.let {
                nextPage++
                images.removeAt(images.size - 1)
                mainAdapter.notifyItemRemoved(images.size - 1)
                images.addAll(response as List<org.sourcei.wallup.deprecated.pojo.ImagePojo>)
                mainAdapter.notifyDataSetChanged()
                mainAdapter.setLoaded()
            }
        }
    }
}
