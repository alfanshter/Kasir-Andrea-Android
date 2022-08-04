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

    private val isIduser = "idusers"
    fun setid_user(check: Int){
        editor?.putInt(isIduser,check)
        editor?.commit()
    }

    fun getid_user():Int?
    {
        return Pref?.getInt(isIduser,0)
    }

    private val isNama = "nama"
    fun setNama(check: String){
        editor?.putString(isNama,check)
        editor?.commit()
    }

    fun getNama():String?
    {
        return Pref?.getString(isNama,"")
    }

    private val isFoto = "foto"
    fun setFoto(check: String){
        editor?.putString(isFoto,check)
        editor?.commit()
    }

    fun getFoto():String?
    {
        return Pref?.getString(isFoto,"")
    }
}
