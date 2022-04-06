package cs481.beerpal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import cs481.beerpal.fragments.*

class MainHub : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView

    private val homeFragment = HomeFragment()
    private val beerDisplayFragment = BeerDisplayFragment()
    private val profileFragment = ProfileFragment()
    private val settingsFragment = SettingsFragment()
    private val wishlistFragment = WishlistFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_hub)
        auth = Firebase.auth
        replaceFragment(homeFragment)

        drawerLayout = findViewById(R.id.drawerLayout)
        actionBarToggle = ActionBarDrawerToggle(this,drawerLayout,0,0)
        drawerLayout.addDrawerListener(actionBarToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarToggle.syncState()
        navView = findViewById(R.id.navView)

        navView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.home_display -> {
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    replaceFragment(homeFragment)
                    true
                }
                R.id.beer_display -> {
                    replaceFragment(beerDisplayFragment)
                    Toast.makeText(this, "Beers", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.wishlist_display -> {
                    replaceFragment(wishlistFragment)
                    Toast.makeText(this, "Wishlist", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.profile_display -> {
                    Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                    replaceFragment(profileFragment)
                    true
                }
                R.id.settings_display -> {
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                    replaceFragment(settingsFragment)
                    true
                }
                else -> {
                    false
                }
            }
        }

        /*
        findViewById<Button>(R.id.signOut).setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }*/
    }

    override fun onSupportNavigateUp(): Boolean {
        drawerLayout.openDrawer(navView)
        return true
    }

    override fun onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}