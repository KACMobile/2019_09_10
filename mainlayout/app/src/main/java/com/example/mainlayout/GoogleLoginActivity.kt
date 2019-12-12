package com.example.mainlayout

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.layout_googlelogin.*
import com.google.android.gms.auth.api.Auth
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient



class GoogleLoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    private var googleSignInClient: GoogleSignInClient? = null

    lateinit var gso:GoogleSignInOptions

    lateinit var mGoogleApiClient : GoogleApiClient


    public fun signIn() {
        val signInIntent = googleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private val RC_SIGN_IN = 9001

    private lateinit var mAuth: FirebaseAuth

    public override fun onStart(){
        super.onStart()
        val currentUser = mAuth.currentUser
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_googlelogin)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        mAuth = FirebaseAuth.getInstance()

        googleLoginBtn.setOnClickListener {
            signIn()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Toast.makeText(this, "로그인 결과?", Toast.LENGTH_SHORT).show()

        if(requestCode === RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException){
                Toast.makeText(this, "로그인 성공??", Toast.LENGTH_SHORT).show()

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task->
                if(task.isSuccessful){
                    val user = mAuth?.currentUser
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@GoogleLoginActivity, MainActivity::class.java)
                    intent.putExtra("userID", user)
                    setResult(RESULT_OK, intent)
                    finish()
                }else{
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }

    }

    override fun onBackPressed() {
    }

}