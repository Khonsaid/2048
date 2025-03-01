package uz.gita.latizx2048.presenter

import android.R.attr.value
import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.setBackground
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
//import com.github.matteobattilana.weather.PrecipType
//import com.github.matteobattilana.weather.WeatherView
import uz.gita.latizx2048.R
import uz.gita.latizx2048.data.GameRepository
import uz.gita.latizx2048.utils.MyTouchListener
import uz.gita.latizx2048.utils.SideEnum

class GameActivity : AppCompatActivity() {
    private val gameRepository = GameRepository()
    private val views = ArrayList<AppCompatTextView>(gameRepository.size * gameRepository.size)
    private lateinit var containerGame: GridLayout
    private val tvScore: TextView by lazy { findViewById(R.id.tv_score) }
    private val tvRecord: TextView by lazy { findViewById(R.id.tv_best) }
    private val btnRes: AppCompatImageButton by lazy { findViewById(R.id.btn_res) }
    private val btnLastState: AppCompatImageButton by lazy { findViewById(R.id.btn_back) }
    private val btnHome: AppCompatImageButton by lazy { findViewById(R.id.btn_home) }
    private val constraintLayout: ConstraintLayout by lazy { findViewById(R.id.main_game) }
//    private val weather: WeatherView by lazy { findViewById(R.id.weather_view) }
    private val resultGameLayout: LinearLayout by lazy { findViewById(R.id.result_game) }
    private val tvResultGame: TextView by lazy { findViewById(R.id.tv_result) }
    private val tvLevel: TextView by lazy { findViewById(R.id.tv_level) }
    private val containerRes: ConstraintLayout by lazy { findViewById(R.id.container_restart) }
    private val btnYes: AppCompatButton by lazy { findViewById(R.id.btn_yes_restart) }
    private val btnNo: AppCompatButton by lazy { findViewById(R.id.btn_no_restart) }
    private var restart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_grid)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_game)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadViews()
        showMatrix()
        slideListener()
        clickButtons()
        animationBackground()
    }

    private fun clickButtons() {
        btnRes.setOnClickListener {
            restart = false
            var isBtnLastVisible = false
            var isResultGameVisible = resultGameLayout.visibility == View.VISIBLE
            if (btnLastState.isVisible) {
                btnLastState.visibility = View.INVISIBLE
                isBtnLastVisible = true
            }
            containerRes.visibility = View.VISIBLE
            btnRes.visibility = View.INVISIBLE
            resultGameLayout.visibility = View.GONE //

            btnYes.setOnClickListener {
                gameRepository.resGame()
//                if (resultGameLayout.visibility == View.VISIBLE) {
//                    resultGameLayout.visibility = View.GONE
//                }
                containerGame.isEnabled = true
                showMatrix()

                containerRes.visibility = View.GONE
                btnRes.visibility = View.VISIBLE
                restart = true
                if (isBtnLastVisible) btnLastState.visibility = View.VISIBLE
            }
            btnNo.setOnClickListener {
                containerRes.visibility = View.GONE
                btnRes.visibility = View.VISIBLE
                restart = true
                if (isResultGameVisible) resultGameLayout.visibility = View.VISIBLE
                if (isBtnLastVisible) btnLastState.visibility = View.VISIBLE
            }
        }
        btnLastState.setOnClickListener {
            gameRepository.lastState()
            btnLastState.visibility = View.INVISIBLE
            showMatrix()
        }
        btnHome.setOnClickListener { onBackPressed() }
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    private fun slideListener() {
        val myTouchListener = MyTouchListener(this)
        myTouchListener.setDetectMotionDirectionListener { side ->
            if (restart) {
                when (side) {
                    SideEnum.UP -> {
                        gameRepository.moveUpDirection(side)
                    }

                    SideEnum.DOWN -> {
                        gameRepository.moveDownDirection(side)
                    }

                    SideEnum.LEFT -> {
                        gameRepository.moveLeftDirection(side)
                    }

                    SideEnum.RIGHT -> {
                        gameRepository.moveRightDirection(side)
                    }
                }
            }
            if (gameRepository.isWin()) {
                resultGameLayout.visibility = View.VISIBLE
                tvResultGame.text = "You are win!!!"
                if (btnLastState.isVisible) btnLastState.visibility = View.INVISIBLE
                containerGame.isEnabled = false
            } else if (gameRepository.isLost()) {
                resultGameLayout.visibility = View.VISIBLE
                tvResultGame.text = "Game over!!!"
                if (btnLastState.isVisible) btnLastState.visibility = View.INVISIBLE
                containerGame.isEnabled = false
            } else {
                btnLastState.visibility = View.VISIBLE
            }
            showMatrix()
        }
        containerGame.setOnTouchListener(myTouchListener)
    }

    @SuppressLint("SetTextI18n")
    private fun loadViews() {
        tvLevel.text = "${gameRepository.size}x${gameRepository.size}"

        containerGame = findViewById(R.id.container_game)
        containerGame.columnCount = gameRepository.size
        containerGame.rowCount = gameRepository.size

        for (i in 0 until containerGame.rowCount * containerGame.columnCount) {
            val textView = LayoutInflater.from(this).inflate(R.layout.item_btn_3x3, constraintLayout, false) as AppCompatTextView
            val param = GridLayout.LayoutParams().apply {
                width = 0
                height = 0
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }

            textView.layoutParams = param
            containerGame.addView(textView)
            views.add(textView)
        }
    }

    private fun showMatrix() {
        val matrix = gameRepository.getMatrix()
        tvRecord.text = "${gameRepository.record}"

        when (gameRepository.currentScore < gameRepository.record) {
            true -> tvScore.text = "${gameRepository.currentScore}"
            else -> {
                tvScore.text = "${gameRepository.currentScore}"
                tvRecord.text = "${gameRepository.currentScore}"
            }
        }

        for (i in 0 until gameRepository.size) {
            for (j in 0 until gameRepository.size) {
                views[gameRepository.size * i + j].text = if (matrix[i][j] != 0) matrix[i][j].toString() else ""
                views[gameRepository.size * i + j].setBackgroundResource(setBackground(matrix[i][j]))
            }
        }
    }

    private fun setBackground(value: Int): Int {
        return when (value) {
            0 -> R.drawable.bg_0
            2 -> R.drawable.bg_btn_2
            4 -> R.drawable.bg_btn_4
            8 -> R.drawable.bg_btn_8
            16 -> R.drawable.bg_btn_16
            32 -> R.drawable.bg_btn_32
            64 -> R.drawable.bg_btn_64
            128 -> R.drawable.bg_btn_128
            256 -> R.drawable.bg_btn_256
            512 -> R.drawable.bg_btn_512
            1024 -> R.drawable.bg_btn_1024
            else -> R.drawable.bg_btn_2048
        }
    }

    override fun onPause() {
        super.onPause()
        gameRepository.saveState()
    }

    private fun animationBackground() {
//        weather.apply {
//            setWeatherData(PrecipType.SNOW)
//            speed = 200
//            emissionRate = 20f
//            angle = 15
//            fadeOutPercent = .65f
//        }

        val animation: AnimationDrawable = constraintLayout.background as AnimationDrawable
        animation.setEnterFadeDuration(2000)
        animation.setExitFadeDuration(6000)
        animation.start()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_exit, null, false)
        val dialog = Dialog(this)
        dialog.setContentView(view)
        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        view.findViewById<Button>(R.id.btn_yes_exit).setOnClickListener {
            if (gameRepository.isLost() || gameRepository.isLost()) gameRepository.resGame()
            finish()
        }
        view.findViewById<Button>(R.id.btn_no_exit).setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
}