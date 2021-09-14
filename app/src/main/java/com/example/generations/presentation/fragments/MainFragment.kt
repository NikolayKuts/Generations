package com.example.generations.presentation.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.generations.R
import com.example.generations.databinding.FragmentMainBinding
import com.example.generations.domain.pojo.DateConverter
import com.example.generations.domain.pojo.Generation
import com.example.generations.domain.pojo.GenerationDescriptionProvider
import com.example.generations.presentation.auxiliary.InputDateAssistant
import com.example.generations.presentation.view_models.MainViewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()

    private companion object {
        private const val DATE_PICKER_TAG = "date_picker"
    }

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

            viewModel.getDataSours { liveData ->
                liveData.observe(viewLifecycleOwner) { generation ->
                    generation?.let { resultTextView.text = generation.name }
                }
            }

            inputDateEditTextWrapper.setStartIconOnClickListener { onStartIconClick() }

            val assistant = InputDateAssistant(
                editText = inputDateEditText,
                editTextWrapper = inputDateEditTextWrapper
            )

            inputDateEditText.doOnTextChanged { text, _, _, _ -> assistant.validate(text) }

            showButton.setOnClickListener { onShowButtonClick(assistant = assistant) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onStartIconClick() {
        with(binding) {
            val datePicker = DatePickerFragment { year, month, day ->
                val date = DateConverter(year = year, month = month, day = day).getDateAsString()
                inputDateEditText.setText(date)
                inputDateEditText.setSelection(inputDateEditText.length())
            }
            datePicker.show(childFragmentManager, DATE_PICKER_TAG)
        }
    }

    private fun onShowButtonClick(assistant: InputDateAssistant) {
        val date = binding.inputDateEditText.text.toString()
        val context = binding.inputDateEditText.context
        if (assistant.isResultValidated(date)) {
            val year = date.substring(6, 10).toInt()
            val result = GenerationDescriptionProvider(year = year).getDescription()

            if (result != GenerationDescriptionProvider.EMPTY) {
                viewModel.insertGeneration(Generation(name = result))
            } else {
                showToast(context = context, messageId = R.string.impossible_calculation_message)
            }
        } else {
            showToast(context = context, messageId = R.string.no_completed_date_typing)
        }
    }

    private fun showToast(context: Context, messageId: Int) {
        val orientation = context.resources.configuration.orientation
        val message = context.resources.getString(messageId)
        if (orientation != Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        } else {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}