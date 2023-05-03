package br.edu.puc.tenstadoessecarai

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TelaLogin : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var senhaEditText: EditText
    private lateinit var entrarButton: Button
    private lateinit var cadastrarText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_login)

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

        // Aqui você pode fazer a lógica de autenticação do usuário, verificando se o email e senha são válidos
        // Para fins de exemplo, vamos apenas abrir a tela de cadastro quando clicar no botão Entrar
        val intent = Intent(this, MilagreActivity::class.java)
        startActivity(intent)
        finish()
    }
}
