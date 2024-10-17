package com.rodnal.dev.login_google

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class FacebookHomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_facebook_home)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Obtener los datos del intent
        val userName = intent.getStringExtra("userName")
        val userEmail = intent.getStringExtra("userEmail")

        // Referencias a los TextViews en el layout
        val textViewName = findViewById<TextView>(R.id.txtName)
        val textViewEmail = findViewById<TextView>(R.id.txtEmail)

        // Mostrar los datos en los TextViews
        textViewName.text = userName
        textViewEmail.text = userEmail

        // Referencia al botón de cerrar sesión
        val buttonLogout = findViewById<Button>(R.id.logout_button)

        // Configurar el clic en el botón de cerrar sesión
        buttonLogout.setOnClickListener {
            // Cerrar sesión de Firebase
            auth.signOut()

            // Redirigir a MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Eliminar el historial para que no puedan volver
            startActivity(intent)
            finish() // Cerrar ProfileActivity
        }
    }
}