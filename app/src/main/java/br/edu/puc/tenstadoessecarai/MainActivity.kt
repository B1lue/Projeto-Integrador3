import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import br.edu.puc.tenstadoessecarai.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

@Suppress("NAME_SHADOWING")
class MainActivity : AppCompatActivity() {

    private lateinit var nomeEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var senhaEditText: EditText
    private lateinit var cadastrarButton: Button
    private lateinit var adicionarEnderecoButton: FloatingActionButton
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this);

        nomeEditText = findViewById(R.id.editTextNome)
        emailEditText = findViewById(R.id.editTextEmail)
        senhaEditText = findViewById(R.id.editTextSenha)
        cadastrarButton = findViewById(R.id.buttonCadastro)
        adicionarEnderecoButton = findViewById(R.id.add_address_button)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()

        cadastrarButton.setOnClickListener {
            cadastrarUsuario()
        }

        adicionarEnderecoButton.setOnClickListener {
            adicionarEndereco()
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

        mAuth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val uid = user?.uid ?: ""
                    val email = user?.email ?: ""
                    val databaseRef = mDatabase.reference.child("usuarios").child(uid)

                    // Cria um HashMap com os dados do usuário
                    val usuario = HashMap<String, String>()
                    usuario["nome"] = nome
                    usuario["email"] = email

                    // Salva o usuário no Realtime Database
                    databaseRef.setValue(usuario).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Usuário salvo com sucesso no Realtime Database")
                        } else {
                            Log.w(TAG, "Erro ao salvar o usuário no Realtime Database", task.exception)
                        }
                    }

                    // Envia um e-mail de verificação para o usuário
                    user?.sendEmailVerification()?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "E-mail de verificação enviado para o usuário")
                        } else {
                            Log.w(TAG, "Erro ao enviar e-mail de verificação para o usuário", task.exception)
                        }
                    }
                } else {
                    Log.w(TAG, "Erro ao criar o usuário", task.exception)
                }
            }
    }

    private fun adicionarEndereco() {
        // código para adicionar endereço
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}