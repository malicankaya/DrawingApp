package com.malicankaya.drawingapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    private var drawingView: DrawingView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val ibBrush: ImageButton = findViewById(R.id.ibBrush)

        drawingView = findViewById(R.id.drawingView)
        drawingView?.setSizeForBrush(20.toFloat())

        ibBrush.setOnClickListener{
            showSetBrushSizeChooserDialog()
        }
    }

    fun showSetBrushSizeChooserDialog(){
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Brush size: ")

        val ibSmallBrush: ImageButton = brushDialog.findViewById(R.id.ibSmallBrush)
        ibSmallBrush.setOnClickListener{
            drawingView?.setSizeForBrush(5.toFloat())
            brushDialog.dismiss()
        }
        val ibMediumBrush: ImageButton = brushDialog.findViewById(R.id.ibMediumBrush)
        ibMediumBrush.setOnClickListener{
            drawingView?.setSizeForBrush(10.toFloat())
            brushDialog.dismiss()
        }
        val ibLargeBrush: ImageButton = brushDialog.findViewById(R.id.ibLargeBrush)
        ibLargeBrush.setOnClickListener{
            drawingView?.setSizeForBrush(20.toFloat())
            brushDialog.dismiss()
        }

        brushDialog.show()
    }
}