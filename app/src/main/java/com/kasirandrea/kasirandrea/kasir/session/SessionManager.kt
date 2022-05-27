package com.kasirandrea.kasirandrea.kasir.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(private val context: Context?) {
    val privateMode = 0
    val privateName ="login"
    var Pref : SharedPreferences?=context?.getSharedPreferences(privateName,privateMode)
    var editor : SharedPreferences.Editor?=Pref?.edit()

    private val islogin = "login"
    fun setLogin(check: Boolean){
        editor?.putBoolean(islogin,check)
        editor?.commit()
    }

    fun getLogin():Boolean?
    {
        return Pref?.getBoolean(islogin,false)
    }

    private val isloginowner = "loginowner"
    fun setLoginowner(check: Boolean){
        editor?.putBoolean(isloginowner,check)
        editor?.commit()
    }

    fun getLoginowner():Boolean?
    {
        return Pref?.getBoolean(isloginowner,false)
    }
}
