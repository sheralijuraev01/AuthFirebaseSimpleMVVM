package uz.sher.firebaseauthmvvmdemo.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import uz.sher.firebaseauthmvvmdemo.R
import uz.sher.firebaseauthmvvmdemo.databinding.FragmentSignInBinding
import uz.sher.firebaseauthmvvmdemo.util.constants.CONSTANTS.REQ_GOOGLE_CODE
import uz.sher.firebaseauthmvvmdemo.util.resource.Resource
import uz.sher.firebaseauthmvvmdemo.viewmodel.AuthViewModel

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private var email: String? = null

    private lateinit var gso: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient


    private val viewModel: AuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = arguments?.getString("email").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(binding.root.context, gso)


        binding.signInWithGoogle.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, REQ_GOOGLE_CODE)

        }



        if (email != "null") {
            binding.signInEmail.setText(email)

        }

        binding.signInSignUpText.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.signInBtn.setOnClickListener {

            val email = binding.signInEmail.text.toString()
            val password = binding.signInPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && password.length > 5) {
                viewModel.loginUser(email, password).observe(this) {
                    when (it) {
                        is Resource.Loading -> {
                            binding.signInProgressBar.visibility = View.VISIBLE
                        }
                        is Resource.Failure -> {
                            binding.signInProgressBar.visibility = View.GONE
                            Toast.makeText(binding.root.context, it.t, Toast.LENGTH_SHORT)
                                .show()
                        }
                        is Resource.Success -> {
                            binding.signInProgressBar.visibility = View.GONE
                            Toast.makeText(binding.root.context, "Success", Toast.LENGTH_SHORT)
                                .show()

                            val bundle = Bundle()
                            bundle.putString("name", it.data.email)



                            findNavController().navigate(
                                R.id.action_signInFragment_to_homeFragment,
                                bundle
                            )
                        }

                    }

                }

            } else {
                Toast.makeText(
                    binding.root.context,
                    "Please fill in the correct information",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_GOOGLE_CODE) {
            val signedInAccountFromIntent = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = signedInAccountFromIntent.getResult(ApiException::class.java)
                if (account != null) {
                    observeGoogleAccount(account.idToken!!)

                } else {
                    Toast.makeText(binding.root.context, "Account null", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    binding.root.context,
                    e.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }


        }


    }

    private fun observeGoogleAccount(idToken: String) {
        viewModel.signInWithGOOGle(idToken).observe(this) {
            when (it) {
                is Resource.Loading -> {
                    binding.signInProgressBar.visibility = View.VISIBLE
                }
                is Resource.Failure -> {
                    binding.signInProgressBar.visibility = View.GONE
                    Toast.makeText(binding.root.context, it.t, Toast.LENGTH_SHORT)
                        .show()
                }
                is Resource.Success -> {
                    binding.signInProgressBar.visibility = View.GONE
                    Toast.makeText(binding.root.context, "Success", Toast.LENGTH_SHORT)
                        .show()
                    val bundle = Bundle()
                    bundle.putString("name", it.data.displayName)

                    findNavController().navigate(R.id.action_signInFragment_to_homeFragment, bundle)
                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}