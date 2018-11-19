package com.example.tangel.vladychatting.activities

import android.app.Activity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.content.res.Resources
import android.support.design.widget.FloatingActionButton
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.tangel.vladychatting.R
import com.example.tangel.vladychatting.adapters.MyListAdapter
import com.example.tangel.vladychatting.models.ChatMessage
import com.google.firebase.database.*

class MainActivity : Activity() {

    private val SIGN_IN_REQUEST_CODE: Int = 1001

    private var mAdapter: MyListAdapter<ChatMessage>? = null

    private var mAuth:FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private val mRootReference = FirebaseDatabase.getInstance().reference
    var mDataSnapshot:DataSnapshot? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get Firebase mAuth instance
        mAuth = FirebaseAuth.getInstance()

        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }

        if (mAuth!!.currentUser == null) {
            startActivityForResult(
                Intent(this@MainActivity, SignupActivity::class.java),
                SIGN_IN_REQUEST_CODE
            )
        }
        else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            Toast.makeText(this,
                "Welcome " + mAuth!!.currentUser!!.email,
                Toast.LENGTH_LONG)
                .show()

            // Load chat room contents
            displayChatMessages()
        }

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton

        fab.setOnClickListener {
            val input = findViewById<View>(R.id.input) as EditText

            // Read the input field and push a new instance
            // of ChatMessage to the Firebase database
            mRootReference
                .push()
                .setValue(
                    ChatMessage(
                        input.text.toString(),
                        mAuth!!.currentUser!!.email!!
                    )
                )

            // Clear the input
            input.setText("")
        }

        mRootReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                mDataSnapshot = p0
                displayChatMessages()
            }
        })

        mRootReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                mDataSnapshot = p0
                displayChatMessages()
            }
        })
    }

    private fun displayChatMessages() {
        val listOfMessages = findViewById<View>(R.id.list_of_messages) as ListView

        mAdapter = object : MyListAdapter<ChatMessage>(
            this, ChatMessage::class.java,
            R.layout.message, mDataSnapshot
        ) {
            override fun populateView(v: View, model: ChatMessage, position: Int) {
                // Get references to the views of message.xml
                val messageText = v.findViewById(R.id.message_text) as TextView
                val messageUser = v.findViewById(R.id.message_user) as TextView
                val messageTime = v.findViewById(R.id.message_time) as TextView

                // Set their text
                messageText.setText(model.messageText)
                messageUser.setText(model.messageUser)

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.messageTime))
            }
        }

        listOfMessages.setAdapter(mAdapter)
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(
                    this,
                    "Successfully signed in. Welcome!",
                    Toast.LENGTH_LONG
                )
                    .show()
                displayChatMessages()
            } else {
                Toast.makeText(
                    this,
                    "We couldn't sign you in. Please try again later.",
                    Toast.LENGTH_LONG
                )
                    .show()

                // Close the app
                finish()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth!!.removeAuthStateListener(mAuthListener!!);
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === R.id.menu_sign_out) {
            mAuth!!.signOut()
        }
        return true
    }
}
