package com.example.tangel.vladychatting.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.support.annotation.NonNull
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.example.tangel.vladychatting.R

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    @BindView(R.id.email) lateinit var inputEmail:EditText

    @BindView(R.id.password) lateinit var inputPassword:EditText

    @BindView(R.id.btn_signup) lateinit var btnSignUp:Button

    @BindView(R.id.btn_login) lateinit var btnLogin:Button

    @BindView(R.id.btn_reset_password) lateinit var btnResetPassword:Button

    @BindView(R.id.progressBar) lateinit var progressBar:ProgressBar

    private var mAuth:FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        if (mAuth!!.currentUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        setContentView(R.layout.activity_login)

        ButterKnife.bind(this)

        btnSignUp!!.setOnClickListener(object:View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
            }
        })

        btnResetPassword!!.setOnClickListener(object:View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(this@LoginActivity, ResetPasswordActivity::class.java))
            }
        })

        btnLogin!!.setOnClickListener(object:View.OnClickListener {
            override fun onClick(v: View?) {
                val email = inputEmail!!.getText().toString()
                val password = inputPassword!!.getText().toString()

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
                    return
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
                    return
                }

                progressBar!!.setVisibility(View.VISIBLE)

                //authenticate user
                mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener(this@LoginActivity,
                        object:OnCompleteListener<AuthResult> {
                            override fun onComplete(task: Task<AuthResult>) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar!!.setVisibility(View.GONE)
                                if (!task.isSuccessful) {
                                    // there was an error
                                    if (password.length < 6) {
                                        inputPassword!!.setError(getString(R.string.minimum_password))
                                    } else {
                                        Log.w("TAG", "signInWithEmail", task.getException())
                                        Toast.makeText(this@LoginActivity, getString(R.string.auth_failed), Toast.LENGTH_LONG)
                                            .show()
                                    }
                                } else {
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }

                        })
            }

        })

    }
}
