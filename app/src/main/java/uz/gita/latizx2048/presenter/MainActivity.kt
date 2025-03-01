package uz.gita.latizx2048.presenter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import uz.gita.latizx2048.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        clickButtons()
        supportFragmentManager.beginTransaction().replace(R.id.container_fragment, TargetFragment()).commit()
    }

    private fun clickButtons() {
        findViewById<Button>(R.id.btn_start).setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }
        findViewById<ImageView>(R.id.btn_question).setOnClickListener {
            val view = LayoutInflater.from(this).inflate(R.layout.dialog_info, null, false)
            val dialog = Dialog(this)
            dialog.setContentView(view)
            dialog.window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
            }
            view.findViewById<Button>(R.id.btn_back_to_home_info).setOnClickListener { dialog.dismiss() }
            dialog.setCancelable(false)
            dialog.show()
        }
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_exit, null, false)
        val dialog = Dialog(this, R.style.FullScreenDialog)
        dialog.setContentView(view)
        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        view.findViewById<Button>(R.id.btn_yes_exit).setOnClickListener { finish() }
        view.findViewById<Button>(R.id.btn_no_exit).setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
}