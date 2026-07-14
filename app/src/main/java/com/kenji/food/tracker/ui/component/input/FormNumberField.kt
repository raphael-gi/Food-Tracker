package com.kenji.food.tracker.ui.component.input

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.kenji.food.tracker.util.Formatter

@Composable
fun <T : Number> FormNumberField(
    modifier: Modifier = Modifier,
    value: T?,
    @StringRes label: Int,
    @DrawableRes iconRes: Int? = null,
    required: Boolean = false,
    onPressDone: () -> Unit = {},
    onValueChange: (String) -> Unit
) = FormNumberField(
    modifier = modifier,
    value = value,
    label = stringResource(label),
    iconRes = iconRes,
    required = required,
    onPressDone = onPressDone,
    onValueChange = onValueChange
)

@Composable
fun <T : Number> FormNumberField(
    modifier: Modifier = Modifier,
    value: T?,
    label: String,
    @DrawableRes iconRes: Int? = null,
    required: Boolean = false,
    onPressDone: () -> Unit = {},
    onValueChange: (String) -> Unit
) {
    var displayValue by rememberSaveable { mutableStateOf(formatValue(value)) }

    FormTextField(
        modifier = modifier,
        value = displayValue,
        label = label,
        iconRes = iconRes,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        required = required,
        onPressDone = onPressDone,
        onValueChange = { input ->
            displayValue = input
            onValueChange(input)
        }
    )
}

private fun formatValue(value: Number?): String {
    return when (value) {
        null -> ""
        is Int -> value.toString()
        is Double -> Formatter.formatDecimal(value)
        else -> value.toString()
    }
}
