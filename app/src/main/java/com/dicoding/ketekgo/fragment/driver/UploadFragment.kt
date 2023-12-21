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
import android.widget.EditText
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class UploadFragment : Fragment() {
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    private val db = FirebaseFirestore.getInstance()

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
        _binding = FragmentUploadBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameEditText: EditText? = binding?.edNameKetek
        val capacityEditText: EditText? = binding?.edCapacity
        val priceEditText: EditText? = binding?.edPrice
        val placeStartEditText: EditText? = binding?.edPlaceStart
        val placeEndEditText : EditText? = binding?.edPlaceEnd
        val timeEditText: EditText? = binding?.edTime

        isLoading(false, binding?.progressUpload!!)
        binding?.btnCamera?.setOnClickListener { startCameraX() }
        binding?.btnGallery?.setOnClickListener { startGallery() }
        binding?.btnUpload?.setOnClickListener {
            isLoading(true, binding?.progressUpload!!)
            val name = nameEditText?.text.toString()
            val capacity = capacityEditText?.text.toString().toInt()
            val price = priceEditText?.text.toString()
            val placeStart = placeStartEditText?.text.toString()
            val placeEnd = placeEndEditText?.text.toString()
            val time = timeEditText?.text.toString()

            uploadImageToStorage(name, capacity, price, placeStart, placeEnd, time)
        }

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

    private fun uploadImageToStorage(
        name: String,
        capacity: Int,
        price: String,
        placeStart: String,
        placeEnd: String,
        time: String
    ) {
        if (getFile != null) {
            val imageRef = storageReference.child("images/${getFile?.name}")

            val uploadTask = imageRef.putFile(Uri.fromFile(getFile))

            uploadTask.addOnSuccessListener { taskSnapshot ->
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Setelah mendapatkan URL gambar, simpan data ke Firestore
                    saveDataToFirestore(
                        name,
                        uri.toString(),
                        capacity,
                        price,
                        placeStart,
                        placeEnd,
                        time
                    )
                }
            }.addOnFailureListener { e ->
                // Gagal mengunggah gambar
                Toast.makeText(
                    requireContext(),
                    "Failed to upload image to Firebase Storage: $e",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            // File gambar tidak ditemukan
            Toast.makeText(requireContext(), "Image file not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveDataToFirestore(
        name: String,
        photoUrl: String,
        capacity: Int,
        price: String,
        placeStart: String,
        placeEnd: String,
        time: String
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        userId?.let {
            val ketekData = hashMapOf(
                "Name" to name,
                "PhotoUrl" to photoUrl,
                "Capacity" to capacity,
                "Price" to price,
                "PlaceStart" to placeStart,
                "PlaceEnd" to placeEnd,
                "Time" to time
            )

            db.collection("Drivers").document(userId)
                .collection("Keteks")
                .add(ketekData)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Data and image uploaded successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    isLoading(false, binding?.progressUpload!!)
                }
                .addOnFailureListener { e: Exception ->
                    // Gagal menyimpan data ke Firestore
                    Toast.makeText(
                        requireContext(),
                        "Failed to save data to Firestore: $e",
                        Toast.LENGTH_SHORT
                    ).show()
                    isLoading(false, binding?.progressUpload!!)
                }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}