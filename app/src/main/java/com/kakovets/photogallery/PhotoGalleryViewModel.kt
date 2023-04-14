package com.kakovets.photogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class PhotoGalleryViewModel: ViewModel() {

    val galleryItemViewModel: LiveData<List<GalleryItem>>

    init {
        galleryItemViewModel = FlickrFetchr().fetchPhotos()
    }


}