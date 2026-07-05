package com.kenji.food.tracker.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kenji.food.tracker.R

@Composable
fun BarcodeScannedInfo(onDelete: () -> Unit) {
    val swipeToDismissState = rememberSwipeToDismissBoxState()

    val shape = RoundedCornerShape(8.dp)

    SwipeToDismissBox(
        state = swipeToDismissState,
        enableDismissFromEndToStart = false,
        onDismiss = { onDelete() },
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.error,
                        shape = shape
                    )
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    shape = shape
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(
                    painter = painterResource(R.drawable.scan),
                    contentDescription = stringResource(R.string.scan)
                )

                Text(stringResource(R.string.barcodeScanned))

                Icon(
                    painter = painterResource(R.drawable.check_circle),
                    contentDescription = stringResource(R.string.check)
                )
            }
        }
    }
}