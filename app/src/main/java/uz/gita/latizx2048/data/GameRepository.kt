package uz.gita.latizx2048.data

import android.util.Log
import uz.gita.latizx2048.utils.SideEnum
import kotlin.random.Random

class GameRepository {
    private val localStorage = LocalStorage.getInstance()
    val size = localStorage.getSize()
    private var ADDING_ELEMENT = 2
    private var PIK_ELEMENT = 2048
    var currentScore = 0
        private set
    var record = localStorage.getRecord()
        private set
    private var lastState = emptyMatrix()
    private var lastScore = 0

    private var matrix = when (size) {
        3 -> arrayOf(
            arrayOf(0, 0, 0),
            arrayOf(0, 0, 0),
            arrayOf(0, 0, 0)
        )

        5 -> arrayOf(
            arrayOf(0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0)
        )

        else -> arrayOf(
            arrayOf(0, 0, 0, 0),
            arrayOf(0, 0, 0, 0),
            arrayOf(0, 0, 0, 0),
            arrayOf(0, 0, 0, 0)
        )
    }

    private var matrix1 = arrayOf(
        arrayOf(4, 0, 0, 0),
        arrayOf(2, 8, 16, 64),
        arrayOf(32, 16, 512, 64),
        arrayOf(1024, 1024, 32, 256)
    )

    init {
        if (localStorage.getStateGame().isNotEmpty()) {
            currentScore = localStorage.getScore()
            val lastMatrix = localStorage.getStateGame()
            val setLastMatrix = emptyMatrix()

            for (i in 0 until size) {
                for (j in 0 until size) {
                    setLastMatrix[i][j] = lastMatrix[i * size + j]
                }
            }
            matrix = setLastMatrix
            localStorage.saveStateGame(emptyList())
        } else {
            addNewElem()
            addNewElem()
        }
    }

    fun getMatrix(): Array<Array<Int>> = matrix

    private fun addNewElem() {
        val emptyPosList = emptyPosList()
        if (emptyPosList().isNotEmpty()) {
            val randomIndex = Random.nextInt(0, emptyPosList.size)
            matrix[emptyPosList[randomIndex] / size][emptyPosList[randomIndex] % size] = ADDING_ELEMENT
        }
    }

    fun moveLeftDirection(left: SideEnum) {
        if (!canMove(left)) return
        val newMatrix = emptyMatrix()
        val ls = ArrayList<Int>()
        var isAdded: Boolean
        lastScore = currentScore

        for (i in 0 until size) {
            isAdded = false
            ls.clear()
            for (j in 0 until size) {
                if (matrix[i][j] == 0) continue
                if (ls.isEmpty()) {
                    ls.add(matrix[i][j])
                    continue
                }
                if (ls.last() == matrix[i][j] && !isAdded) {
                    ls[ls.size - 1] = 2 * ls.last()
                    currentScore += ls.last()
                    isAdded = true
                } else {
                    ls.add(matrix[i][j])
                    isAdded = false
                }
            }
            for (k in 0 until ls.size) {
                newMatrix[i][k] = ls[k]
            }
        }
        lastState = matrix
        matrix = newMatrix
        addNewElem()
    }

    fun moveRightDirection(right: SideEnum) {
        if (!canMove(right)) return
        val newMatrix = emptyMatrix()
        val ls = ArrayList<Int>()
        var isAdded: Boolean
        lastScore = currentScore

        for (i in 0 until size) {
            isAdded = false
            ls.clear()
            for (j in size - 1 downTo 0) {
                if (matrix[i][j] == 0) continue
                if (ls.isEmpty()) {
                    ls.add(matrix[i][j])
                    continue
                }
                if (ls.last() == matrix[i][j] && !isAdded) {
                    ls[ls.size - 1] = 2 * ls.last()
                    currentScore += ls.last()
                    isAdded = true
                } else {
                    ls.add(matrix[i][j])
                    isAdded = false
                }
            }
            for (k in ls.size - 1 downTo 0) newMatrix[i][size - 1 - k] = ls[k]
        }
        lastState = matrix
        matrix = newMatrix
        addNewElem()
    }

    fun moveDownDirection(down: SideEnum) {
        if (!canMove(down)) return
        val newMatrix = emptyMatrix()
        val ls = ArrayList<Int>()
        var isAdded: Boolean
        lastScore = currentScore

        for (i in 0 until size) {
            isAdded = false
            ls.clear()
            for (j in size - 1 downTo 0) {
                if (matrix[j][i] == 0) continue
                if (ls.isEmpty()) {
                    ls.add(matrix[j][i])
                    continue
                }
                if (ls.last() == matrix[j][i] && !isAdded) {
                    ls[ls.size - 1] = ls.last() * 2
                    currentScore += ls.last()
                    isAdded = true
                } else {
                    ls.add(matrix[j][i])
                    isAdded = false
                }
            }
            for (k in ls.size - 1 downTo 0) newMatrix[size - 1 - k][i] = ls[k]
        }
        lastState = matrix
        matrix = newMatrix
        addNewElem()
    }

