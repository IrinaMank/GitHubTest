package com.mankovskaya.githubtest.ui.common

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("app:imgUrl")
fun setProfilePicture(imageView: CircleImageView, imgUrl: String) {
    Glide.with(imageView.context)
        .load(imgUrl)
        .dontAnimate()
        .into(imageView)
}

@BindingAdapter("app:errorText")
fun setErrorMessage(view: TextInputLayout, errorMessage: String?) {
    view.error = errorMessage
}