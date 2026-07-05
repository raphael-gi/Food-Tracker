package com.kenji.food.tracker.ui.component.input

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.kenji.food.tracker.R
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme

@Composable
fun FormTextField(
    modifier: Modifier = Modifier,
    value: String,
    @StringRes label: Int,
    @DrawableRes iconRes: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    required: Boolean = false,
    onPressDone: () -> Unit = {},
    onValueChange: (String) -> Unit,
) = FormTextField(
    modifier,
    value,
    stringResource(label),
    iconRes,
    keyboardOptions,
    required,
    onPressDone,
    onValueChange
)

@Composable
fun FormTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    @DrawableRes iconRes: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    required: Boolean = false,
    onPressDone: () -> Unit = {},
    onValueChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        isError = required && value.isEmpty(),
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium
            )
        },
        leadingIcon = if (iconRes != null) {
            {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = label
                )
            }
        } else null,
        onValueChange = { onValueChange(it) },
        keyboardOptions = keyboardOptions.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.moveFocus(FocusDirection.Down)
                onPressDone()
            }
        )
    )
}


@Preview
@Composable
private fun FormTextFieldPreview() {
    FoodTrackerTheme {
        Surface {
            FormTextField(
                value = "",
                label = R.string.caloriesLabel,
                required = true,
            ) {}
        }
    }
}
