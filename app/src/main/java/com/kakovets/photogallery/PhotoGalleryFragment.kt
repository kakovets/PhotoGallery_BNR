package com.kakovets.photogallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

private const val TAG = "PhotoGalleryFragment"
class PhotoGalleryFragment: Fragment() {

    private lateinit var photoGalleryViewModel: PhotoGalleryViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photoGalleryViewModel = ViewModelProvider(this)[PhotoGalleryViewModel::class.java]
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

    private class PhotoHolder(itemTextView: TextView): ViewHolder(itemTextView) {

        val bindTitle: (CharSequence) -> Unit = itemTextView::setText

    }

    private class PhotoAdapter(private val galleryItems: List<GalleryItem>): RecyclerView.Adapter<PhotoHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
            val textView = TextView(parent.context)
            return PhotoHolder(textView)
        }

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem = galleryItems[position]
            holder.bindTitle(galleryItem.title)
        }

        override fun getItemCount() = galleryItems.size
    }


    companion object {
        fun newInstance() : PhotoGalleryFragment {
            return PhotoGalleryFragment()
        }
    }
}