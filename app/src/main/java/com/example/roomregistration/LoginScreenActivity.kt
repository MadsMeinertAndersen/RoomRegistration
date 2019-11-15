package com.example.roomregistration

import androidx.appcompat.app.AppCompatActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toolbar
import kotlinx.android.synthetic.main.activity_login_screen.*

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginScreenActivity : Activity() {

    private var firebaseAuth: FirebaseAuth? = null
    private var welcomeText: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        firebaseAuth = FirebaseAuth.getInstance()

        val user = firebaseAuth!!.currentUser

        welcomeText = LoginScreenWelcomeText
        welcomeText!!.text = "Welcome " + user!!.email!!

        if (user != null)
            myReservationsButton.visibility = View.VISIBLE;

        setActionBar(toolbar as Toolbar)
        actionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.room_menu_item) {
            startActivity(Intent(this, RoomActivity::class.java))
        }
        if (item.itemId == R.id.logout_menu_item) {
            if (FirebaseAuth.getInstance().currentUser != null) {
                firebaseAuth!!.signOut()
                finish()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun ShowRooms(view: View) {
        startActivity(Intent(this, RoomActivity::class.java))
    }

    fun ShowReservations(view: View) {
        startActivity(Intent(this, MyReservations::class.java))
    }
}
