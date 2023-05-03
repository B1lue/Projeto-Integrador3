package br.edu.puc.tenstadoessecarai

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.puc.tenstadoessecarai.databinding.ActivityMilagreBinding
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.GsonBuilder

class MilagreActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var nomeEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var senhaEditText: EditText
    private lateinit var cadastrarButton: Button
    private lateinit var adicionarEnderecoButton: MaterialButton
    private lateinit var db: FirebaseFirestore

    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()


    private lateinit var functions: FirebaseFunctions

    private lateinit var binding:ActivityMilagreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMilagreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)

        nomeEditText = findViewById(R.id.editTextNome)
        emailEditText = findViewById(R.id.editTextEmail)
        senhaEditText = findViewById(R.id.editTextSenha)
        cadastrarButton = findViewById(R.id.buttonCadastro)
        adicionarEnderecoButton = findViewById(R.id.add_address_button)

        db = FirebaseFirestore.getInstance()

        /*adicionarEnderecoButton.setOnClickListener {
            adicionarEndereco()
        }

        cadastrarButton.setOnClickListener {
            cadastrarUsuario()
        }*/
        binding.buttonCadastro.setOnClickListener(this)
        binding.addAddressButton.setOnClickListener(this)
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


        FirebaseFunctions.getInstance("southamerica-east1")
            .getHttpsCallable("setUserProfile")
            .call(dados)
            .continueWith { res ->
                Log.e("x", "${res.exception}")
                res.result.data as String
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

    override fun onClick(v: View) {
        if(v.id == R.id.buttonCadastro){
            cadastrarUsuario()
        }
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
    }
}

data class CustomResponse(val status: String?, val message: String?, val payload: Any?)
