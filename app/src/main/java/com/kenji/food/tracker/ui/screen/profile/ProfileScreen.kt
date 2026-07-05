package com.kenji.food.tracker.ui.screen.profile

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kenji.food.tracker.R
import com.kenji.food.tracker.entity.FoodTargetEntity
import com.kenji.food.tracker.ui.Route
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.ui.viewmodel.profile.ProfileAction
import com.kenji.food.tracker.ui.viewmodel.profile.ProfileEffect
import com.kenji.food.tracker.ui.viewmodel.profile.ProfileViewModel
import com.kenji.food.tracker.util.Permissions

private val SPACING = 10.dp

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onRoute: (Route) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ProfileEffect.OnUpdateFoodTarget -> onRoute(
                    Route.FoodTarget(
                        calories = effect.foodTarget.calories,
                        proteins = effect.foodTarget.protein,
                        sugar = effect.foodTarget.sugar
                    )
                )
            }
        }
    }

    ProfileScreenContent(
        currentFoodTarget = state.currentFoodTarget,
        barcodeScanningEnabled = state.barcodeScanningEnabled,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ProfileScreenContent(
    currentFoodTarget: FoodTargetEntity?,
    barcodeScanningEnabled: Boolean,
    onAction: (ProfileAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(SPACING),
        verticalArrangement = Arrangement.spacedBy(SPACING)
    ) {
        if (currentFoodTarget != null) {
            CurrentFoodTargetSection(currentFoodTarget, onAction)
            HorizontalDivider()
        }

        CameraPermissionSection(barcodeScanningEnabled, onAction)
    }
}

@Composable
private fun CurrentFoodTargetSection(
    currentFoodTarget: FoodTargetEntity,
    onAction: (ProfileAction) -> Unit
) {
    Column(horizontalAlignment = Alignment.End) {
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(15.dp)) {
                Text(
                    text = stringResource(R.string.foodTarget),
                    style = MaterialTheme.typography.headlineMedium
                )

                FoodTargetProperty(
                    value = currentFoodTarget.calories,
                    icon = R.drawable.calories,
                    iconDescription = R.string.calories
                )
                FoodTargetProperty(
                    value = currentFoodTarget.protein,
                    icon = R.drawable.calories,
                    iconDescription = R.string.protein
                )
                FoodTargetProperty(
                    value = currentFoodTarget.sugar,
                    icon = R.drawable.calories,
                    iconDescription = R.string.sugar
                )
            }
        }

        Text(
            modifier = Modifier.clickable { onAction(ProfileAction.UpdateFoodTarget) },
            text = stringResource(R.string.update),
        )
    }
}

@Composable
private fun CameraPermissionSection(
    barcodeScanningEnabled: Boolean,
    onAction: (ProfileAction) -> Unit
) {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(RequestPermission()) { isGranted ->
        if (isGranted) {
            onAction(ProfileAction.ToggleBarcodePermission)
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = stringResource(R.string.barcodeScanning))

        Switch(
            checked = barcodeScanningEnabled,
            onCheckedChange = {
                if (!barcodeScanningEnabled) {
                    permissionLauncher.launch(Permissions.BARCODE_PERMISSIONS)
                } else {
                    val uri = Uri.fromParts("package", context.packageName, null)
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = uri
                    }

                    context.startActivity(intent)
                }
            }
        )
    }
}

@Composable
private fun FoodTargetProperty(
    value: Int?,
    @DrawableRes icon: Int,
    @StringRes iconDescription: Int
) {
    value?.let {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(icon),
                contentDescription = stringResource(iconDescription),
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    FoodTrackerTheme {
        ProfileScreenContent(
            currentFoodTarget = FoodTargetEntity(
                id = 0,
                calories = 100,
                protein = 50,
                sugar = null
            ),
            barcodeScanningEnabled = true
        ) {}
    }
}
