package uz.sher.firebaseauthmvvmdemo.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import uz.sher.firebaseauthmvvmdemo.R
import uz.sher.firebaseauthmvvmdemo.databinding.FragmentSignUpBinding

import uz.sher.firebaseauthmvvmdemo.util.resource.Resource
import uz.sher.firebaseauthmvvmdemo.viewmodel.AuthViewModel

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.signUpSignInText.setOnClickListener{
            findNavController().popBackStack()
        }



        binding.signUpBtn.setOnClickListener {
            val email = binding.signUpEmail.text.toString().trim()
            val password = binding.signUpPassword.text.toString().trim()
            val confPassword = binding.signUpConfPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && password.length > 5) {
                if (confPassword == password) {

                    viewModel.signUpUser(email, password).observe(this) {
                        when (it) {
                            is Resource.Loading -> {
                                binding.signUpProgressBar.visibility = View.VISIBLE
                            }
                            is Resource.Failure -> {
                                binding.signUpProgressBar.visibility = View.GONE
                                Toast.makeText(
                                    binding.root.context,
                                    it.t,
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                            is Resource.Success -> {
                                binding.signUpProgressBar.visibility = View.GONE
                                Toast.makeText(binding.root.context, "Success", Toast.LENGTH_SHORT)
                                    .show()

                                val bundle=Bundle()
                                bundle.putString("email",email)

                                findNavController().navigate(R.id.action_signUpFragment_to_signInFragment,bundle)

                            }
                        }
                    }

                } else
                    Toast.makeText(
                        binding.root.context,
                        "Passwords do not match",
                        Toast.LENGTH_SHORT
                    ).show()

            } else
                Toast.makeText(
                    binding.root.context, "Please fill in the fields", Toast.LENGTH_SHORT
                ).show()


        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}