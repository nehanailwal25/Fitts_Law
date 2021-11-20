package com.example.assignment1

import android.R
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.widget.AbsoluteLayout
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activi)

    }

  /*  fun showRandomButton(){
        val button = findViewById<View>(R.id.my_button) as Button
        val absParams = button.layoutParams as AbsoluteLayout.LayoutParams
        val displaymetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displaymetrics)
        val width = displaymetrics.widthPixels
        val height = displaymetrics.heightPixels


        val r = Random()

        absParams.x = r.nextInt(width)
        absParams.y = r.nextInt(height)
        button.layoutParams = absParams
    }

*/
    fun createButtons() {
        title = "Test 1"
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        for (i in 0..2) {
            val row = LinearLayout(this)
            row.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layout.gravity = Gravity.CENTER

            for (j in 0..3) {
                val btnTag = Button(this)
                btnTag.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
                btnTag.text = "Button " + (j + 1 + i * 4)
                btnTag.id = j + 1 + i * 4
                btnTag.setOnClickListener(View.OnClickListener {

                })

                row.addView(btnTag)
            }
            layout.addView(row)
        }
        setContentView(layout)
    }

//    fun setClickListners() {
//        val btnA1 = findViewById<Button>(R.id.btnA1)
//        btnA1.setOnClickListener(View.OnClickListener {
//
//        })
//    }
}