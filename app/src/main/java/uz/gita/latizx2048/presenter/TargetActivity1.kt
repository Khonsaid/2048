//import android.os.Bundle
//import android.view.LayoutInflater
//import android.widget.Button
//import android.widget.GridLayout
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.AppCompatTextView
//import com.skydoves.elasticviews.ElasticLayout
//import uz.gita.latizx2048.R
//import uz.gita.latizx2048.data.LocalStorage
//
//class TargetActivity1 : AppCompatActivity() {
//    private val gridLayouts: Map<Int, Lazy<GridLayout>> by lazy {
//        mapOf(
//            3 to lazy { findViewById<GridLayout>(R.id.grid_3x3) },
//            4 to lazy { findViewById<GridLayout>(R.id.grid_4x4) },
//            5 to lazy { findViewById<GridLayout>(R.id.grid_5x5) }
//        )
//    }
//
//    private val elasticLayouts: Map<Int, Lazy<ElasticLayout>> by lazy {
//        mapOf(
//            3 to lazy { findViewById<ElasticLayout>(R.id.fl_grid_3x3) },
//            4 to lazy { findViewById<ElasticLayout>(R.id.fl_grid_4x4) },
//            5 to lazy { findViewById<ElasticLayout>(R.id.fl_grid_5x5) }
//        )
//    }
//
//    private val btnHome: Button by lazy { findViewById(R.id.btn_back_to_home_target) }
//    private val localStorage = LocalStorage.getInstance()
//    private lateinit var views: List<AppCompatTextView>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_game)
//
//        setupClickListeners()
//        loadViews()
//    }
//
//    private fun setupClickListeners() {
//        btnHome.setOnClickListener { finish() }
//
//        elasticLayouts.forEach { (size, layoutLazy) ->
//            layoutLazy.value.setOnClickListener {
//                handleSizeSelection(size)
//            }
//        }
//    }
//
//    private fun handleSizeSelection(selectedSize: Int) {
//        localStorage.setSize(selectedSize)
//        gridLayouts.forEach { (size, layoutLazy) ->
//            val layout = layoutLazy.value
//            if (size == selectedSize) {
//                createViews(layout, size, R.drawable.bg_0)
//                loadDataToMatrix(getStateGameForSize(size), size)
//            } else {
//                createViews(layout, size, R.drawable.bg_0)
//            }
//        }
//    }
//
//    private fun getStateGameForSize(size: Int): List<Int> = when (size) {
//        3 -> localStorage.getStateGame3()
//        4 -> localStorage.getStateGame4()
//        5 -> localStorage.getStateGame5()
//        else -> emptyList()
//    }
//
//    private fun loadViews() {
//        val currentSize = localStorage.getSize()
//        gridLayouts.forEach { (size, layoutLazy) ->
//            val layout = layoutLazy.value
//            createViews(layout, size, R.drawable.bg_0)
//            if (size == currentSize) {
//                loadDataToMatrix(getStateGameForSize(size), size)
//            }
//        }
//    }
//
//    private fun createViews(container: GridLayout, target: Int, backgroundId: Int) {
//        container.apply {
//            removeAllViews()
//            rowCount = target
//            columnCount = target
//        }
//
//        views = List(target * target) {
//            LayoutInflater.from(this).inflate(R.layout.item_btn_3x3, container, false) as AppCompatTextView
//        }.onEach { view ->
//            view.layoutParams = GridLayout.LayoutParams().apply {
//                width = 0
//                height = 0
//                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
//                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
//            }
//            view.setBackgroundResource(backgroundId)
//            container.addView(view)
//        }
//    }
//
//    private fun loadDataToMatrix(lastMatrix: List<Int>, size: Int) {
//        val setLastMatrix = Array(size) { i ->
//            Array(size) { j -> lastMatrix[i * size + j] }
//        }
//        showMatrix(setLastMatrix, size)
//    }
//
//    private fun showMatrix(matrix: Array<Array<Int>>, size: Int) {
//        matrix.forEachIndexed { i, row ->
//            row.forEachIndexed { j, value ->
//                views[size * i + j].apply {
//                    text = if (value != 0) value.toString() else ""
//                    setBackgroundResource(setBackground(value))
//                }
//            }
//        }
//    }
//
//    private fun setBackground(value: Int): Int = when (value) {
//        0 -> R.drawable.bg_0
//        2 -> R.drawable.bg_btn_2
//        4 -> R.drawable.bg_btn_4
//        8 -> R.drawable.bg_btn_8
//        16 -> R.drawable.bg_btn_16
//        32 -> R.drawable.bg_btn_32
//        64 -> R.drawable.bg_btn_64
//        128 -> R.drawable.bg_btn_128
//        256 -> R.drawable.bg_btn_256
//        512 -> R.drawable.bg_btn_512
//        1024 -> R.drawable.bg_btn_1024
//        else -> R.drawable.bg_btn_2048
//    }
//}