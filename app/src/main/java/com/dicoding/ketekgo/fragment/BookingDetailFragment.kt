package com.dicoding.ketekgo.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dicoding.ketekgo.activity.MainActivity
import com.dicoding.ketekgo.databinding.FragmentBookingDetailBinding
import com.dicoding.ketekgo.dataclass.Ketek

class BookingDetailFragment : Fragment() {
    private var _binding: FragmentBookingDetailBinding? = null
    private val binding get() = _binding!!
    private var basePrice = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as? MainActivity

        mainActivity?.hideBottomNavigationBar()

        val selectedItem: Ketek? = arguments?.getParcelable(ITEM)

        selectedItem?.let {
            setDisplay(it)
            basePrice = it.price.toString()
        }

        binding.edPassengers.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(textView: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(textView: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(textView: Editable?) {
                val enteredText = textView.toString().takeIf { it.isNotBlank() }
                val priceInteger = enteredText?.split(".")?.firstOrNull()?.toIntOrNull() ?: 0
                changeButtonPrice(priceInteger)
            }

        })

        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
            mainActivity?.showBottomNavigationBar()
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

    companion object {
        const val ITEM = "selectedItem"
    }
}