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
package org.sourcei.wallup.deprecated.repositories

import org.json.JSONObject
import org.sourcei.wallup.deprecated.BuildConfig
import org.sourcei.wallup.deprecated.utils.C
import org.sourcei.wallup.deprecated.utils.Config
import org.sourcei.wallup.deprecated.utils.ErrorUtils
import org.sourcei.wallup.deprecated.utils.L
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-05-20 by Saksham
 *
 * @note Updates :
 *  2018 08 03 - recent - Saksham - downloaded a photo
 *  2018 08 31 - master - Saksham - user details
 *  2018 09 01 - master - Saksham - image details
 *  2018 09 02 - master - Saksham - random user images
 *  2018 09 08 - master - Saksham - featured collections
 *  2018 09 14 - master - Saksham - user's collections
 *  2018 09 22 - master - Saksham - random images tag
 *  2018 10 01 - master - Saksham - generate bearer token
 *  2018 10 04 - master - Saksham - user profile
 *  2018 10 08 - master - Saksham - user likes
 *  2018 10 21 - master - Saksham - add image in user collection
 *  2018 12 09 - master - Saksham - image statistics
 *  2018 12 11 - master - Saksham - new collection
 *  2018 12 19 - master - Saksham - delete collection
 */
object UnsplashRepository {
    private val NAME = "UnsplashRepository"

