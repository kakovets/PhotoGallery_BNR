package com.kakovets.photogallery

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

private const val TAG = "PhotoGalleryFragment"
class PhotoGalleryFragment: Fragment() {

    private lateinit var photoGalleryViewModel: PhotoGalleryViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var thumbnailDownloader: ThumbnailDownloader<PhotoHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photoGalleryViewModel = ViewModelProvider(this)[PhotoGalleryViewModel::class.java]

//        retainInstance = true

        thumbnailDownloader = ThumbnailDownloader()
        lifecycle.addObserver(thumbnailDownloader)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photo_gallery, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoGalleryViewModel.galleryItemViewModel.observe(viewLifecycleOwner) { galleryItems ->
            Log.d(TAG, "Have gallery items from ViewModel $galleryItems")
            recyclerView.adapter = PhotoAdapter(galleryItems)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(thumbnailDownloader)
    }

    private inner class PhotoAdapter(private val galleryItems: List<GalleryItem>): RecyclerView.Adapter<PhotoHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
            val imageView = layoutInflater.inflate(
                R.layout.list_item_gallery,
                parent,
                false) as ImageView
            return PhotoHolder(imageView)
        }

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem = galleryItems[position]
            val placeholder: Drawable = ContextCompat
                .getDrawable(requireContext(), R.drawable.bill_up_close) ?:
                ColorDrawable()
            holder.bindDrawable(placeholder)

            thumbnailDownloader.queueThumbNail(holder, galleryItem.url)
        }

        override fun getItemCount() = galleryItems.size
    }

    private class PhotoHolder(itemTextView: ImageView): ViewHolder(itemTextView) {
        val bindDrawable: (Drawable) -> Unit = itemTextView::setImageDrawable
    }

    companion object {
        fun newInstance() : PhotoGalleryFragment {
            return PhotoGalleryFragment()
        }
    }
}