    fun moveUpDirection(up: SideEnum) {
        if (!canMove(up)) return
        val newMatrix = emptyMatrix()
        val ls = ArrayList<Int>()
        var isAdded: Boolean
        lastScore = currentScore

        for (i in 0 until size) {
            isAdded = false
            ls.clear()
            for (j in 0 until size) {
                if (matrix[j][i] == 0) continue
                if (ls.isEmpty()) {
                    ls.add(matrix[j][i])
                    continue
                }
                if (ls.last() == matrix[j][i] && !isAdded) {
                    ls[ls.size - 1] = ls.last() * 2
                    currentScore += ls.last()
                    isAdded = true
                } else {
                    ls.add(matrix[j][i])
                    isAdded = false
                }
            }
            for (k in 0 until ls.size) newMatrix[k][i] = ls[k]
        }
        lastState = matrix
        matrix = newMatrix
        addNewElem()
    }

    fun isWin(): Boolean {
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (matrix[i][j] == PIK_ELEMENT) {
                    localStorage.removeStateGame()
//                    resGame()
                    return true
                }
            }
        }
        return false
    }

    fun isLost(): Boolean {
        if (emptyPosList().isEmpty() && !checkMoveVertical() && !checkMoveHorizontal()) {
            localStorage.removeStateGame()
//            resGame()
            return true
        }
        return false
    }

    private fun emptyMatrix(): Array<Array<Int>> = when (size) {
        3 -> arrayOf(
            arrayOf(0, 0, 0),
            arrayOf(0, 0, 0),
            arrayOf(0, 0, 0)
        )

        5 -> arrayOf(
            arrayOf(0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0)
        )

        else -> arrayOf(
            arrayOf(0, 0, 0, 0),
            arrayOf(0, 0, 0, 0),
            arrayOf(0, 0, 0, 0),
            arrayOf(0, 0, 0, 0)
        )
    }

    fun resGame() {
        if (currentScore >= record) localStorage.saveRecord(currentScore)
        matrix = emptyMatrix()
        addNewElem()
        addNewElem()
        currentScore = 0
    }

    fun lastState() {
        matrix = lastState
        currentScore = lastScore
    }

    fun saveState() {
        if (!isLost() && !isWin()) {
            val currentState = ArrayList<Int>(size * size)
            for (i in 0 until size) {
                for (j in 0 until size) {
                    currentState.add(matrix[i][j])
                }
            }
            localStorage.saveStateGame(list = currentState)
            localStorage.saveScore(score = currentScore)
        }
        if (currentScore >= record) localStorage.saveRecord(record = currentScore)
    }

    private fun emptyPosList(): ArrayList<Int> {
        val emptyPosList = ArrayList<Int>()
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (matrix[i][j] == 0)
                    emptyPosList.add(size * i + j)
            }
        }
        return emptyPosList
    }

    private fun checkMoveVertical(): Boolean {
        for (i in 0 until size - 1) {
            for (j in 0 until size) {
                if (matrix[i][j] == matrix[i + 1][j]) return true
            }
        }
        return false
    }

    private fun checkMoveHorizontal(): Boolean {
        for (i in 0 until size) {
            for (j in 0 until size - 1) {
                if (matrix[i][j] == matrix[i][j + 1]) return true
            }
        }
        return false
    }

    private fun canMove(direction: SideEnum): Boolean {
        for (i in 0..<size) {
            for (j in 0..<size) {
                if (matrix[i][j] != 0) {
                    when (direction) {
                        SideEnum.UP -> if (i > 0 && (matrix[i][j] == matrix[i - 1][j] || matrix[i - 1][j] == 0)) return true
                        SideEnum.DOWN -> if (i < size - 1 && (matrix[i][j] == matrix[i + 1][j] || matrix[i + 1][j] == 0)) return true
                        SideEnum.LEFT -> if (j > 0 && (matrix[i][j] == matrix[i][j - 1] || matrix[i][j - 1] == 0)) return true
                        SideEnum.RIGHT -> if (j < size - 1 && (matrix[i][j] == matrix[i][j + 1] || matrix[i][j + 1] == 0)) return true
                    }
                }
            }
        }
        return false
    }
}