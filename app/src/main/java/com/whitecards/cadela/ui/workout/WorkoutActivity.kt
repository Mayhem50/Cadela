package com.whitecards.cadela.ui.workout

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.whitecards.cadela.R
import com.whitecards.cadela.databinding.ActivityWorkoutBinding

class WorkoutActivity : AppCompatActivity() {
    private lateinit var _viewModel: WorkoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewModel = ViewModelProviders.of(this).get(WorkoutViewModel::class.java)
        var binding = DataBindingUtil.setContentView<ActivityWorkoutBinding>(this, R.layout.activity_workout)
        binding.vm = _viewModel

        initObservers()
    }

    private fun initObservers() {
        _viewModel.percentageTimeLeft.observe(this, Observer {
            var progressBar = findViewById<ProgressBar>(R.id.timeProgressBar)
            progressBar.progress = it
        })

        _viewModel.timeLeft.observe(this, Observer {
            var timeText = findViewById<TextView>(R.id.workout_timer)
            timeText.text = it
        })

        _viewModel.isLastExercise.observe(this, Observer {
            var button = findViewById<Button>(R.id.workout_button)
            if (it) {
                button.text = resources.getString(R.string.end_workout)
            } else {
                button.text = resources.getString(R.string.next_exercise)
            }
        })

        _viewModel.buttonEnable.observe(this, Observer {
            var button = findViewById<Button>(R.id.workout_button)
            button.isEnabled = it
        })
    }
}
