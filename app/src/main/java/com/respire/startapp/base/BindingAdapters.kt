package com.respire.startapp.base

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory

class ImageAdapters {
    companion object {
        @BindingAdapter("app:glideCircleImageUrl")
        @JvmStatic
        fun loadImageUrl(view: ImageView, url: String?) {
            url?.let {
                val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
                Glide.with(view)
                    .load(it)
                    .apply(RequestOptions().circleCrop())
                    .transition(DrawableTransitionOptions.withCrossFade(factory))
                    .placeholder(android.R.color.white)
                    .into(view)
            }
        }
    }
}