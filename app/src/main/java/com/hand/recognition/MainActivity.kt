package com.hand.recognition

import android.annotation.SuppressLint
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

    private val variants = arrayOf("hand", "finger")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etImageUrl.setOnEditorActionListener { _, action, _ ->
            if (action == EditorInfo.IME_ACTION_DONE) {
                Picasso.with(applicationContext)
                    .load(etImageUrl.text.toString())
                    .into(ivHolder)

                return@setOnEditorActionListener true
            }

            false
        }
    }

    @SuppressLint("DefaultLocale")
    fun generateLabels(v: View) {
        val image = FirebaseVisionImage.fromBitmap(
            (ivHolder.drawable as BitmapDrawable).bitmap
        )

        val detector = FirebaseVision.getInstance().cloudImageLabeler

        detector
            .processImage(image)
            .addOnCompleteListener { task ->
                val isPalm = task.result
                    ?.asSequence()
                    ?.map { it.confidence to it.text.toLowerCase() }
                    ?.filter { variants.contains(it.second) }
                    ?.filter { it.first >= 0.8 }
                    ?.any()

                runOnUiThread {
                    alert(isPalm.toString(), "Is it palm?").show()
                }

                detector.close()
            }
    }
}
