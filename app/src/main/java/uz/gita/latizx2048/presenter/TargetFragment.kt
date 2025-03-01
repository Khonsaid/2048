package uz.gita.latizx2048.presenter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.skydoves.elasticviews.ElasticLayout
import uz.gita.latizx2048.R
import uz.gita.latizx2048.data.LocalStorage

class TargetFragment : Fragment(R.layout.activity_target) {
    private lateinit var gridLayout3x3: GridLayout
    private lateinit var gridLayout4x4: GridLayout
    private lateinit var gridLayout5x5: GridLayout
    private lateinit var elastic5x5: ElasticLayout
    private lateinit var elastic3x3: ElasticLayout
    private lateinit var elastic4x4: ElasticLayout
    private lateinit var tv3x3: TextView
    private lateinit var tv4x4: TextView
    private lateinit var tv5x5: TextView
    private val localStorage = LocalStorage.getInstance()
    private lateinit var views: ArrayList<AppCompatTextView>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        gridLayout3x3 = view.findViewById(R.id.grid_3x3)
        gridLayout4x4 = view.findViewById(R.id.grid_4x4)
        gridLayout5x5 = view.findViewById(R.id.grid_5x5)
        elastic5x5 = view.findViewById(R.id.fl_grid_5x5)
        elastic3x3 = view.findViewById(R.id.fl_grid_3x3)
        elastic4x4 = view.findViewById(R.id.fl_grid_4x4)
        tv3x3 = view.findViewById(R.id.tv_3x3)
        tv4x4 = view.findViewById(R.id.tv_4x4)
        tv5x5 = view.findViewById(R.id.tv_5x5)
//        loadViews()
        clickVies()
    }

    private fun clickVies() {
        elastic3x3.setOnClickListener {
            localStorage.setSize(3)
            if (localStorage.getSize() == 3 && localStorage.getStateGame3().isNotEmpty()) {
                createViews(gridLayout3x3, 3, R.drawable.bg_0)
                loadDataToMatrix(localStorage.getStateGame3(), 3)
            }
            createViews(gridLayout4x4, 4, R.drawable.bg_0)
            createViews(gridLayout5x5, 5, R.drawable.bg_0)
            tv4x4.visibility = View.VISIBLE
            tv5x5.visibility = View.VISIBLE
            tv3x3.visibility = View.INVISIBLE
        }
        elastic4x4.setOnClickListener {
            localStorage.setSize(4)
            if (localStorage.getSize() == 4 && localStorage.getStateGame4().isNotEmpty()) {
                createViews(gridLayout4x4, 4, R.drawable.bg_0)
                loadDataToMatrix(localStorage.getStateGame4(), 4)
            }
            createViews(gridLayout5x5, 5, R.drawable.bg_0)
            createViews(gridLayout3x3, 3, R.drawable.bg_0)
            tv3x3.visibility = View.VISIBLE
            tv5x5.visibility = View.VISIBLE
            tv4x4.visibility = View.INVISIBLE
        }
        elastic5x5.setOnClickListener {
            localStorage.setSize(5)
            if (localStorage.getSize() == 5 && localStorage.getStateGame5().isNotEmpty()) {
                createViews(gridLayout5x5, 5, R.drawable.bg_0)
                loadDataToMatrix(localStorage.getStateGame5(), 5)
            }
            createViews(gridLayout4x4, 4, R.drawable.bg_0)
            createViews(gridLayout3x3, 3, R.drawable.bg_0)
            tv4x4.visibility = View.VISIBLE
            tv3x3.visibility = View.VISIBLE
            tv5x5.visibility = View.INVISIBLE
        }
    }

    private fun loadViews() {
        if (localStorage.getSize() == 3 && localStorage.getStateGame3().isNotEmpty()) {
            createViews(gridLayout3x3, 3, R.drawable.bg_0)
            loadDataToMatrix(localStorage.getStateGame3(), 3)
            tv4x4.visibility = View.VISIBLE
            tv5x5.visibility = View.VISIBLE
            tv3x3.visibility = View.INVISIBLE
        } else createViews(gridLayout3x3, 3, R.drawable.bg_0)

        if (localStorage.getSize() == 4 && localStorage.getStateGame4().isNotEmpty()) {
            createViews(gridLayout4x4, 4, R.drawable.bg_0)
            loadDataToMatrix(localStorage.getStateGame4(), 4)
            tv3x3.visibility = View.VISIBLE
            tv5x5.visibility = View.VISIBLE
            tv4x4.visibility = View.INVISIBLE
        } else createViews(gridLayout4x4, 4, R.drawable.bg_0)

        if (localStorage.getSize() == 5 && localStorage.getStateGame5().isNotEmpty()) {
            createViews(gridLayout5x5, 5, R.drawable.bg_0)
            loadDataToMatrix(localStorage.getStateGame5(), 5)
            tv4x4.visibility = View.VISIBLE
            tv3x3.visibility = View.VISIBLE
            tv5x5.visibility = View.INVISIBLE
        } else createViews(gridLayout5x5, 5, R.drawable.bg_0)
    }

    override fun onResume() {
        super.onResume()
        loadViews()
    }

    private fun createViews(container: GridLayout, target: Int, backgroundId: Int) {
        container.apply {
            removeAllViews()
            rowCount = target
            columnCount = target
        }

        views = ArrayList(target * target)
        for (i in 0 until target * target) {
            val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_btn_3x3, container, false) as AppCompatTextView
            val param = GridLayout.LayoutParams().apply {
                width = 0
                height = 0
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            view.setBackgroundResource(backgroundId)
            view.layoutParams = param
            container.addView(view)

            views.add(view)
        }
    }

    private fun loadDataToMatrix(lastMatrix: List<Int>, size: Int) {
        val setLastMatrix = Array(size) { Array(size) { 0 } }

        for (i in 0 until size) {
            for (j in 0 until size) {
                setLastMatrix[i][j] = lastMatrix[i * size + j]
            }
        }
        showMatrix(setLastMatrix, size)
    }

    private fun showMatrix(matrix: Array<Array<Int>>, size: Int) {
        for (i in matrix.indices) {
            for (j in matrix.indices) {
                views[size * i + j].text = if (matrix[i][j] != 0) matrix[i][j].toString() else ""
                views[size * i + j].textSize = 12f
                views[size * i + j].setBackgroundResource(setBackground(matrix[i][j]))
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
}