    // Get latest photos
    fun getLatestPhotos(page: Int, callback: (Any?, Any?) -> Unit) {

        L.d(NAME, Config.apiKey())
        L.d(NAME, BuildConfig.UNSPLASH_API_KEY)

        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.getLatestPhotos(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                page)

        call.enqueue(object : Callback<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>> {

            override fun onResponse(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, response: Response<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // Get popular photos
    fun getPopularPhotos(page: Int, callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.getPopularPhotos(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                page)

        call.enqueue(object : Callback<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>> {

            override fun onResponse(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, response: Response<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // Get curated photos
    fun getCuratedPhotos(page: Int, callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.getCuratedPhotos(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                page)

        call.enqueue(object : Callback<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>> {

            override fun onResponse(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, response: Response<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // Downloaded a photo
    fun downloadedPhoto(url: String) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.imageDownloaded(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                url)

        call.enqueue(object : Callback<JSONObject> {

            override fun onResponse(call: Call<JSONObject>?, response: Response<JSONObject>) {
                L.d(org.sourcei.wallup.deprecated.repositories.UnsplashRepository.NAME, "File downloaded linked")
            }

            override fun onFailure(call: Call<JSONObject>?, t: Throwable?) {
                L.e(org.sourcei.wallup.deprecated.repositories.UnsplashRepository.NAME, t.toString())
            }
        })
    }

    // User Details
    fun userDetails(username: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.userDetails(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                username)

        call.enqueue(object : Callback<org.sourcei.wallup.deprecated.pojo.UserPojo> {

            override fun onResponse(call: Call<org.sourcei.wallup.deprecated.pojo.UserPojo>?, response: Response<org.sourcei.wallup.deprecated.pojo.UserPojo>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<org.sourcei.wallup.deprecated.pojo.UserPojo>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // Get user photos
    fun userPhotos(page: Int, count: Int, username: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.userPhotos(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                username,
                page,
                count)

        call.enqueue(object : Callback<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>> {

            override fun onResponse(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, response: Response<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // Get image details
    fun getImage(id: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.getImage(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                id
        )

        call.enqueue(object : Callback<org.sourcei.wallup.deprecated.pojo.ImagePojo> {

            override fun onResponse(call: Call<org.sourcei.wallup.deprecated.pojo.ImagePojo>?, response: Response<org.sourcei.wallup.deprecated.pojo.ImagePojo>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<org.sourcei.wallup.deprecated.pojo.ImagePojo>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // Get random images
    fun randomImages(callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.randomImages(
                org.sourcei.wallup.deprecated.utils.Config.apiKey()
        )

        call.enqueue(object : Callback<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>> {

            override fun onResponse(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, response: Response<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // Get random user images
    fun randomUserImages(username: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.randomUserImages(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                username
        )

        call.enqueue(object : Callback<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>> {

            override fun onResponse(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, response: Response<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // curated curatedCollections
    fun curatedCollections(page: Int, callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.curatedCollections(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                page
        )

        call.enqueue(object : Callback<List<org.sourcei.wallup.deprecated.pojo.CollectionPojo>> {

            override fun onResponse(call: Call<List<org.sourcei.wallup.deprecated.pojo.CollectionPojo>>?, response: Response<List<org.sourcei.wallup.deprecated.pojo.CollectionPojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<org.sourcei.wallup.deprecated.pojo.CollectionPojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // featured curatedCollections
    fun featuredCollections(page: Int, callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.featuredCollections(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                page
        )

        call.enqueue(object : Callback<List<org.sourcei.wallup.deprecated.pojo.CollectionPojo>> {

            override fun onResponse(call: Call<List<org.sourcei.wallup.deprecated.pojo.CollectionPojo>>?, response: Response<List<org.sourcei.wallup.deprecated.pojo.CollectionPojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<org.sourcei.wallup.deprecated.pojo.CollectionPojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // collection photos
    fun collectionPhotos(id: String, page: Int, count: Int, callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.collectionPhotos(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                id,
                count,
                page
        )

        call.enqueue(object : Callback<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>> {

            override fun onResponse(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, response: Response<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // curated collection photos
    fun curatedCollectionPhotos(id: String, page: Int, count: Int, callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.curatedCollectionPhotos(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                id,
                count,
                page
        )

        call.enqueue(object : Callback<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>> {

            override fun onResponse(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, response: Response<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // random collection photos
    fun randomCollectionPhotos(id: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.randomCollectionPhotos(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                id
        )

        call.enqueue(object : Callback<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>> {

            override fun onResponse(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, response: Response<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // user's collections
    fun userCollections(username: String, page: Int, count: Int, callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.getUserCollections(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                username,
                page,
                count
        )

        call.enqueue(object : Callback<List<org.sourcei.wallup.deprecated.pojo.CollectionPojo>> {

            override fun onResponse(call: Call<List<org.sourcei.wallup.deprecated.pojo.CollectionPojo>>?, response: Response<List<org.sourcei.wallup.deprecated.pojo.CollectionPojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<org.sourcei.wallup.deprecated.pojo.CollectionPojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // random images with a tag
    fun randomImagesTag(keyword: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.randomImagesTag(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                keyword
        )

        call.enqueue(object : Callback<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>> {

            override fun onResponse(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, response: Response<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // like a photo
    fun likePhoto(id: String) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.likeImage(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                id
        )

        call.enqueue(object : Callback<org.sourcei.wallup.deprecated.pojo.ImagePojo> {

            override fun onResponse(call: Call<org.sourcei.wallup.deprecated.pojo.ImagePojo>?, response: Response<org.sourcei.wallup.deprecated.pojo.ImagePojo>) {
                if (!response.isSuccessful)
                    L.dO(org.sourcei.wallup.deprecated.repositories.UnsplashRepository.NAME, org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response))
                else
                    L.d(org.sourcei.wallup.deprecated.repositories.UnsplashRepository.NAME, "Liked Photo")
            }

            override fun onFailure(call: Call<org.sourcei.wallup.deprecated.pojo.ImagePojo>?, t: Throwable?) {
                t?.toString()?.let { L.d(org.sourcei.wallup.deprecated.repositories.UnsplashRepository.NAME, it) }
            }
        })
    }

    // unlike a photo
    fun unlikePhoto(id: String) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.unlikeImage(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                id
        )

        call.enqueue(object : Callback<org.sourcei.wallup.deprecated.pojo.ImagePojo> {

            override fun onResponse(call: Call<org.sourcei.wallup.deprecated.pojo.ImagePojo>?, response: Response<org.sourcei.wallup.deprecated.pojo.ImagePojo>) {
                if (!response.isSuccessful)
                    L.dO(org.sourcei.wallup.deprecated.repositories.UnsplashRepository.NAME, org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response))
                else
                    L.d(org.sourcei.wallup.deprecated.repositories.UnsplashRepository.NAME, "Unliked Photo")
            }

            override fun onFailure(call: Call<org.sourcei.wallup.deprecated.pojo.ImagePojo>?, t: Throwable?) {
                t?.toString()?.let { L.d(org.sourcei.wallup.deprecated.repositories.UnsplashRepository.NAME, it) }
            }
        })
    }

    // generate bearer token
    fun bearerToken(body: org.sourcei.wallup.deprecated.pojo.BearerBody, callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.bearerToken(
                C.UNSPLASH_TOKEN,
                body
        )

        call.enqueue(object : Callback<org.sourcei.wallup.deprecated.pojo.BearerToken> {

            override fun onResponse(call: Call<org.sourcei.wallup.deprecated.pojo.BearerToken>?, response: Response<org.sourcei.wallup.deprecated.pojo.BearerToken>) {
                if (response.isSuccessful) {
                    callback(null, response.body())
                } else {
                    val error = ErrorUtils.parseErrorAuth(response)
                    L.e(NAME, error)
                    callback(error, null)
                }
            }

            override fun onFailure(call: Call<org.sourcei.wallup.deprecated.pojo.BearerToken>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // user profile
    fun selfProfile(callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.selfProfile(
                org.sourcei.wallup.deprecated.utils.Config.apiKey()
        )

        call.enqueue(object : Callback<org.sourcei.wallup.deprecated.pojo.UserPojo> {

            override fun onResponse(call: Call<org.sourcei.wallup.deprecated.pojo.UserPojo>?, response: Response<org.sourcei.wallup.deprecated.pojo.UserPojo>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<org.sourcei.wallup.deprecated.pojo.UserPojo>?, t: Throwable?) {
                t?.toString()?.let { L.d(org.sourcei.wallup.deprecated.repositories.UnsplashRepository.NAME, it) }
            }
        })
    }

    // user liked photos
    fun userLikedPhotos(username: String, page: Int, callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.userLikedPhotos(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                username,
                page)

        call.enqueue(object : Callback<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>> {

            override fun onResponse(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, response: Response<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // add image in collection
    fun addImageInCollection(photo_id: String, collection_id: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.addImageInCollection(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                collection_id,
                photo_id
        )

        call.enqueue(object : Callback<org.sourcei.wallup.deprecated.pojo.ImagePojo> {

            override fun onResponse(call: Call<org.sourcei.wallup.deprecated.pojo.ImagePojo>?, response: Response<org.sourcei.wallup.deprecated.pojo.ImagePojo>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<org.sourcei.wallup.deprecated.pojo.ImagePojo>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // remove image in collection
    fun removeImageInCollection(photo_id: String, collection_id: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.removeImageInCollection(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                collection_id,
                photo_id
        )

        call.enqueue(object : Callback<org.sourcei.wallup.deprecated.pojo.ImagePojo> {

            override fun onResponse(call: Call<org.sourcei.wallup.deprecated.pojo.ImagePojo>?, response: Response<org.sourcei.wallup.deprecated.pojo.ImagePojo>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<org.sourcei.wallup.deprecated.pojo.ImagePojo>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // image stats
    fun imageStats(id: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.imageStats(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                id
        )

        call.enqueue(object : Callback<org.sourcei.wallup.deprecated.pojo.ImageStatsPojo> {

            override fun onResponse(call: Call<org.sourcei.wallup.deprecated.pojo.ImageStatsPojo>?, response: Response<org.sourcei.wallup.deprecated.pojo.ImageStatsPojo>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<org.sourcei.wallup.deprecated.pojo.ImageStatsPojo>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // new collection
    fun newCollection(body: org.sourcei.wallup.deprecated.pojo.NewCollections, callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.newCollection(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                body
        )

        call.enqueue(object : Callback<org.sourcei.wallup.deprecated.pojo.CollectionPojo> {

            override fun onResponse(call: Call<org.sourcei.wallup.deprecated.pojo.CollectionPojo>?, response: Response<org.sourcei.wallup.deprecated.pojo.CollectionPojo>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<org.sourcei.wallup.deprecated.pojo.CollectionPojo>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // delete collection
    fun deleteCollection(id: String) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        val call = apiClient.deleteCollection(
                org.sourcei.wallup.deprecated.utils.Config.apiKey(),
                id
        )

        call.enqueue(object : Callback<org.sourcei.wallup.deprecated.pojo.CollectionPojo> {

            override fun onResponse(call: Call<org.sourcei.wallup.deprecated.pojo.CollectionPojo>?, response: Response<org.sourcei.wallup.deprecated.pojo.CollectionPojo>) {
            }

            override fun onFailure(call: Call<org.sourcei.wallup.deprecated.pojo.CollectionPojo>?, t: Throwable?) {

            }
        })
    }
}

