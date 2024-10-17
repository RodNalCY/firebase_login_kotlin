package com.rodnal.dev.login_google

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class MainActivity : AppCompatActivity() {

    companion object {
        private const val GOOGLE_SIGN_IN = 9001
        private const val FACEBOOK_SIGN_IN = 64206

    }

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        FacebookSdk.sdkInitialize(applicationContext)
        callbackManager = CallbackManager.Factory.create()

        ///////////////////////////////////////////GOOGLE///////////////////////////////////////////
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // The user is already signed in, navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // finish the current activity to prevent the user from coming back to the SignInActivity using the back button
        }

        val signInButton = findViewById<Button>(R.id.google_login_button)
        signInButton.apply {
            setOnClickListener {
                signIn()
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////

        // Initialize Facebook Login button

        /*val buttonFacebookLogin = findViewById<LoginButton>(R.id.facebook_login_button_sdk)
        buttonFacebookLogin.setReadPermissions("email", "public_profile")

        buttonFacebookLogin.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)
                println("------------------------------------------------")
                println("facebook:onSuccess:$loginResult")
                println("------------------------------------------------")
            }

            override fun onCancel() {
                println("------------------------------------------------")
                println("facebook:onCancel")
                println("------------------------------------------------")
            }

            override fun onError(error: FacebookException) {
                println("------------------------------------------------")
                println("facebook:onSuccess:$error")
                println("------------------------------------------------")
            }
        })*/

        // Botón personalizado para login con Facebook
        val buttonFacebookLogin2 = findViewById<AppCompatButton>(R.id.facebook_login_button_custom)
        buttonFacebookLogin2.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))

            // Maneja el callback del login
            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    // Si el login es exitoso, maneja el token
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Toast.makeText(this@MainActivity, "Login cancelado", Toast.LENGTH_SHORT).show()
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(this@MainActivity, "Error de login: ${error.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    /*
     * ******************************************************
     *  LOGIN WITH FACEBOOK
     * ******************************************************
     * */
    private fun handleFacebookAccessToken(token: AccessToken) {
        println("------------------------------------------------")
        println("handleFacebookAccessToken:$token")
        println("------------------------------------------------")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    println("------------------------------------------------")
                    println("signInWithCredential:success")
                    println("------------------------------------------------")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    println("------------------------------------------------")
                    println("signInWithCredential:failure")
                    println("------------------------------------------------")
                    updateUI(null)
                }
            }
    }
    private fun updateUI(user: FirebaseUser?) {
        println("------------------------------------------------")
        println("updateUI ${user}")
        println("------------------------------------------------")
        if (user != null) {
            // Crear intent para abrir la segunda Activity
            /*val intent = Intent(this, FacebookHomeActivity::class.java)
            intent.putExtra("userName", user.displayName)
            intent.putExtra("userEmail", user.email)
            startActivity(intent)*/
            Toast.makeText(this, "Bienvenido: ${user.displayName}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))
            finish() // Termina la actividad actual para que no se pueda volver atrás
        }
    }

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }*/

    /*
     * ******************************************************
     *  LOGIN WITH GOOGLE
     * ******************************************************
     * */

    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("------------------------------------")
        println("${requestCode} = ${GOOGLE_SIGN_IN}")
        println("${requestCode} = ${FACEBOOK_SIGN_IN}")
        println("------------------------------------")
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!, account)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }else if (requestCode == FACEBOOK_SIGN_IN){
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String, account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{
            // Verifica si el inicio de sesión con Google fue exitoso
            if(it.isSuccessful){
                try {
                    // Obtenemos los datos del Usuario de Google
                    val name  = account.displayName
                    val email = account.email
                    val token = account.idToken
                    println("---------------------------------------------")
                    println(name)
                    println(email)
                    println(token)
                    println("---------------------------------------------")

                    Toast.makeText(this, "Bienvenido: ${name}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()

                }catch (e:Exception){
                    Toast.makeText(this, "Error Inesperado", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                println("Exception login google: ${it.exception}")
                Toast.makeText(this, "Usuario no existe", Toast.LENGTH_SHORT).show()
            }
        }
    }
}