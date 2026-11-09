package com.kenji.food.tracker.ui

import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalResources
import com.kenji.food.tracker.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object SnackBarController {
    private val _events = Channel<Event>(3)
    val events = _events.receiveAsFlow()

    fun sendEvent(event: Event) {
        _events.trySend(event)
    }

    sealed class Event(
        val message: String? = null,
        @StringRes val messageRes: Int? = null,
        val args: List<String?> = emptyList(),
        val actionLabel: String? = null,
        val actionLabelRes: Int? = null,
        val onAction: () -> Unit = {}
    ) {
        data class BarcodeNotFound(val barcode: String? = null, val onClickCreate: () -> Unit) :
            Event(
                messageRes = R.string.barcodeNotFound,
                args = listOf(barcode),
                actionLabelRes = R.string.create,
                onAction = onClickCreate
            )
    }

    @Composable
    fun Show() {
        val snackBarState = remember { SnackbarHostState() }

        val resources = LocalResources.current

        LaunchedEffect(Unit) {
            events.collect { event ->
                val message = event.message ?: event.messageRes?.let {
                    resources.getString(it, event.args.toTypedArray())
                }

                val actionLabel = event.actionLabel ?: event.actionLabelRes?.let {
                    resources.getString(it)
                }

                message?.let {
                    val result = snackBarState.showSnackbar(
                        message = it,
                        withDismissAction = true,
                        actionLabel = actionLabel,
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        event.onAction.invoke()
                    }
                }
            }
        }

        SnackbarHost(hostState = snackBarState)
    }
}