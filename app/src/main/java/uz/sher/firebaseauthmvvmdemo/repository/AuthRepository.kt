package uz.sher.firebaseauthmvvmdemo.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import uz.sher.firebaseauthmvvmdemo.util.network.NetworkHelper
import uz.sher.firebaseauthmvvmdemo.util.resource.Resource
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth, private val networkHelper: NetworkHelper
) {
    private var result: MutableLiveData<Resource<FirebaseUser>> =
        MutableLiveData(Resource.Loading())

    fun login(email: String, password: String): MutableLiveData<Resource<FirebaseUser>> {

        if(networkHelper.isNetworkConnected()){
            try {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) result.postValue(Resource.Success(it.result.user!!))
                    else result.postValue(
                        Resource.Failure(it.exception?.message.toString()
                            .ifEmpty { "Information error" })
                    )

                }
            } catch (e: Exception) {
                e.printStackTrace()
                result.postValue(Resource.Failure(e.message.toString()))
            }
        }else{
            result.postValue(Resource.Failure("Please connect to the internet"))
        }





        return result
    }

    fun signup(

        email: String, password: String
    ): MutableLiveData<Resource<FirebaseUser>> {
        if (networkHelper.isNetworkConnected()){
            try {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) result.postValue(Resource.Success(it.result.user!!))
                    else {
                        result.postValue(Resource.Failure(it.exception?.message.toString()))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                result.postValue(Resource.Failure(e.message.toString()))
            }
        }else{
            result.postValue(Resource.Failure("Please connect to the internet"))
        }



        return result
    }


    fun signInWithGoogle(idToken: String): MutableLiveData<Resource<FirebaseUser>> {
        if(networkHelper.isNetworkConnected()){
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = auth.currentUser
                        result.postValue(Resource.Success(user!!))

                    } else {
                        result.postValue(Resource.Failure(it.exception?.message.toString()))
                    }


                }
            } catch (e: Exception) {
                e.printStackTrace()
                result.postValue(Resource.Failure(e.message.toString()))
            }
        }else{
            result.postValue(Resource.Failure("Please connect to the internet"))
        }



         return result
    }

    fun logout() {
        auth.signOut()
    }


}