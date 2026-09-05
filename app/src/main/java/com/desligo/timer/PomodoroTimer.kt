package com.desligo.timer

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import com.desligo.App
import com.desligo.MainActivity
import com.desligo.R
import com.desligo.util.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TimerState(
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val isBreak: Boolean = false,
    val totalSeconds: Int = Constants.DEFAULT_WORK_MINUTES * 60,
    val remainingSeconds: Int = Constants.DEFAULT_WORK_MINUTES * 60,
    val completedSessions: Int = 0
) {
    val progress: Float get() = if (totalSeconds > 0) 1f - (remainingSeconds.toFloat() / totalSeconds) else 0f
    val displayTime: String get() {
        val min = remainingSeconds / 60
        val sec = remainingSeconds % 60
        return "%02d:%02d".format(min, sec)
    }
}

class PomodoroTimer(private val context: Context) {

    private val _state = MutableStateFlow(TimerState())
    val state: StateFlow<TimerState> = _state.asStateFlow()

    private var timer: CountDownTimer? = null

    fun start() {
        val current = _state.value
        if (current.isPaused) {
            resume()
            return
        }

        val duration = if (current.isBreak) {
            if (current.completedSessions % Constants.SESSIONS_BEFORE_LONG_BREAK == 0)
                Constants.DEFAULT_LONG_BREAK_MINUTES * 60
            else
                Constants.DEFAULT_BREAK_MINUTES * 60
        } else {
            Constants.DEFAULT_WORK_MINUTES * 60
        }

        _state.value = _state.value.copy(
            isRunning = true,
            isPaused = false,
            totalSeconds = duration,
            remainingSeconds = duration
        )

        startTimer(duration)
    }

    fun pause() {
        timer?.cancel()
        _state.value = _state.value.copy(isPaused = true, isRunning = false)
    }

    fun stop() {
        timer?.cancel()
        _state.value = TimerState()
    }

    fun skip() {
        timer?.cancel()
        onTimerFinished()
    }

    private fun resume() {
        _state.value = _state.value.copy(isRunning = true, isPaused = false)
        startTimer(_state.value.remainingSeconds)
    }

    private fun startTimer(seconds: Int) {
        timer?.cancel()
        timer = object : CountDownTimer(seconds * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _state.value = _state.value.copy(
                    remainingSeconds = (millisUntilFinished / 1000).toInt()
                )
            }

            override fun onFinish() {
                onTimerFinished()
            }
        }.start()
    }

    private fun onTimerFinished() {
        val current = _state.value
        if (!current.isBreak) {
            // Work session completed
            _state.value = current.copy(
                isRunning = false,
                isBreak = true,
                completedSessions = current.completedSessions + 1,
                remainingSeconds = 0
            )
        } else {
            // Break completed
            _state.value = current.copy(
                isRunning = false,
                isBreak = false,
                remainingSeconds = 0
            )
        }
    }

    fun createNotification(): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return Notification.Builder(context, App.TIMER_CHANNEL_ID)
            .setContentTitle("Desligo Timer")
            .setContentText(_state.value.displayTime)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }
}
