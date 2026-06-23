package com.kenji.food.tracker.ui.component.input

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun FormNumberField(
    value: Int?,
    @StringRes label: Int,
    @DrawableRes iconRes: Int? = null,
    required: Boolean = false,
    onValueChange: (String) -> Unit
) = FormNumberField(
    value = value,
    label = stringResource(label),
    iconRes = iconRes,
    required = required,
    onValueChange = onValueChange
)

@Composable
fun FormNumberField(
    value: Int?,
    label: String,
    @DrawableRes iconRes: Int? = null,
    required: Boolean = false,
    onValueChange: (String) -> Unit
) = FormTextField(
    value = value?.toString() ?: "",
    label = label,
    iconRes = iconRes,
    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
    required = required,
    onValueChange = onValueChange
)
