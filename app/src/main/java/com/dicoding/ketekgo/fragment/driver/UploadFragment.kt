package com.dicoding.ketekgo.fragment.driver

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dicoding.ketekgo.R
import com.dicoding.ketekgo.databinding.FragmentUploadBinding
import com.dicoding.ketekgo.isLoading
import com.dicoding.ketekgo.rotateBitmap
import com.dicoding.ketekgo.uriToFile
import com.dicoding.ketekgo.viewmodel.UserViewModel
import com.dicoding.ketekgo.viewmodel.ViewModelFactory
import java.io.File

class UploadFragment : Fragment() {
    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding
    private var getFile: File? = null
    private var image: Bitmap? = null
    private val viewModel: UserViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isLoading(false, binding?.progressUpload!!)
        binding?.btnCamera?.setOnClickListener { startCameraX() }
        binding?.btnGallery?.setOnClickListener { startGallery() }
        binding?.btnUpload?.setOnClickListener { uploadImage() }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        viewModel.cameraCode.observe(viewLifecycleOwner) { code ->
            if (code == CAMERA_X_RESULT) {
                viewModel.filePhoto.observe(viewLifecycleOwner) { file ->
                    getFile = file
                    viewModel.cameraSelector.observe(viewLifecycleOwner) { camera ->
                        val result = rotateBitmap(
                            BitmapFactory.decodeFile(file.path),
                            camera
                        )
                        image = result
                        binding?.imgUploaded?.setImageBitmap(result)
                    }
                }
            }
        }
    }

    private fun uploadImage() {
        TODO("Not yet implemented")
    }

    private fun startCameraX() {
        findNavController().navigate(R.id.action_uploadFragment_to_cameraFragment)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, resources.getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireActivity().baseContext,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        @Suppress("DEPRECATION")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.permission_not_granted),
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().finish()
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri? = result.data?.data
            if (selectedImg != null) {
                handleSelectedImage(selectedImg)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Failed to retrieve image from gallery",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Gallery activity result was not OK",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun handleSelectedImage(selectedImg: Uri) {
        val myFile = uriToFile(selectedImg, requireContext())
        getFile = myFile

        try {
            val bitmap = BitmapFactory.decodeStream(
                requireContext().contentResolver.openInputStream(selectedImg)
            )
            image = bitmap
            binding?.imgUploaded?.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Failed to decode the selected image",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}