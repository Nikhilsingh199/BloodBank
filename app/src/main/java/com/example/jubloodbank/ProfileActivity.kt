package com.example.jubloodbank

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.jubloodbank.databinding.ActivityProfileBinding
import com.example.jubloodbank.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch


class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    private val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user: FirebaseUser? = auth.currentUser
        val userId = user!!.uid
        val database = FirebaseDatabase.getInstance()
        val userReference: DatabaseReference = database.getReference("users").child(userId)

        binding.requestBack.setOnClickListener {
            onBackPressed()
        }

        binding.support.setOnClickListener {
            scheduleAlarmForSupport(this)
        }

        binding.history.setOnClickListener {
            Toast.makeText(this, "Not Yet Implemented", Toast.LENGTH_SHORT).show()
        }

        binding.settings.setOnClickListener {
            Toast.makeText(this, "Not Yet Implemented", Toast.LENGTH_SHORT).show()
        }

        lifecycleScope.launch {
            userReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val fieldValue = dataSnapshot.child("name").getValue(String::class.java)
                        val blood = dataSnapshot.child("blood").getValue(String::class.java)
                        val phone = dataSnapshot.child("phone").getValue(String::class.java)
                        if (fieldValue != null) {
                            binding.personName.text = fieldValue
                            binding.myblood.text = blood
                            binding.myphone.text = phone
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors or exceptions here
                }
            })

        }

        binding.logout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.editprofile.setOnClickListener {
            startActivity(Intent(this, UpdateProfileActivity::class.java))
        }
    }

    private fun scheduleAlarmForSupport(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, SupportAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Set the alarm to start after 10 seconds (for demonstration purposes)
        val triggerTime = System.currentTimeMillis() + 10000
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)

        Toast.makeText(context, "Alarm scheduled for Customer Support", Toast.LENGTH_SHORT).show()
    }
}
