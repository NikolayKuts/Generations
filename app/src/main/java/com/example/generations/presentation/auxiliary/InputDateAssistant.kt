package com.example.generations.presentation.auxiliary

import android.util.Log
import android.widget.EditText
import com.example.generations.R
import com.google.android.material.textfield.TextInputLayout

class InputDateAssistant(
    private val editText: EditText,
    private val editTextWrapper: TextInputLayout,
) {
    private val context = editText.context
    private var date: String = ""
    private var isWarned = false
    private var isSlashAdded = false
    private var lastText = ""

    private companion object {
        private const val SPLITTER_PATTERN = "[/.-]"
        private const val DAY_PATTERN = "^\\d\\d"
        private const val DAY_WITH_SPLITTER_PATTERN = "$DAY_PATTERN?$SPLITTER_PATTERN"
        private const val LINE_START_WITH_SYMBOL_PATTERN = "^$SPLITTER_PATTERN"
        private const val EXCESS_ZEROS_IN_LINE_START_PATTERN = "^00+"
        private const val NULLABLE_DAY_PATTERN = "^0$SPLITTER_PATTERN"
        private const val EXCESS_DAY_DIGITS_PATTERN = "^\\d{3}"
        private const val DAY_LESS_THAN_TEN_PATTERN = "^\\d$SPLITTER_PATTERN"
        private const val EXCESS_SYMBOLS_AFTER_DAY_PATTERN = DAY_WITH_SPLITTER_PATTERN +
                "$SPLITTER_PATTERN+"
        private const val EXCESS_ZEROS_IN_MONTH_PATTERN = "${DAY_WITH_SPLITTER_PATTERN}00+"
        private const val NULLABLE_MONTH_PATTERN = "${DAY_WITH_SPLITTER_PATTERN}0$SPLITTER_PATTERN"
        private const val MONTH_PATTERN = "$DAY_WITH_SPLITTER_PATTERN\\d\\d"
        private const val MONTH_LESS_THAN_TEN_PATTERN = "$DAY_WITH_SPLITTER_PATTERN\\d" +
                SPLITTER_PATTERN
        private const val DAY_WITH_MONTH_PATTERN = "$DAY_WITH_SPLITTER_PATTERN\\d\\d"
        private const val EXCESS_MONTH_DIGITS_AFTER_SLASH_PATTERN = DAY_WITH_SPLITTER_PATTERN +
                "\\d{3,}"
        private const val DAY_MONTH_WITH_SPLITTER_PATTERN = DAY_WITH_MONTH_PATTERN +
                SPLITTER_PATTERN
        private const val EXCESS_SYMBOLS_AFTER_MONTH_PATTERN = DAY_WITH_MONTH_PATTERN +
                "($SPLITTER_PATTERN){2,}"
        private const val YEAR_START_WITH_ZERO_PATTERN = "${DAY_MONTH_WITH_SPLITTER_PATTERN}0+"
        private const val YEAR_ENDS_WITH_SYMBOL_PATTERN = DAY_MONTH_WITH_SPLITTER_PATTERN +
                "\\d?\\d?\\d?\\d?$SPLITTER_PATTERN"
        private const val FULL_DATE_PATTERN = "$DAY_MONTH_WITH_SPLITTER_PATTERN\\d{4}"
        private const val EXCESS_YEAR_DIGITS_PATTERN = "$FULL_DATE_PATTERN\\d+"

        private const val MAXIMUM_DAY = 31
        private const val MAXIMUM_MONTH = 12
        private const val MAXIMUM_YEAR = 2021
        private const val SLASH = "/"
        private const val ZERO = "0"
    }

    fun isResultValidated(result: String): Boolean = (Regex(FULL_DATE_PATTERN).matches(result))

    fun validate(text: CharSequence?) {
        date = text?.toString()?.trim() ?: ""

        when {
            Regex(pattern = LINE_START_WITH_SYMBOL_PATTERN).containsMatchIn(date) -> {
                onIfStartsWithCharacter()
            }
            Regex(pattern = EXCESS_ZEROS_IN_LINE_START_PATTERN).containsMatchIn(date) -> {
                onIfDayHasExcessZeros()
            }
            Regex(pattern = NULLABLE_DAY_PATTERN).containsMatchIn(date) -> {
                onIfDayNullable()
            }
            Regex(pattern = DAY_PATTERN).containsMatchIn(date)
                    && date.substring(0, 2).toInt() > MAXIMUM_DAY -> {
                onIfNoSuchDay()
            }
            Regex(pattern = "$EXCESS_DAY_DIGITS_PATTERN$SPLITTER_PATTERN")
                .containsMatchIn(date) -> {
                shiftExcessDayNumberAfterSlash()
            }
            (lastText.length - date.length) == 1
                    && date.length > 1
                    && lastText.endsWith(SLASH) -> {
                removeSlashAndNumberBeforeSlash()
            }
            Regex(DAY_LESS_THAN_TEN_PATTERN).containsMatchIn(date) -> {
                addZeroBeforeDayIfLessThanTen()
            }
            Regex(pattern = DAY_PATTERN).matches(date) && !isSlashAdded -> {
                addSlashAfterDay()
            }
            // month
            Regex(pattern = EXCESS_SYMBOLS_AFTER_DAY_PATTERN).containsMatchIn(date) -> {
                onIfExcessCharacterAfterDay()
            }
            Regex(pattern = EXCESS_ZEROS_IN_MONTH_PATTERN).containsMatchIn(date) -> {
                onIfMonthHasExcessZeros()
            }
            Regex(pattern = NULLABLE_MONTH_PATTERN).containsMatchIn(date) -> {
                onIfMonthNullable()
            }
            Regex(pattern = MONTH_PATTERN).containsMatchIn(date)
                    && date.substring(3, 5).toInt() > MAXIMUM_MONTH -> {
                onExcessMonthsInYear()
            }
            Regex(pattern = MONTH_LESS_THAN_TEN_PATTERN).matches(date) -> {
                addZeroBeforeMonthIfLessThanTen()
            }
            Regex(pattern = DAY_WITH_MONTH_PATTERN).matches(date) && !isSlashAdded -> {
                addSlashAfterMonth()
            }
            Regex(pattern = EXCESS_MONTH_DIGITS_AFTER_SLASH_PATTERN).containsMatchIn(date) -> {
                shiftExcessMonthNumberAfterSlash()
            }
            // year
            Regex(pattern = EXCESS_SYMBOLS_AFTER_MONTH_PATTERN).containsMatchIn(date) -> {
                onIfExcessCharacterAfterMonth()
            }
            Regex(pattern = YEAR_START_WITH_ZERO_PATTERN).containsMatchIn(date) -> {
                onIfYearStartsWithZero()
            }

            Regex(pattern = YEAR_ENDS_WITH_SYMBOL_PATTERN).containsMatchIn(date) -> {
                onIfCharacterInYearEnd()
            }
            Regex(pattern = FULL_DATE_PATTERN).matches(date)
                    && date.substring(6, 10).toInt() > MAXIMUM_YEAR -> {
                onNoSuchYear()
            }
            Regex(pattern = EXCESS_YEAR_DIGITS_PATTERN).containsMatchIn(date) -> {
                onIfExcessDigitsInYear()
            }
            isWarned -> {
                onIfWarned()
            }
            !isWarned -> {
                onIfNotWarned()
            }
        }
    }

    private fun onIfStartsWithCharacter() {
        editTextWrapper.error = getMessageFromResources(R.string.beginning_with_symbol_warning)
        isWarned = true
        editText.setText("")
    }

    private fun onIfDayHasExcessZeros() {
        editTextWrapper.error = getMessageFromResources(R.string.day_format_warning)
        isWarned = true
        editText.setText(ZERO)
        editText.setSelection(editText.length())
    }

    private fun onIfDayNullable() {
        editTextWrapper.error = getMessageFromResources(R.string.nullable_day_warning)
        isWarned = true
        editText.setText(ZERO)
        editText.setSelection(editText.length())
    }

    private fun onIfNoSuchDay() {
        editTextWrapper.error = getMessageFromResources(R.string.absent_day_warning)
        isWarned = true
        if (date.length > 2) {
            val newDate = date.substring(0, 2)
            lastText = newDate
            editText.setText(newDate)
            editText.setSelection(editText.length())
        } else{
            lastText = date
        }
    }

    private fun shiftExcessDayNumberAfterSlash() {
        val newDate = date.substring(0, 2) + "$SLASH${date.elementAt(2)}"
        isSlashAdded = true
        editText.setText(newDate)
        editText.setSelection(editText.length())
    }

    private fun removeSlashAndNumberBeforeSlash() {
        val newDate = date.substring(0, date.length - 1)
        lastText = newDate
        editText.setText(newDate)
        editText.setSelection(editText.length())
    }

    private fun addZeroBeforeDayIfLessThanTen() {
        val newDate = "$ZERO${date.substring(0, 1)}$SLASH"
        lastText = newDate
        editText.setText(newDate)
        editText.setSelection(editText.length())
    }

    private fun addSlashAfterDay() {
        val newDate = "$date$SLASH"
        isSlashAdded = true
        isWarned = false
        lastText = newDate
        editText.setText(newDate)
        editText.setSelection(editText.length())
    }

    private fun onIfExcessCharacterAfterDay() {
        editTextWrapper.error = getMessageFromResources(R.string.type_month_warning)
        isWarned = true
        removeLastCharacterAndSetCaretAtEnd()
    }

    private fun onIfMonthHasExcessZeros() {
        editTextWrapper.error = getMessageFromResources(R.string.month_format_warning)
        isWarned = true
        removeLastCharacterAndSetCaretAtEnd()
    }

    private fun onIfMonthNullable() {
        val temporary = date.replace(Regex(SPLITTER_PATTERN), SLASH)
        val newDate = date.substring(0, temporary.indexOf(SLASH, 3))
        editTextWrapper.error = getMessageFromResources(R.string.nullable_month_warning)
        isWarned = true
        editText.setText(newDate)
        editText.setSelection(editText.length())
    }

    private fun onExcessMonthsInYear() {
        editTextWrapper.error = getMessageFromResources(R.string.absent_month_warning)
        isWarned = true
        if (date.length > 5) {
            lastText = date.substring(0, 5)
            editText.setText(date.substring(0, 5))
            editText.setSelection(editText.length())
        } else {
            lastText = date
        }
    }

    private fun addZeroBeforeMonthIfLessThanTen() {
        val newDate = date.substring(0, 3) + ZERO + date.substring(3, 4) + SLASH
        lastText = newDate
        editText.setText(newDate)
        editText.setSelection(editText.length())

    }

    private fun addSlashAfterMonth() {
        val newDate = "$date$SLASH"
        isSlashAdded = true
        isWarned = false
        lastText = newDate
        editText.setText(newDate)
        editText.setSelection(editText.length())
    }

    private fun shiftExcessMonthNumberAfterSlash() {
        val newDate = if (date.endsWith(SLASH)) {
            date.substring(0, date.length - 2) + "$SLASH${date.elementAt(date.length - 2)}"
        } else {
            date.substring(0, date.length - 1) + "$SLASH${date.elementAt(date.length - 1)}"
        }
        isSlashAdded = true
        lastText = newDate
        editText.setText(newDate)
        editText.setSelection(editText.length())
    }

    private fun onIfExcessCharacterAfterMonth() {
        editTextWrapper.error = getMessageFromResources(R.string.type_year_warning)
        isWarned = true
        removeLastCharacterAndSetCaretAtEnd()
    }

    private fun onIfYearStartsWithZero() {
        editTextWrapper.error = getMessageFromResources(R.string.year_begins_from_zero_warning)
        isWarned = true
        removeLastCharacterAndSetCaretAtEnd()
    }

    private fun onIfCharacterInYearEnd() {
        editTextWrapper.error = getMessageFromResources(R.string.no_required_symbol_warning)
        isWarned = true
        removeLastCharacterAndSetCaretAtEnd()
    }

    private fun onNoSuchYear() {
        editTextWrapper.error = getMessageFromResources(R.string.no_existing_year_warning)
        isWarned = true
        lastText = date
    }

    private fun onIfExcessDigitsInYear() {
        editTextWrapper.error = getMessageFromResources(R.string.excess_digits_in_year_warning)
        isWarned = true
        removeLastCharacterAndSetCaretAtEnd()
    }

    private fun onIfWarned() {
        isWarned = false
        if ((lastText.length - date.length) == 1) {
            editTextWrapper.error = null
        }
    }

    private fun onIfNotWarned() {
        if (date.length <= 2) {
            isSlashAdded = false
        } else if (!date.substring(3).contains(Regex(SPLITTER_PATTERN))) {
            isSlashAdded = false
        }
        editTextWrapper.error = null
        lastText = date
    }

    private fun removeLastCharacterAndSetCaretAtEnd() {
        editText.setText(date.substring(0, date.length - 1))
        editText.setSelection(editText.length())
    }

    private fun getMessageFromResources(stringId: Int): String  {
        return context.resources.getString(stringId)
    }
}