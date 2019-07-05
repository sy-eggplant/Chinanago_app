package yskw.work.chinanago

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout
import android.view.View
import android.content.Intent
import android.os.Handler
import kotlin.random.Random



class MainActivity : AppCompatActivity() {
    private var mGestureDetector: GestureDetector? = null
    // Y軸最低スワイプ距離
    private val SWIPE_MIN_DISTANCE = 50
    // Y軸最低スワイプスピード
    private val SWIPE_THRESHOLD_VELOCITY = 200

    private var flick_count = 0
    private var visible_flg = true
    private val mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mGestureDetector = GestureDetector(this, mOnGestureListener) // => 忘れない
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this, DictionalyActivity::class.java)
            startActivity(intent)
        }
    }

    // タッチイベント
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return mGestureDetector!!.onTouchEvent(event)
    }

    // タッチイベントのリスナー
    private val mOnGestureListener = object : GestureDetector.SimpleOnGestureListener() {
        // フリックイベント
        override fun onFling(event1: MotionEvent, event2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            try {
                val messageView: TextView = findViewById(R.id.textView1)
                val chinanaImg: ImageView = findViewById(R.id.chinanago)

                if (event2.y - event1.y > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    // 上から下
                    chinanaImg.setVisibility(View.VISIBLE)
                    visible_flg = true

                } else if (event1.y - event2.y > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    // 下から上、フリック
                    chinanaImg.setVisibility(View.INVISIBLE)

                    if (visible_flg) {
                        flick_count++
                        mHandler.post {
                            // スレッド内、1秒待つ
                            Thread.sleep(1000)
                            val imageWidth = Random.nextInt(1000)
                            val imageHeight = Random.nextInt(2000)
                            val layoutParams = LinearLayout.LayoutParams(imageWidth, imageHeight)
                            chinanaImg.layoutParams = layoutParams
                            chinanaImg.setVisibility(View.VISIBLE)
                            visible_flg = true
                        }
                    }
                    visible_flg = false
                    messageView.text = "$flick_count"
                }

            } catch (e: Exception) {
                // TODO
            }

            return false
        }
    }

}
