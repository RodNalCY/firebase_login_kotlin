package com.rodnal.dev.login_google

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.content.Intent
import android.widget.Button
import android.widget.TextView
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private lateinit var loginManager: LoginManager // Declaración aquí
    private lateinit var callbackManager: CallbackManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mAuth = FirebaseAuth.getInstance()
        // Inicializa Facebook SDK
        FacebookSdk.sdkInitialize(applicationContext)
        callbackManager = CallbackManager.Factory.create()



        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)



        val txtName = findViewById<TextView>(R.id.name)
        val txtEmail = findViewById<TextView>(R.id.email)

        val auth = Firebase.auth
        val user = auth.currentUser

        if (user != null) {
            val name  = user.displayName
            val email = user.email
            txtName.text = name
            txtEmail.text = email
        } else {
            // Handle the case where the user is not signed in
        }



        // Inside onCreate() method
        val sign_out_button = findViewById<Button>(R.id.logout_button)
        sign_out_button.setOnClickListener {
            signOutAndStartSignInActivity()
        }
    }


    private fun signOutAndStartSignInActivity() {
       /* mAuth.signOut()

        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
            // Optional: Update UI or show a message to the user
            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }*/
        // Cierra sesión de Firebase
        mAuth.signOut()

        // Cierra sesión de Facebook
        LoginManager.getInstance().logOut()

        // Si estás usando Google, cierra sesión de Google
        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
            // Opcional: Actualiza la interfaz de usuario o muestra un mensaje al usuario
            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}