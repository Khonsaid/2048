package uz.gita.latizx2048.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class MyTouchListener(context: Context) : View.OnTouchListener {
    private val gestureDetector = GestureDetector(context, MyGestureDetector())
    private var detectMotionDirectionListener: ((SideEnum) -> Unit)? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return true
    }

    inner class MyGestureDetector : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(start: MotionEvent?, end: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            if (abs(start!!.x - end.x) < 100 && abs(start.y - end.y) < 100) return false

            if (abs(start.x - end.x) > abs(start.y - end.y)) { // horizontal
                if (end.x > start.x) detectMotionDirectionListener?.invoke(SideEnum.RIGHT) //right
                else detectMotionDirectionListener?.invoke(SideEnum.LEFT) // left
            } else { // vertical
                if (start.y < end.y) detectMotionDirectionListener?.invoke(SideEnum.DOWN) //down
                else detectMotionDirectionListener?.invoke(SideEnum.UP) //up
            }

            return super.onFling(start, end, velocityX, velocityY)
        }
    }

    fun setDetectMotionDirectionListener(block: (SideEnum) -> Unit) {
        detectMotionDirectionListener = block
    }
}