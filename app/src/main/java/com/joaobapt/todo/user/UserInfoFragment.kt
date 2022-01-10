package com.joaobapt.todo.user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import com.google.modernstorage.mediastore.FileType
import com.google.modernstorage.mediastore.MediaStoreRepository
import com.google.modernstorage.mediastore.SharedPrimary
import com.joaobapt.todo.R
import com.joaobapt.todo.databinding.FragmentUserInfoBinding
import com.joaobapt.todo.network.Api
import com.joaobapt.todo.setNavigationResult
import kotlinx.coroutines.launch
import java.util.*

class UserInfoFragment : Fragment() {
    private lateinit var binding: FragmentUserInfoBinding
    private lateinit var pictureUri: Uri
    
    private val mediaStore by lazy { MediaStoreRepository(context!!) }
    private val viewModel: UserInfoViewModel by viewModels()
    
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { results ->
        val cameraAccepted = results[Manifest.permission.CAMERA] ?: false
        val writeAccepted = results[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: false
        if (cameraAccepted && writeAccepted) launchCamera()
        else showExplanation()
    }
    
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()) { accepted ->
        if (accepted) handleImage()
        else Snackbar.make(binding.root, getString(R.string.user_avatar_take_picture_failed),
                           Snackbar.LENGTH_LONG).show()
    }
    
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            pictureUri = uri
            handleImage()
        }
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        with(binding) {
            uploadImageButton.setOnClickListener { galleryLauncher.launch("image/*") }
            takePictureButton.setOnClickListener { launchCameraWithPermission() }
            userConfirmButton.setOnClickListener { updateUser() }
            
            userLogoutButton.setOnClickListener {
                Api.eraseToken()
                findNavController().clearBackStack(
                    R.id.action_userInfoFragment_to_authenticationFragment)
            }
        }
        
        return binding.root
    }
    
    override fun onResume() {
        super.onResume()
        
        viewModel.getInfo()
        
        lifecycleScope.launch {
            viewModel.userInfo.collect {
                binding.avatarImageView.load(it?.avatar) {
                    error(R.drawable.ic_launcher_background)
                    transformations(CircleCropTransformation())
                }
    
                if (it != null) {
                    with(binding) {
                        userFirstNameEdit.setText(it.firstName)
                        userLastNameEdit.setText(it.lastName)
                        userEmailEdit.setText(it.email)
                    }
                }
            }
        }
    }
    
    private fun updateUser() {
        with(binding) {
            val newUser = UserInfo(
                firstName = userFirstNameEdit.text.toString(),
                lastName = userLastNameEdit.text.toString(),
                email = userEmailEdit.text.toString()
            )
            
            viewModel.update(newUser)
        }
    }
    
    private fun launchCameraWithPermission() {
        val camPermission = Manifest.permission.CAMERA
        val storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        
        val permissionStatus = context?.checkSelfPermission(camPermission) ?: false
        val isAlreadyAccepted = permissionStatus == PackageManager.PERMISSION_GRANTED
        val isExplanationNeeded = shouldShowRequestPermissionRationale(camPermission)
        
        if (mediaStore.canWriteSharedEntries() && isAlreadyAccepted) launchCamera()
        else if (isExplanationNeeded) showExplanation()
        else permissionLauncher.launch(arrayOf(camPermission, storagePermission))
    }
    
    private fun showExplanation() {
        AlertDialog.Builder(context ?: return)
            .setMessage(getString(R.string.camera_permission_reason))
            .setPositiveButton(getString(R.string.accept)) { _, _ -> launchAppSettings() }
            .setNegativeButton(getString(R.string.decline)) { dialog, _ -> dialog.dismiss() }
            .show()
    }
    
    private fun launchAppSettings() {
        val myActivity = activity ?: return
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                             Uri.fromParts("package", myActivity.packageName, null)))
    }
    
    private fun launchCamera() {
        lifecycleScope.launch {
            pictureUri = mediaStore.createMediaUri(
                "picture-${UUID.randomUUID()}.jpg",
                FileType.IMAGE, SharedPrimary
            ).getOrThrow()
            cameraLauncher.launch(pictureUri)
        }
    }
    
    private fun handleImage() {
        val myActivity = activity ?: return
        val stream = myActivity.contentResolver.openInputStream(pictureUri)
        
        if (stream != null) {
            viewModel.updateAvatar(stream)
    
            binding.avatarImageView.load(pictureUri) {
                error(R.drawable.ic_launcher_background)
                transformations(CircleCropTransformation())
            }
        }
    }
}