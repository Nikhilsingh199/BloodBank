package com.example.jubloodbank.HomePage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.jubloodbank.ChatFragment
import com.example.jubloodbank.MessageFragment
import com.example.jubloodbank.ProfileActivity
import com.example.jubloodbank.R
import com.example.jubloodbank.bloodlist.BloodListActivity
import com.example.jubloodbank.databinding.ActivityHomePageBinding
import com.example.jubloodbank.feeds.BloodRequestActivity
import com.example.jubloodbank.login.LoginActivity
import com.example.jubloodbank.registration.User
import com.example.jubloodbank.requestblood.RequestBloodActivity
import com.example.jubloodbank.searchdonor.SearchDonorActivitiy
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomePageActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawer: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var binding: ActivityHomePageBinding
    private var imageslist = mutableListOf<Int>()

    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        drawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawer,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        FirebaseMessaging.getInstance().subscribeToTopic("All")
        FirebaseApp.initializeApp(this)
        val user: FirebaseUser? = auth.currentUser

        val userId = user!!.uid
        val database = FirebaseDatabase.getInstance()
        val userReference: DatabaseReference = database.getReference("users").child(userId)

        lifecycleScope.launch {
            val fieldRef = userReference.child("name")

            fieldRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val fieldValue = dataSnapshot.getValue(String::class.java)
                        if (fieldValue != null) {
                            // Set the retrieved field value (name) to your TextView
                            binding.username.text = fieldValue
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors or exceptions here
                }
            })
        }

        binding.profileImageView.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        binding.username.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.alldonor.setOnClickListener {
            startActivity(Intent(this, BloodListActivity::class.java))
        }
        binding.requestblood.setOnClickListener {
            startActivity(Intent(this, RequestBloodActivity::class.java))

        }
        binding.cvSearchDonor.setOnClickListener {
            startActivity(Intent(this, SearchDonorActivitiy::class.java))
        }
        binding.feed.setOnClickListener {
            startActivity(Intent(this, BloodRequestActivity::class.java))
        }

        imageslist.add(R.drawable.ddonateblood)
        imageslist.add(R.drawable.img_3)
        imageslist.add(R.drawable.donate_blood)
        imageslist.add(R.drawable.blood_donor_day)

        binding.viewPager2.adapter = HomeViewPageAdapter(imageslist)
        binding.viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.circleIndicator3.setViewPager(binding.viewPager2)
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Handle navigation item selections here
            R.id.nav_profile -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.nav_message -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container,MessageFragment())
                .commit()

            R.id.nav_chat -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container,ChatFragment())
                .commit()

            R.id.nav_profile -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container,MessageFragment())
                .commit()

            R.id.nav_share -> Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
            R.id.nav_send -> Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show()
            // Add other navigation item handling as needed
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
