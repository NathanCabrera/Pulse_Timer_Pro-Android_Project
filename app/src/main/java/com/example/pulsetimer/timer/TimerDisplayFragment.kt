package com.example.pulsetimer.timer

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.pulsetimer.databinding.FragmentTimerDisplayBinding

// INTERFACE
interface TimerControls {
    fun onStartClicked()
    fun onPauseClicked()
    fun onResetClicked()
    fun onEndClicked()
}

class TimerDisplayFragment : Fragment() {

    private var _binding: FragmentTimerDisplayBinding? = null
    private val binding get() = _binding!!

    private var listener: TimerControls? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? TimerControls
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimerDisplayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStart.setOnClickListener {
            listener?.onStartClicked()
        }

        binding.btnPause.setOnClickListener {
            listener?.onPauseClicked()
        }

        binding.btnRestart.setOnClickListener {
            listener?.onResetClicked()
        }

        binding.btnEnd.setOnClickListener {
            listener?.onEndClicked()
        }
    }

    fun updateUI(
        timeLeft: Long,
        currentRound: Int,
        totalRounds: Int,
        isResting: Boolean,
        restLength: Long,
        roundLength: Long
    ) {
        if (_binding == null) return

        val minutes = (timeLeft / 1000) / 60
        val seconds = (timeLeft / 1000) % 60

        binding.tvTimeRemaining.text = String.format("%d:%02d", minutes, seconds)

        binding.tvRoundInfo.text =
            if (!isResting) "Round: $currentRound of $totalRounds"
            else "Rest"

        binding.tvNextRest.text =
            if (!isResting) "Next Rest: ${restLength / 1000} sec"
            else ""

        val totalTime = if (!isResting) roundLength else restLength
        val progress = if (totalTime > 0) {
            ((timeLeft.toFloat() / totalTime) * 100).toInt()
        } else 0

        binding.circularProgress.progress = progress
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
