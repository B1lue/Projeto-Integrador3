package br.edu.puc.tenstadoessecarai

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

class TelaLogin : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var senhaEditText: EditText
    private lateinit var entrarButton: Button
    private lateinit var cadastrarText: TextView
    private val TAG = "SignUpFragment"
    private lateinit var auth: FirebaseAuth
    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_login)

        supportActionBar?.hide()

        emailEditText = findViewById(R.id.email)
        senhaEditText = findViewById(R.id.password)
        entrarButton = findViewById(R.id.login_button)
        cadastrarText = findViewById(R.id.register_text)

        entrarButton.setOnClickListener {
            logarUsuario()
        }

        cadastrarText.setOnClickListener {
            val intent = Intent(this@TelaLogin, MilagreActivity::class.java)
            startActivity(intent)
        }

    }

    private fun logarUsuario() {
        val email = emailEditText.text.toString().trim()
        val senha = senhaEditText.text.toString().trim()

        if (TextUtils.isEmpty(email)) {
            emailEditText.error = "Campo obrigatório"
            return
        }

        if (TextUtils.isEmpty(senha)) {
            senhaEditText.error = "Campo obrigatório"
            return
        }

        // Para fins de exemplo, vamos apenas abrir a tela de cadastro quando clicar no botão Entrar
        startActivity(Intent(this, TelaUsuario::class.java))
        finish()
    }
    private fun updateUserProfile(nome: String, telefone: String, email: String, uid: String, fcmToken: String) : Task<CustomResponse> {
        // chamar a function para atualizar o perfil.
        functions = Firebase.functions("southamerica-east1")

        // Create the arguments to the callable function.
        val data = hashMapOf(
            "nome" to nome,
            "telefone" to telefone,
            "email" to email,
            "uid" to uid,
            "fcmToken" to fcmToken
        )

        return functions
            .getHttpsCallable("setUserProfile")
            .call(data)
            .continueWith { task ->

                val result = gson.fromJson((task.result?.data as String), CustomResponse::class.java)
                result
            }

    }
}
