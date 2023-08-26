package uz.sher.firebaseauthmvvmdemo.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.sher.firebaseauthmvvmdemo.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {


    fun loginUser(email: String, password: String) =
        authRepository.login(email, password)

    fun signUpUser(email: String, password: String) = authRepository.signup(email, password)

    fun signInWithGOOGle(idToken:String)=authRepository.signInWithGoogle(idToken)

    fun logOut()= authRepository.logout()


}