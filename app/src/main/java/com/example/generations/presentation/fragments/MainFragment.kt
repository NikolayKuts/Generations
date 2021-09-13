package com.example.generations.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.generations.databinding.FragmentMainBinding
import com.example.generations.domain.pojo.DateConverter
import com.example.generations.presentation.auxiliary.InputDateAssistant

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            dateInputEditTextWrapper.setStartIconOnClickListener {
                DatePickerFragment() { year, month, day ->
                    val date =
                        DateConverter(year = year, month = month, day = day).getDateAsString()
                }
            }

            val assistant = InputDateAssistant(
                editText = dateInputEditText,
                editTextWrapper = dateInputEditTextWrapper
            )

            dateInputEditText.doOnTextChanged { text, start, before, count ->
                assistant.validate(text)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}