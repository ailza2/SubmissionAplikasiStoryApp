package com.dicoding.submissionaplikasistoryapp.costumui

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.dicoding.submissionaplikasistoryapp.R


class Button : AppCompatButton {

    private lateinit var enableBackground: Drawable
    private lateinit var disableBackground: Drawable
    private var txtColor : Int = 0
    constructor(context: Context):super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet):super(context,attrs){
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr:Int):super(context, attrs, defStyleAttr){
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isEnabled) enableBackground else disableBackground

        setTextColor(txtColor)
        textSize = 14f
        gravity = Gravity.CENTER
        text
    }

    private fun init(){
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enableBackground = ContextCompat.getDrawable(context, R.drawable.custom_button_round) as Drawable
        disableBackground = ContextCompat.getDrawable(context, R.drawable.custom_button_disable) as Drawable
    }

}