package com.kasirandrea.kasirandrea.kasir.owner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.kasir.owner.ui.admin.AdminFragment
import com.kasirandrea.kasirandrea.kasir.owner.ui.gaji.GajiOwnerFragment
import com.kasirandrea.kasirandrea.kasir.owner.ui.produk.ProdukOwnerFragment
import com.kasirandrea.kasirandrea.kasir.owner.ui.transaksi.TransaksiOwnerFragment

class OwnerMenuActivity : AppCompatActivity() {
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_produk -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.framehome,
                        ProdukOwnerFragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_transaksi -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.framehome,
                        TransaksiOwnerFragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true

                }
                R.id.navigation_gaji -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.framehome,
                        GajiOwnerFragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true

                }

                R.id.navigation_admin -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.framehome,
                        AdminFragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true

                }


            }

            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_menu)
        val navView: BottomNavigationView = findViewById(R.id.nav_viewhome)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        moveToFragment(ProdukOwnerFragment())

    }

    private fun moveToFragment(fragment: Fragment) {
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.framehome, fragment)
        fragmentTrans.commit()
    }

}