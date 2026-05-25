package com.example.pulsetimer.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.pulsetimer.databinding.FragmentConfigurationBinding

class ConfigurationFragment : Fragment() {

    private var _binding: FragmentConfigurationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigurationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            applyPreset(
                it.getInt("roundLength"),
                it.getInt("restLength"),
                it.getInt("bellInterval"),
                it.getInt("totalRounds")
            )
        }
    }

    fun getRoundLength(): Int {
        return binding.spinnerRoundLength.selectedItem.toString().toInt()
    }

    fun getRestDuration(): Int {
        return binding.spinnerRest.selectedItem.toString().toInt()
    }

    fun getTotalRounds(): Int {
        return binding.spinnerTotalRounds.selectedItem.toString().toInt()
    }

    fun getBellInterval(): Int {
        return binding.spinnerBell.selectedItem.toString().toInt()
    }

    // APPLY PRESET
    fun applyPreset(
        roundLength: Int,
        restLength: Int,
        bellInterval: Int,
        totalRounds: Int
    ) {
        binding.spinnerRoundLength.setSelection(
            getIndex(binding.spinnerRoundLength, roundLength.toString())
        )

        binding.spinnerRest.setSelection(
            getIndex(binding.spinnerRest, restLength.toString())
        )

        binding.spinnerBell.setSelection(
            getIndex(binding.spinnerBell, bellInterval.toString())
        )

        binding.spinnerTotalRounds.setSelection(
            getIndex(binding.spinnerTotalRounds, totalRounds.toString())
        )
    }

    private fun getIndex(spinner: Spinner, value: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == value) {
                return i
            }
        }
        return 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}