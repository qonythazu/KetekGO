package com.dicoding.ketekgo.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.dicoding.ketekgo.R
import com.dicoding.ketekgo.activity.MainActivity
import com.dicoding.ketekgo.data.repository.Result
import com.dicoding.ketekgo.databinding.FragmentLoginBinding
import com.dicoding.ketekgo.isLoading
import com.dicoding.ketekgo.viewmodel.AuthModel
import com.dicoding.ketekgo.viewmodel.ViewModelFactory

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegisterHere.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registerFragment)
        )

        val loginButton = binding.btnLogin
        loginButton.setOnClickListener{
            val emailTxt = binding.edEmailLogin.text.toString()
            val passwordTxt = binding.edPasswordLogin.text.toString()
            viewModel.authLogin(emailTxt, passwordTxt).observe(requireActivity()) {result ->
                when (result) {
                    is Result.Loading -> {
                        isLoading(true, binding.progressLogin)
                    }
                    is Result.Success -> {
                        moveMainActivity()
                    }
                    is Result.Error -> {
                        isLoading(false, binding.progressLogin)
                        Toast.makeText(
                            context,
                            "${resources.getString(R.string.error_message)}, ${result.error}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun moveMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        requireActivity().finish()
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
