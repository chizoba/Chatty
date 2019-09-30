package me.chizobaogbonna.chatty.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.chizobaogbonna.chatty.R

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.container,
                HomeFragment()
            )
                .commit()
        }

    }
}
