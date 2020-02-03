package com.hand.recognition

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        image_url_field.setOnEditorActionListener { _, action, _ ->
            if (action == EditorInfo.IME_ACTION_DONE) {
                Picasso.with(applicationContext)
                    .load(image_url_field.text.toString())
                    .into(image_holder)

                return@setOnEditorActionListener true
            }

            false
        }
    }

    fun recognizeText(v: View) {
        val textImage = FirebaseVisionImage.fromBitmap(
            (image_holder.drawable as BitmapDrawable).bitmap
        )

        val detector = FirebaseVision.getInstance().cloudTextRecognizer

        detector
            .processImage(textImage)
            .addOnCompleteListener { task ->
                val result = StringBuilder()

                task.result?.textBlocks?.forEach {
                    result.append(it.text)
                    result.append("\n")
                }

                runOnUiThread {
                    alert(result.toString(), "Text").show()
                }

                detector.close()
            }
    }

    fun detectFaces(v: View) {
        // To do
    }

    fun generateLabels(v: View) {
        // To do
    }
}
