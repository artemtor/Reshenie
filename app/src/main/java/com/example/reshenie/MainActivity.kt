package com.example.reshenie

import com.example.reshenie.databinding.ActivityMainBinding
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var correctAnswers = 0
    private var incorrectAnswers = 0
    private var totalAnswers = 0
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var maxTime: Long = 0
    private var minTime: Long = Long.MAX_VALUE
    private var avgTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.but2ton.isEnabled = false
        binding.but22ton.isEnabled = false
        binding.button.setOnClickListener {
            generateExample()
            binding.button.isEnabled = false
            binding.but2ton.isEnabled = true
            binding.but22ton.isEnabled = true
            startTime = SystemClock.elapsedRealtime()
        }


        binding.but2ton.setOnClickListener {
            checkAnswer(true)
        }

        binding.but22ton.setOnClickListener {
            checkAnswer(false)
        }

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        }
        updateUI()
    }

    private fun getFloatTwoDigits(number: Double): String {
        val format = DecimalFormat("#.##")
        format.roundingMode = RoundingMode.FLOOR
        return format.format(number)
    }

    private fun calculate(): Double {
        val num1 = binding.textView6.text.toString().toDouble()
        val num2 = binding.textView7.text.toString().toDouble()
        val operator = binding.textView5.text.toString()
        var result = 0.0
        when (operator) {
            "*" -> result = num1 * num2
            "/" -> result = getFloatTwoDigits(num1 / num2).toDouble()
            "-" -> result = num1 - num2
            "+" -> result = num1 + num2
        }
        return result
    }

    private fun generateExample() {
        binding.textView6.text = (10..99).random().toString()
        binding.textView7.text = (10..99).random().toString()
        when ((0..3).random()) {
            0 -> binding.textView5.text = "*"
            1 -> binding.textView5.text = "/"
            2 -> binding.textView5.text = "-"
            3 -> binding.textView5.text = "+"
        }
        val result = calculate()
        if ((0..1).random() == 0) {
            binding.answere.text = result.toString()
        } else {
            var answer: String
            do {
                answer = getFloatTwoDigits(Random.nextDouble(-999.0, 999.0))
            } while (answer.toDouble() == result)
            binding.answere.text = answer
        }
    }

    private fun checkAnswer(answer: Boolean) {
        totalAnswers++
        endTime = SystemClock.elapsedRealtime()
        val time = endTime - startTime
        avgTime = (avgTime * totalAnswers + time) / totalAnswers
        maxTime = max(time, maxTime)
        minTime = min(time, minTime)
        if (answer == (calculate() == (binding.answere.text.toString().toDoubleOrNull() ?: 0))) {
            correctAnswers++
        } else {
            incorrectAnswers++
        }

        binding.button.isEnabled = true
        binding.but2ton.isEnabled = false
        binding.but22ton.isEnabled = false
        updateUI()
    }

    private fun updateUI() {
        binding.countRightText5.text = correctAnswers.toString()
        binding.countErrorText6.text = incorrectAnswers.toString()
        binding.textView3.text = totalAnswers.toString()
        binding.textView4.text = "%.2f%%".format(correctAnswers.toDouble() / (if (totalAnswers > 0) totalAnswers else 1) * 100)
        binding.countErrorText.text = (maxTime / 1000).toString()
        binding.countRightText.text = ((if (minTime == Long.MAX_VALUE) 0 else minTime) / 1000).toString()
        binding.countErrorText12.text = (avgTime / 1000).toString()
    }

    override fun onSaveInstanceState(instanceState: Bundle) {
        super.onSaveInstanceState(instanceState)
        instanceState.putInt("correctAnswers", correctAnswers)
        instanceState.putInt("incorrectAnswers", incorrectAnswers)
        instanceState.putInt("totalAnswers", totalAnswers)
        instanceState.putLong("maxTime", maxTime)
        instanceState.putLong("minTime", minTime)
        instanceState.putLong("avgTime", avgTime)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        correctAnswers = savedInstanceState.getInt("correctAnswers")
        incorrectAnswers = savedInstanceState.getInt("incorrectAnswers")
        totalAnswers = savedInstanceState.getInt("totalAnswers")
        maxTime = savedInstanceState.getLong("maxTime")
        minTime = savedInstanceState.getLong("minTime")
        avgTime = savedInstanceState.getLong("avgTime")
    }
}