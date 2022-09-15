package com.malicankaya.drawingapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    private var drawingView: DrawingView? = null
    private var mImageButtonCurrentPaint: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val ibBrush: ImageButton = findViewById(R.id.ibBrush)
        val linearLayoutColors = findViewById<LinearLayout>(R.id.llBrushColor)

        mImageButtonCurrentPaint = linearLayoutColors[2] as ImageButton
        mImageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.pallet_selected)
        )

        drawingView = findViewById(R.id.drawingView)
        drawingView?.setSizeForBrush(10.toFloat())

        ibBrush.setOnClickListener {
            showSetBrushSizeChooserDialog()
        }
    }

    private fun showSetBrushSizeChooserDialog() {
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Brush size: ")

        val ibSmallBrush: ImageButton = brushDialog.findViewById(R.id.ibSmallBrush)
        ibSmallBrush.setOnClickListener {
            drawingView?.setSizeForBrush(5.toFloat())
            brushDialog.dismiss()
        }
        val ibMediumBrush: ImageButton = brushDialog.findViewById(R.id.ibMediumBrush)
        ibMediumBrush.setOnClickListener {
            drawingView?.setSizeForBrush(10.toFloat())
            brushDialog.dismiss()
        }
        val ibLargeBrush: ImageButton = brushDialog.findViewById(R.id.ibLargeBrush)
        ibLargeBrush.setOnClickListener {
            drawingView?.setSizeForBrush(20.toFloat())
            brushDialog.dismiss()
        }

        brushDialog.show()
    }

    fun paintClicked(view: View){
        if(view != mImageButtonCurrentPaint){
            val imageButton = view as ImageButton

            drawingView?.setColor(imageButton.tag.toString())

            imageButton.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.pallet_selected))

            mImageButtonCurrentPaint?.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.pallet_normal))
            mImageButtonCurrentPaint = view
        }
    }

}