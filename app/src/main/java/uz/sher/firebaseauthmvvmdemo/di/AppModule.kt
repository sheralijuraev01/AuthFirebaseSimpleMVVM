package uz.sher.firebaseauthmvvmdemo.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.sher.firebaseauthmvvmdemo.repository.AuthRepository
import uz.sher.firebaseauthmvvmdemo.util.network.NetworkHelper
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule() {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideAuthFirebase(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, networkHelper: NetworkHelper): AuthRepository =
        AuthRepository(auth, networkHelper)


}