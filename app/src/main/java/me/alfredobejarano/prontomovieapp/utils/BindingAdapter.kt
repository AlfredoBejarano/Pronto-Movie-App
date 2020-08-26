package me.alfredobejarano.prontomovieapp.utils

import androidx.databinding.BindingAdapter
import com.facebook.drawee.view.SimpleDraweeView

/**
 * BindingAdapter
 *
 * @author (c) AlfredoBejarano - alfredo.corona@rappi.com
 */
class BindingAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter("image_url")
        fun setImageUrl(view: SimpleDraweeView, url: String) = view.setImageURI(url)
    }
}