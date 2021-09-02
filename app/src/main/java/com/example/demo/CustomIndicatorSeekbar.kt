package com.example.demo

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.indicator_progressbar_layout.view.*

class CustomIndicatorProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.indicator_progressbar_layout, this)
    }

    fun setProgress(progress: Int) {
        val params = progress2.layoutParams
        params.width = progress
        progress2.layoutParams = params
    }

    fun moveThumbTo(dx: Int) {
        thumb.translationX = dx.toFloat()
    }

}