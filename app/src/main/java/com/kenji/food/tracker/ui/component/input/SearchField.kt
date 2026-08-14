package com.kenji.food.tracker.ui.component.input

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(query: String, @StringRes placeholder: Int, onSearch: (String) -> Unit) {
    SearchBarDefaults.InputField(
        query = query,
        placeholder = { Text(stringResource(placeholder)) },
        onQueryChange = onSearch,
        onSearch = onSearch,
        expanded = false,
        onExpandedChange = {}
    )
}