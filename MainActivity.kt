package com.example.using_room

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.using_room.Room.MyDatabase
import com.example.using_room.Room.User
import com.example.using_room.Room.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Room Database
        val db = Room.databaseBuilder(
            applicationContext,
            MyDatabase::class.java, "user_database"
        ).build()

        userDao = db.userDao()

        // Get UI Elements
        val idEditText = findViewById<EditText>(R.id.etId)
        val nameEditText = findViewById<EditText>(R.id.etName)
        val emailEditText = findViewById<EditText>(R.id.etEmail)
        //val ageEditText = findViewById<EditText>(R.id.etAge)
        val addButton = findViewById<Button>(R.id.btnAddUser)
        val fetchButton = findViewById<Button>(R.id.btnFetchUsers)
        val updatebutton = findViewById<Button>(R.id.btnUpdate)
        val deletebutton = findViewById<Button>(R.id.btnDelete)


        // Insert User Data when Add Button is Clicked
        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            //val age = ageEditText.text.toString().toIntOrNull()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                val user = User(name = name, email = email)

                lifecycleScope.launch(Dispatchers.IO) {
                    userDao.insertUser(user)

                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "User Added!", Toast.LENGTH_SHORT).show()
                        nameEditText.text.clear()
                        emailEditText.text.clear()
                        //ageEditText.text.clear()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Please enter all details", Toast.LENGTH_SHORT).show()
            }
        }

        // Fetch and Display All Users when Fetch Button is Clicked
        fetchButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val users = userDao.getAllUsers()

                runOnUiThread {
                    Toast.makeText(this@MainActivity, users.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
        updatebutton.setOnClickListener {
            val id = idEditText.text.toString().toIntOrNull()
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            //val age = ageEditText.text.toString().toIntOrNull()

            if (id != null && name.isNotEmpty() && email.isNotEmpty()) {
                val user = User(id = id, name = name, email = email)

                lifecycleScope.launch(Dispatchers.IO) {
                    userDao.updateUser(user)
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "User Updated!", Toast.LENGTH_SHORT).show()
                        idEditText.text.clear()
                        nameEditText.text.clear()
                        emailEditText.text.clear()
                        //ageEditText.text.clear()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Please enter all details", Toast.LENGTH_SHORT).show()
            }
        }

        // Delete User
        deletebutton.setOnClickListener {
            val id = idEditText.text.toString().toIntOrNull()

            if (id != null) {
                val user = User(id = id, name = "", email = "")

                lifecycleScope.launch(Dispatchers.IO) {
                    userDao.deleteUser(user)
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "User Deleted!", Toast.LENGTH_SHORT).show()
                        idEditText.text.clear()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Please enter a valid ID", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

