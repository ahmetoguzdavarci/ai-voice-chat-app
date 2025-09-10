package com.aod.aivoicechat.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference

object MicPermissionManager {
    private var launcher: ActivityResultLauncher<String>? = null
    private var hostActivityRef: WeakReference<ComponentActivity>? = null
    private var pending: ((granted: Boolean, permanentlyDenied: Boolean) -> Unit)? = null

    val isInitialized: Boolean
        get() = launcher != null && hostActivityRef?.get() != null

    fun init(activity: ComponentActivity) {
        hostActivityRef = WeakReference(activity)
        launcher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            val act = hostActivityRef?.get()
            val permanentlyDenied = !granted && act?.let {
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    it, Manifest.permission.RECORD_AUDIO
                )
            } ?: false
            pending?.invoke(granted, permanentlyDenied)
            pending = null
        }
    }

    fun check(
        onGranted: () -> Unit,
        onDenied: ((permanentlyDenied: Boolean) -> Unit)? = null
    ) {
        val act = hostActivityRef?.get() ?: run {
            onDenied?.invoke(false)
            return
        }
        if (isGranted(act)) onGranted()
        else {
            pending = { granted, permanentlyDenied ->
                if (granted) onGranted() else onDenied?.invoke(permanentlyDenied)
            }
            launcher?.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    fun isGranted(context: Context): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

    fun shouldShowRationale(): Boolean {
        val act = hostActivityRef?.get() ?: return false
        return ActivityCompat.shouldShowRequestPermissionRationale(
            act, Manifest.permission.RECORD_AUDIO
        )
    }

    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    fun clear() {
        launcher = null
        hostActivityRef = null
        pending = null
    }
}