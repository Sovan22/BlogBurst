package com.demomiru.blogburst.util

import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton

fun rvTouchListener(next: ImageButton, prev: ImageButton ) : View.OnTouchListener{
    val listener = object : View.OnTouchListener {
        private var initialY = 0f

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialY = event.y
                    return false
                }

                MotionEvent.ACTION_UP -> {
                    val finalY = event.y
                    val deltaY = finalY - initialY

                    if (deltaY > 0) {
                        // Scroll down
                        next.visibility = View.VISIBLE
                        prev.visibility = View.VISIBLE
                    } else if (deltaY < 0) {
                        // Scroll up
                        next.visibility = View.GONE
                        prev.visibility = View.GONE
                    }
                    return false
                }
            }
            return false
        }

    }
    return listener
}