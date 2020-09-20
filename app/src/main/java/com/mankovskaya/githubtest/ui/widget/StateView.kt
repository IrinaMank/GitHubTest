package com.mankovskaya.githubtest.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import com.mankovskaya.githubtest.databinding.ViewStateBinding

class StateView : FrameLayout {

    constructor(
        context: Context
    ) : super(context)

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)


    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding: ViewStateBinding =
        ViewStateBinding.inflate(LayoutInflater.from(context), this, true)

    fun setViewModel(viewModel: StateViewModel) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.context as? LifecycleOwner
        binding.retryButton.setOnClickListener { viewModel.reactOnAction(StateAction.ButtonClicked) }
    }
}