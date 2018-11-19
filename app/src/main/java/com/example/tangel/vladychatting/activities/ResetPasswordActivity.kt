package com.example.tangel.vladychatting.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import com.example.tangel.vladychatting.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset_password.*
import android.widget.Toast
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.text.TextUtils

class ResetPasswordActivity : AppCompatActivity() {
    @BindView(R.id.email) lateinit var inputEmail: EditText

    @BindView(R.id.btn_back) lateinit var btnBack: Button

    @BindView(R.id.btn_reset_password) lateinit var btnReset: Button

    @BindView(R.id.progressBar) lateinit var progressBar: ProgressBar

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        ButterKnife.bind(this)

        mAuth = FirebaseAuth.getInstance()

        btnBack!!.setOnClickListener { finish() }

        btnReset!!.setOnClickListener(object:View.OnClickListener {
            override fun onClick(v: View?) {
                val email = inputEmail!!.getText().toString().trim()

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(application, "Enter your registered email id", Toast.LENGTH_SHORT).show()
                    return
                }

                progressBar!!.setVisibility(View.VISIBLE)
                mAuth!!.sendPasswordResetEmail(email)
                    .addOnCompleteListener(object:OnCompleteListener<Void> {
                        override fun onComplete(task: Task<Void>) {
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@ResetPasswordActivity,
                                    "We have sent you instructions to reset your password!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(this@ResetPasswordActivity, "Failed to send reset email!", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            progressBar!!.setVisibility(View.GONE)
                        }
                    })
            }
        })
    }
}
