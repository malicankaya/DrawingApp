package com.malicankaya.drawingapp

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    private var drawingView: DrawingView? = null
    private var mImageButtonCurrentPaint: ImageButton? = null
    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK && result.data != null){
                val imageBackground: ImageView = findViewById(R.id.ivBackground)

                imageBackground.setImageURI(result.data?.data)
            }
        }
    private val requestPermissions: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val permission = it.key
                val isGranted = it.value

                if (isGranted) {
                    if (permission == Manifest.permission.READ_EXTERNAL_STORAGE) {
                        Toast.makeText(
                            this@MainActivity,
                            "Permission granted now you can read the storage files",
                            Toast.LENGTH_SHORT
                        ).show()
                        val pickIntent = Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        openGalleryLauncher.launch(pickIntent)

                    } else {
                        if (permission == Manifest.permission.READ_EXTERNAL_STORAGE) {
                            Toast.makeText(
                                this@MainActivity,
                                "Permission denied, you cannot read the storage files",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val ibBrush: ImageButton = findViewById(R.id.ibBrush)
        val linearLayoutColors = findViewById<LinearLayout>(R.id.llBrushColor)
        val ibGallery: ImageButton = findViewById(R.id.ibGallery)
        val ivUndo: ImageView = findViewById(R.id.ivUndo)
        val ivRedo: ImageView = findViewById(R.id.ivRedo)
        val ibSave: ImageView = findViewById(R.id.ibSave)

        mImageButtonCurrentPaint = linearLayoutColors[2] as ImageButton
        mImageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.pallet_selected)
        )

        drawingView = findViewById(R.id.drawingView)
        drawingView?.setSizeForBrush(10.toFloat())

        ibBrush.setOnClickListener {
            showSetBrushSizeChooserDialog()
        }

        ibGallery.setOnClickListener {
            requestStoragePermission()
        }

        ivUndo.setOnClickListener {
            drawingView?.onClickUndo()
        }

        ivRedo.setOnClickListener {
            drawingView?.onClickRedo()
        }

        ibSave.setOnClickListener {

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

    private fun requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )){
            showRationaleDialog("Drawing App", "Drawing App needs to access your external storage.")
        }else{
            requestPermissions.launch(arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ))
        }
    }

    private fun showRationaleDialog(title: String, message: String){
        val builder = AlertDialog.Builder(this)

        builder.setTitle(title)
        builder.setMessage(message)
        builder.setNegativeButton("Cancel"){ dialog, _->
            dialog.dismiss()
        }
        builder.setPositiveButton("OK"){ dialog, _->
            dialog.dismiss()
            requestPermissions.launch(arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE))
        }

        builder.create().show()
    }

    private fun getBitmapFromView(view: View): Bitmap{
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if(bgDrawable != null){
            bgDrawable.draw(canvas)
        }else{
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)

        return returnedBitmap
    }

}