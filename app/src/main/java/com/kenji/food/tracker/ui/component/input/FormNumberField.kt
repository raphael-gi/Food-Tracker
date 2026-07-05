package com.kenji.food.tracker.ui.component.input

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun FormNumberField(
    modifier: Modifier = Modifier,
    value: Int?,
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
fun FormNumberField(
    modifier: Modifier = Modifier,
    value: Int?,
    label: String,
    @DrawableRes iconRes: Int? = null,
    required: Boolean = false,
    onPressDone: () -> Unit = {},
    onValueChange: (String) -> Unit
) = FormTextField(
    modifier = modifier,
    value = value?.toString() ?: "",
    label = label,
    iconRes = iconRes,
    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
    required = required,
    onPressDone = onPressDone,
    onValueChange = onValueChange
)
