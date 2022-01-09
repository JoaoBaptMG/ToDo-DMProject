package com.joaobapt.todo.user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import coil.load
import coil.transform.CircleCropTransformation
import com.joaobapt.todo.R
import com.joaobapt.todo.databinding.ActivityUserInfoBinding
import java.io.File

class UserInfoActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityUserInfoBinding
    
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { accepted ->
        if (accepted) launchCamera()
        else showExplanation()
    }
    
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            val tmpFile = File.createTempFile("avatar", "jpeg")
            tmpFile.outputStream().use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
            }
            handleImage(tmpFile.toUri())
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        _binding = ActivityUserInfoBinding.inflate(layoutInflater)
        with(_binding) {
            avatarImageView.load("https://goo.gl/gEgYUd") {
                transformations(CircleCropTransformation())
            }
            
            takePictureButton.setOnClickListener { launchCameraWithPermission() }
            setContentView(root)
        }
    }
    
    private fun launchCameraWithPermission() {
        val camPermission = Manifest.permission.CAMERA
        val permissionStatus = checkSelfPermission(camPermission)
        val isAlreadyAccepted = permissionStatus == PackageManager.PERMISSION_GRANTED
        val isExplanationNeeded = shouldShowRequestPermissionRationale(camPermission)
        
        if (isAlreadyAccepted) launchCamera()
        else if (isExplanationNeeded) showExplanation()
        else cameraPermissionLauncher.launch(getString(R.string.camera_permission_reason))
    }
    
    private fun showExplanation() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.camera_permission_reason))
            .setPositiveButton(getString(R.string.accept)) { _, _ -> launchAppSettings() }
            .setNegativeButton(getString(R.string.decline)) { dialog, _ -> dialog.dismiss() }
            .show()
    }
    
    private fun launchAppSettings() {
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                             Uri.fromParts("package", packageName, null)))
    }
    
    private fun launchCamera() { cameraLauncher.launch(null) }
    
    private fun handleImage(imageUri: Uri) {
    
    }
}