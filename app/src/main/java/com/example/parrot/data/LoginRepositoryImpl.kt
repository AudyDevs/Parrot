package com.example.parrot.data

import android.app.Activity
import android.content.Intent
import com.example.parrot.R
import com.example.parrot.core.type.ProviderType
import com.example.parrot.domain.LoginRepository
import com.example.parrot.domain.model.LoginModel
import com.example.parrot.domain.state.LoginState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val GOOGLE_REQUEST_CODE_SIGN_IN = 100

class LoginRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : LoginRepository {

    override suspend fun loginWithEmail(email: String, password: String): LoginState {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            LoginState.Success
        } catch (e: FirebaseAuthException) {
            LoginState.Error
        }
    }

    override suspend fun signupWithEmail(email: String, password: String): LoginState {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            LoginState.Success
        } catch (e: FirebaseAuthException) {
            LoginState.Error
        }
    }

    override suspend fun logoutWithEmail(): LoginState {
        return try {
            firebaseAuth.signOut()
            LoginState.Success
        } catch (e: FirebaseAuthException) {
            LoginState.Error
        }
    }

    override suspend fun loginWithGoogle(activity: Activity) {
        val googleOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(activity, googleOptions)
        googleSignInClient.signOut()
        activity.startActivityForResult(
            googleSignInClient.signInIntent,
            GOOGLE_REQUEST_CODE_SIGN_IN
        )
    }

    //onActivityResult
    override suspend fun signupWithGoogle(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ): LoginModel {
        if (requestCode == GOOGLE_REQUEST_CODE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    firebaseAuth.signInWithCredential(credential).await()
                    return LoginModel(email = account.email ?: "", provider = ProviderType.Google)
                }
            } catch (e: ApiException) {
                return LoginModel(email = "", provider = ProviderType.Google)
            }

        }
        return LoginModel(email = "", provider = ProviderType.Google)
    }
}