package com.kakovets.photogallery.api

import com.google.gson.annotations.SerializedName
import com.kakovets.photogallery.GalleryItem

class PhotoResponse {
    @SerializedName("photo")
    lateinit var galleryItems: List<GalleryItem>
}