package com.cos.lotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    // 버튼 연결
    private val btnClear: Button by lazy {
        findViewById<Button>(R.id.btnClear)
    }

    private val btnAdd: Button by lazy {
        findViewById<Button>(R.id.btnAdd)
    }

    private val btnRun: Button by lazy {
        findViewById<Button>(R.id.btnRun)
    }

    private val numberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.NumberPicker)
    }

    private val numTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById<TextView>(R.id.tv1Num),
            findViewById<TextView>(R.id.tv2Num),
            findViewById<TextView>(R.id.tv3Num),
            findViewById<TextView>(R.id.tv4Num),
            findViewById<TextView>(R.id.tv5Num),
            findViewById<TextView>(R.id.tv6Num)
        )
    }

    private var didRun = false

    private val pickNumberSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        init()
        initRunButton()
        initAddButton()
        initClearButton()
    }

    private fun init() {

    }

    private fun initRunButton() {
        btnRun.setOnClickListener {
            val list = getRandomNum()

            didRun = true

            list.forEachIndexed { index, number ->
                val textView = numTextViewList[index]

                textView.text = number.toString()
                textView.isVisible = true

                setNumberBackground(number, textView)
            }

        }
    }

    private fun initAddButton() {
        btnAdd.setOnClickListener {
            if (didRun) {
                Toast.makeText(this,"초기화 후에 시작해 주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pickNumberSet.size >= 5) {
                Toast.makeText(this,"번호는 5개 까지만 선택할 수 있습니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickNumberSet.contains(numberPicker.value)) {
                Toast.makeText(this,"이미 선택한 번호 입니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textView = numTextViewList[pickNumberSet.size]
            textView.isVisible = true
            textView.text = numberPicker.value.toString()

            setNumberBackground(numberPicker.value, textView)

            textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)

            pickNumberSet.add(numberPicker.value)
        }
    }

    private fun setNumberBackground(number:Int, textView: TextView ) {
        when(number) {
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 31..40 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
        }
    }

    private fun initClearButton() {
        btnClear.setOnClickListener {
            pickNumberSet.clear()
            numTextViewList.forEach {
                it.isVisible = false
            }
            didRun = false
        }
    }

    private fun getRandomNum(): List<Int> {
        val numberList = mutableListOf<Int>()
            .apply {
                for (i in 1..45) {
                    if (pickNumberSet.contains(i)){
                        continue
                    }
                    this.add(i)
                }
            }
        numberList.shuffle()

        val newList = pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)

        return newList.sorted()
    }

}