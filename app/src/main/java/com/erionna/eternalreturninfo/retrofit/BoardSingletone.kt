package com.erionna.eternalreturninfo.retrofit

import android.util.Log
import com.erionna.eternalreturninfo.model.ERModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

object BoardSingletone {

    private var loginUser: ERModel = ERModel()

    fun LoginUser(): ERModel {
        return loginUser
    }

    fun manager(): ERModel{
        return ERModel(uid="3pjD6ndbPVdzo2xUhjJAnuwKcPw2", name = "JANG", email = "JANG@gmail.com", profilePicture = "https://firebasestorage.googleapis.com/v0/b/eternalreturninfo-4dc4b.appspot.com/o/JANG%40gmail.com.jpg?alt=media&token=4aa4be09-7d71-470c-98b4-1052e069d5f9")
    }

    fun Login(){

        val uid = FirebaseAuth.getInstance().uid

        if (uid != null) {
            FBRef.userRef.child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        val user = snapshot.getValue<ERModel>()
                        if (user != null) {
                            loginUser = user
                            Log.d("user.name",user.name.toString())
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

    }

}