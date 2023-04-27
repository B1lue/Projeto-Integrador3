package br.edu.puc.tenstadoessecarai

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions

class MilagreActivity : AppCompatActivity() {
    private lateinit var nomeEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var senhaEditText: EditText
    private lateinit var cadastrarButton: Button
    private lateinit var adicionarEnderecoButton: MaterialButton
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milagre)
        FirebaseApp.initializeApp(this);

        nomeEditText = findViewById(R.id.editTextNome)
        emailEditText = findViewById(R.id.editTextEmail)
        senhaEditText = findViewById(R.id.editTextSenha)
        cadastrarButton = findViewById(R.id.buttonCadastro)
        adicionarEnderecoButton = findViewById(R.id.add_address_button)

        db = FirebaseFirestore.getInstance()

        adicionarEnderecoButton.setOnClickListener {
            adicionarEndereco()
        }

        cadastrarButton.setOnClickListener {
            cadastrarUsuario()
        }
    }

    private fun cadastrarUsuario() {
        val nome = nomeEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val senha = senhaEditText.text.toString().trim()

        if (TextUtils.isEmpty(nome)) {
            nomeEditText.error = "Campo obrigatório"
            return
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.error = "Campo obrigatório"
            return
        }

        if (TextUtils.isEmpty(senha)) {
            senhaEditText.error = "Campo obrigatório"
            return
        }

        // Cria um HashMap com os dados do usuário
        val dados = hashMapOf(
            "nome" to nome,
            "email" to email,
            "senha" to senha
        )

        FirebaseFunctions.getInstance(FirebaseApp.getInstance(), "southamerica-east1")
            .getHttpsCallable("cadastrarUsuario")
            .call(dados)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "sucesso ao mandar usuario para china")
                    // Salva o usuário no Firestore Database
                    db.collection("usuarios").document(email).set(dados)
                        .addOnSuccessListener {
                            Log.d(TAG, "Usuário salvo com sucesso")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Erro ao salvar o usuário", e)
                        }
                } else {
                    Log.w(TAG, "Erro ao criar o usuário", task.exception)
                }
            }
    }

    private fun adicionarEndereco() {
        val additionalAddressesContainer = findViewById<LinearLayout>(R.id.additional_addresses_container)
        val newAddressEditText = EditText(this)
        newAddressEditText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        newAddressEditText.hint = getString(R.string.endereço)
        newAddressEditText.minHeight = resources.getDimensionPixelSize(R.dimen.text_field_size)

        additionalAddressesContainer.addView(newAddressEditText)
    }

    companion object {
        private const val TAG = "MilagreActivity"
    }
}
