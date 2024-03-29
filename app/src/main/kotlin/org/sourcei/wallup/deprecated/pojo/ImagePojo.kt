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
package org.sourcei.wallup.deprecated.pojo

import com.google.gson.annotations.SerializedName

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-05-13 by Saksham
 *
 * @note Updates :
 */
data class ImagePojo(
        @SerializedName("id") val id: String = "",
        @SerializedName("created_at") val created_at: String = "",
        @SerializedName("width") val width: Int = 0,
        @SerializedName("height") val height: Int = 0,
        @SerializedName("color") val color: String? = "",
        @SerializedName("urls") val urls: org.sourcei.wallup.deprecated.pojo.Urls? = null,
        @SerializedName("links") val links: org.sourcei.wallup.deprecated.pojo.Links? = null,
        @SerializedName("likes") var likes: Int = 0,
        @SerializedName("user") val user: org.sourcei.wallup.deprecated.pojo.UserPojoRefined? = null,
        @SerializedName("key") val key: String = "",
        @SerializedName("timestamp") val timestamp: Int = 0,
        @SerializedName("views") val views: Int = 0,
        @SerializedName("downloads") val downloads: Int = 0,
        @SerializedName("exif") val exif: org.sourcei.wallup.deprecated.pojo.Exif? = null,
        @SerializedName("liked_by_user") var liked_by_user: Boolean = false,
        @SerializedName("current_user_collections") var current_user_collections: MutableList<org.sourcei.wallup.deprecated.pojo.CollectionPojo>? = null
)

data class Urls(
        @SerializedName("raw") val raw: String = "",
        @SerializedName("full") val full: String = "",
        @SerializedName("small") val small: String = "",
        @SerializedName("thumb") val thumb: String = ""
)

data class Links(
        @SerializedName("self") val self: String = "",
        @SerializedName("html") val html: String = "",
        @SerializedName("download") val download: String = "",
        @SerializedName("download_location") val download_location: String = ""
)

data class Exif(
        @SerializedName("make") val make: String? = null,
        @SerializedName("model") val model: String? = null,
        @SerializedName("exposure_time") val exposure_time: String? = null,
        @SerializedName("focal_length") val focal_length: String? = null,
        @SerializedName("iso") val iso: Int? = null,
        @SerializedName("aperture") val aperture: String? = null
)