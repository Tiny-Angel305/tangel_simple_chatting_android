package com.example.tangel.vladychatting.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.view.*
import com.example.tangel.vladychatting.R

class SignupActivity:AppCompatActivity() {

    @BindView(R.id.email) lateinit var inputEmail:EditText

    @BindView(R.id.password) lateinit var inputPassword:EditText

    @BindView(R.id.sign_in_button) lateinit var btnSignIn:Button

    @BindView(R.id.sign_up_button) lateinit var btnSignUp:Button

    @BindView(R.id.btn_reset_password) lateinit var btnResetPassword:Button

    @BindView(R.id.progressBar) lateinit var progressBar:ProgressBar

    private var mAuth:FirebaseAuth? = null

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        ButterKnife.bind(this)

         //Get Firebase mAuth instance
        mAuth = FirebaseAuth.getInstance()

        btnResetPassword!!.setOnClickListener { startActivity(Intent(this@SignupActivity, ResetPasswordActivity::class.java)) }

        btnSignIn!!.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent())
            finish() }

        btnSignUp!!.setOnClickListener(object:View.OnClickListener {

            override fun onClick(v:View) {

                val email = inputEmail!!.text.toString().trim { it <= ' ' }
                val password = inputPassword!!.text.toString().trim { it <= ' ' }

                if (TextUtils.isEmpty(email))
                {
                    Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
                    return
                }

                if (TextUtils.isEmpty(password))
                {
                    Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
                    return
                }

                if (password.length < 6)
                {
                    Toast.makeText(applicationContext, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show()
                    return
                }

                progressBar!!.visibility = View.VISIBLE

                 //create user
                mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this@SignupActivity
                ) {
                        task ->
                        Toast.makeText(this@SignupActivity, "createUserWithEmail:onComplete:" + task.isSuccessful, Toast.LENGTH_SHORT).show()
                        progressBar!!.visibility = View.GONE
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the mAuth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful) {
                            Toast.makeText(this@SignupActivity, "Authentication failed." + task.exception!!,
                                Toast.LENGTH_SHORT).show()
                        } else {
                            startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                            finish()
                        }
                }

            }
        })
    }

    override fun onResume() {
        super.onResume()
        progressBar!!.visibility = View.GONE
    }
}