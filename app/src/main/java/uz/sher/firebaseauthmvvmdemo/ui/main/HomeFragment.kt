package uz.sher.firebaseauthmvvmdemo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.sher.firebaseauthmvvmdemo.databinding.FragmentHomeBinding
import uz.sher.firebaseauthmvvmdemo.viewmodel.AuthViewModel


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val viewModel: AuthViewModel by viewModels()

    private val binding get() = _binding!!
    private var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        name = arguments?.getString("name").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textWelcome.text = "WELCOME: $name"



        binding.homeLogOut.setOnClickListener {
            viewModel.logOut()
            findNavController().popBackStack()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}