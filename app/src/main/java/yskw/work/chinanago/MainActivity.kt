package yskw.work.chinanago

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import android.view.View
import android.os.Handler

class MainActivity : AppCompatActivity() {
    private var mGestureDetector: GestureDetector? = null
    // Y軸最低スワイプ距離
    private val SWIPE_MIN_DISTANCE = 50
    // Y軸最低スワイプスピード
    private val SWIPE_THRESHOLD_VELOCITY = 200
    // X軸の移動距離 これ以上なら縦移動を判定しない
    private val SWIPE_MAX_OFF_PATH = 200

    private var flick_count = 0
    private var visible_flg = true
    private val mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val messageView: TextView = findViewById(R.id.textView1)
        messageView.text = "0"
        mGestureDetector = GestureDetector(this, mOnGestureListener) // => 忘れない
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
                // 移動距離・スピードを出力
                val distance_y = Math.abs(event1.y - event2.y)
                val velocity_y = Math.abs(velocityY)
                val messageView: TextView = findViewById(R.id.textView1)
                val chinanaImg: ImageView = findViewById(R.id.chinanago)
//                messageView.text = "縦の移動距離:$distance_y 縦の移動スピード:$velocity_y"

                // X軸の移動距離が大きすぎる場合
                if (Math.abs(event1.x - event2.x) > SWIPE_MAX_OFF_PATH) {
//                    messageView.text = "横の移動距離が大きすぎます"

                    // 開始位置から終了位置の移動距離が指定値より大きい
                    // Y軸の移動速度が指定値より大きい
                } else if (event2.y - event1.y > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
//                    messageView.text = "上から下"
                    chinanaImg.setVisibility(View.VISIBLE)
                    visible_flg = true

                    // 終了位置から開始位置の移動距離が指定値より大きい
                    // Y軸の移動速度が指定値より大きい
                } else if (event1.y - event2.y > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {

                    println("flick")
                    chinanaImg.setVisibility(View.INVISIBLE)


                    if (visible_flg) {
                        println("@@@@")
                        flick_count = flick_count + 1
                        mHandler.post {
                            // この部分はUIスレッドで動作する
                            Thread.sleep(1000)
                            chinanaImg.setVisibility(View.VISIBLE)
                            visible_flg = true
                        }
                    }
                    visible_flg = false
//                    messageView.text = "下から上"
                    messageView.text = "$flick_count"
                }

            } catch (e: Exception) {
                // TODO
            }

            return false
        }
        // 長押し
        override fun onLongPress(e: MotionEvent?) {
            super.onLongPress(e)
        }
    }

}
