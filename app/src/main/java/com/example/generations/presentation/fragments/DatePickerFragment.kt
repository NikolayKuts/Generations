package com.example.generations.presentation.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.example.generations.R
import java.util.*

class DatePickerFragment(private val callback: (year: Int, moth: Int, day: Int) -> Unit) :
    DialogFragment(), DatePickerDialog.OnDateSetListener {

    private companion object {
        const val DEFAULT_YEAR = 1990
        const val DEFAULT_MONTH = 0
        const val DEFAULT_DAY = 1

        const val MIN_DATE_1930: Long = -1262286000000
        const val MAX_DATE_2022: Long = 1641013200000
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(
            requireActivity(),
            R.style.MyPickerDialogTheme,
            this,
            DEFAULT_YEAR,
            DEFAULT_MONTH,
            DEFAULT_DAY
        )
            .apply {
                Date(MIN_DATE_1930).also { date -> datePicker.minDate = date.time }
                Date(MAX_DATE_2022).also { date -> datePicker.maxDate = date.time }
            }
    }


    override fun onDateSet(p0: DatePicker?, year: Int, monthIndex: Int, day: Int) {
        val month = convertMonthIndexToNumber(monthIndex)
        callback(year, month, day)
    }

    private fun convertMonthIndexToNumber(index: Int) = index + 1
}