package com.kenji.food.tracker.ui.component.input

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kenji.food.tracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(query: String, onSearch: (String) -> Unit) {
    SearchBarDefaults.InputField(
        query = query,
        placeholder = { Text(stringResource(R.string.search)) },
        onQueryChange = onSearch,
        onSearch = onSearch,
        expanded = false,
        onExpandedChange = {}
    )
}