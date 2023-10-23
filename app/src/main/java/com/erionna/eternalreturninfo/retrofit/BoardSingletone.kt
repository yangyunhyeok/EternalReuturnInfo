package com.erionna.eternalreturninfo.retrofit

import android.util.Log
import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.model.ERModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlin.math.log

object BoardSingletone {

    private var loginUser: ERModel = ERModel()

    fun LoginUser(): ERModel {
        return loginUser
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