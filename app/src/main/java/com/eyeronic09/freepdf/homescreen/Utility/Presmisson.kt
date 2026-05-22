package com.eyeronic09.freepdf.homescreen.Utility

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat

@Composable
fun PermissionHandler(
    onPermissionGranted: () -> Unit,
) {
    val context = LocalContext.current
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if (Environment.isExternalStorageManager()) {
            onPermissionGranted()
        } else {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                data = Uri.fromParts("package", "${context.packageName}", null)
            }
            context.startActivity(intent)
        }
    } else {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        val isPermissionGranted = ActivityCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED

        if (isPermissionGranted) {
            onPermissionGranted()
        } else {
            val requestLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted ->
            if (isGranted) {
                onPermissionGranted()
            }
                Log.d("IsGranted", isGranted.toString())
            }
            LaunchedEffect(Unit) {
                requestLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }
}