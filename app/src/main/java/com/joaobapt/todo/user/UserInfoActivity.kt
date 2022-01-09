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
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.joaobapt.todo.R
import com.joaobapt.todo.databinding.ActivityUserInfoBinding
import com.joaobapt.todo.network.Api
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserInfoBinding
    
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
        
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        with(binding) {
            takePictureButton.setOnClickListener { launchCameraWithPermission() }
            setContentView(root)
        }
        
        lifecycleScope.launch {
            val userInfo = Api.userWebService.getInfo().body()
            binding.avatarImageView.load(userInfo?.avatar) {
                transformations(CircleCropTransformation())
                error(R.drawable.ic_launcher_background)
            }
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
    
    private fun launchCamera() {
        cameraLauncher.launch(null)
    }
    
    private fun handleImage(imageUri: Uri) {
        lifecycleScope.launch { Api.userWebService.updateAvatar(convert(imageUri)) }
        binding.avatarImageView.load(imageUri) {
            error(R.drawable.ic_launcher_background)
            transformations(CircleCropTransformation())
        }
    }
    
    private fun convert(uri: Uri): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name = "avatar", filename = "temp.jpeg",
            body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
        )
    }
}