package com.dicoding.ketekgo.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dicoding.ketekgo.R
import com.dicoding.ketekgo.databinding.FragmentBookingDetailBinding
import com.dicoding.ketekgo.dataclass.Booking
import com.dicoding.ketekgo.dataclass.Ketek
import com.dicoding.ketekgo.isLoading
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BookingDetailFragment : Fragment() {
    private lateinit var fStore: FirebaseFirestore
    private var _binding: FragmentBookingDetailBinding? = null
    private val binding get() = _binding!!
    private var basePrice = ""
    private var selectedKetek: Ketek? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedItem: Ketek? = arguments?.getParcelable(ITEM)
        selectedKetek = arguments?.getParcelable(ITEM)

        selectedItem?.let {
            setDisplay(it)
            basePrice = it.price.toString()
        }

        binding.edPassengers.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(textView: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(textView: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(textView: Editable?) {
                val enteredText = textView.toString().takeIf { it.isNotBlank() }
                val maxCapacity = selectedKetek?.capacity ?: 0

                val enteredPassengers = enteredText?.toIntOrNull() ?: 0
                val validPassengers = enteredPassengers.coerceAtMost(maxCapacity)

                if (enteredPassengers != validPassengers) {
                    binding.edPassengers.setText(validPassengers.toString())
                }

                changeButtonPrice(validPassengers)
            }
        })

        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnPay.setOnClickListener {
            showDialog()
        }
    }

    private fun setDisplay(item: Ketek) {
        binding.tvItemKetekName.text = item.name
        binding.ketekTo.text = item.to
        binding.tvTime.text = item.time
        binding.edPassengers.text =
            Editable.Factory.getInstance().newEditable(item.capacity.toString())

        val price = item.price
        val priceString = price?.replace(".", "")?.toInt()
        val totalPrice = priceString?.times(item.capacity!!)
        binding.btnPay.text = totalPrice?.let { formatToRupiah(it) }
    }

    private fun formatToRupiah(value: Int): String {
        val formattedValue = String.format("%,d", value)
        formattedValue.replace(",", ".")

        return "Rp$formattedValue"
    }

    private fun changeButtonPrice(value: Int) {
        val originalPrice = basePrice
        val cleanPrice = originalPrice.replace("[Rp,]".toRegex(), "")
        Log.e("OriPrice", originalPrice)
        Log.e("CleanPrice", cleanPrice)
        val priceString = cleanPrice.replace(".", "").toInt()
        Log.e("PriceString", "$priceString")
        val totalPrice = priceString * value
        binding.btnPay.text = formatToRupiah(totalPrice)
    }

    private fun saveBookingToFirestore(booking: Booking, status: String, date: String) {
        fStore = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val customerRef = fStore.collection("Customers").document(userId)
            val bookingsRef = customerRef.collection("Bookings")
            val bookingData = mapOf(
                "name" to booking.name,
                "destination" to booking.destination,
                "time" to booking.time,
                "passengers" to booking.passengers,
                "ketekId" to booking.ketekId,
                "driverId" to booking.driverId,
                "totalPrice" to booking.totalPrice,
                "status" to status,
                "date" to date,
            )

            bookingsRef.add(bookingData)
                .addOnSuccessListener { documentReference ->
                    val bookingId = documentReference.id
                    val ketekId = booking.ketekId
                    val driverId = booking.driverId

                    val ketekRef = fStore.collection("Drivers").document(ketekId.toString())

                    ketekRef.get()
                        .addOnSuccessListener { _ ->
                            if (driverId != null) {
                                val driverRef = fStore.collection("Drivers").document(driverId)
                                val historyRef = driverRef.collection("History")

                                val historyData = mapOf(
                                    "customerId" to userId,
                                    "customerName" to booking.name,
                                    "driverId" to driverId,
                                    "ketekId" to ketekId,
                                    "ketekName" to booking.name, // Menggunakan name dari booking, sesuaikan jika perlu
                                    "ketekDestination" to booking.destination,
                                    "ketekTime" to booking.time,
                                    "bookingId" to bookingId,
                                    "totalPrice" to booking.totalPrice,
                                    "date" to date
                                )
                                Log.d("DEBUG", "historyData: $historyData")

                                historyRef.add(historyData)
                                    .addOnSuccessListener {
                                        isLoading(false, binding.progressBarBooking)
                                        findNavController().navigate(R.id.historyFragment)
                                        Toast.makeText(
                                            requireContext(),
                                            "Success Booking",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    .addOnFailureListener { e ->
                                        isLoading(false, binding.progressBarBooking)
                                        Toast.makeText(
                                            requireContext(),
                                            "Error: $e",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            } else {
                                isLoading(false, binding.progressBarBooking)
                                Toast.makeText(
                                    requireContext(),
                                    "Driver not found",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        .addOnFailureListener { e ->
                            isLoading(false, binding.progressBarBooking)
                            Toast.makeText(requireContext(), "Error: $e", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { e ->
                    isLoading(false, binding.progressBarBooking)
                    Toast.makeText(requireContext(), "Error: $e", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun showDialog() {
        isLoading(true, binding.progressBarBooking)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmation")
        builder.setMessage("Yakin Nih Mau Booking Ketek Ini?")

        builder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            selectedKetek?.let {
                val name = it.name.toString()
                val destination = it.to.toString()
                val time = it.time.toString()
                val passengers = binding.edPassengers.text.toString().toInt()
                val rawPrice = binding.btnPay.text.toString().replace("[Rp,]".toRegex(), "")
                val cleanPrice = rawPrice.replace(".", "").toInt()
                val status = "Belum dibayar"
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val ketekId = it.ketekId
                val driverId = it.driverId

                val booking = Booking(name, destination, time, passengers, cleanPrice, ketekId, driverId)
                saveBookingToFirestore(booking, status, date)
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            isLoading(false, binding.progressBarBooking)
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        const val ITEM = "selectedItem"
    }
}