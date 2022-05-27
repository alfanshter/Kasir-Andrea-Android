package com.kasirandrea.kasirandrea.kasir

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.kasir.admin.UtamaActivity
import com.kasirandrea.kasirandrea.kasir.auth.LoginActivity
import com.kasirandrea.kasirandrea.kasir.owner.OwnerMenuActivity
import com.kasirandrea.kasirandrea.kasir.session.SessionManager
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class SplashActivity : AppCompatActivity() {
    lateinit var handler: Handler
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sessionManager = SessionManager(this)
        handler = Handler()
        handler.postDelayed({
            if (sessionManager.getLogin() == true){
                startActivity(intentFor<UtamaActivity>().clearTask().newTask())
                finish()
            }else if (sessionManager.getLoginowner() == true){
                startActivity(intentFor<OwnerMenuActivity>().clearTask().newTask())
                finish()
            }else{
                startActivity(intentFor<LoginActivity>().clearTask().newTask())
                finish()

            }
        }, 3000)

    }
}