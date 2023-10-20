package com.erionna.eternalreturninfo.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.MyprofileFragmentBinding

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyProfileFragment : Fragment() {
    private val binding get() = _binding!!
    private var _binding: MyprofileFragmentBinding? = null
    private var auth: FirebaseAuth? = null
    var mGoogleSignInClient: GoogleSignInClient? = null
    var db = Firebase.firestore

    companion object {
        fun newInstance() = MyProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyprofileFragmentBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        var uid = auth?.uid.toString()
        Patch(uid)
        return binding.root

    }

    fun Patch(uid: String) {
        val docRef = db.collection("EternalReturnInfo").document("$uid")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    var uri = Uri.parse(document["profile"].toString())
                    binding.myprofileEmailTv.text = document["email"].toString()
                    binding.myprofileNicknameTv.text = document["nickName"].toString()
                    binding.myprofileMycharacterTv.text = document["character"].toString()
                    Glide.with(this).load(uri).into(binding.myprofileProfileImg);
                    ImgPacth(document["character"].toString())
                    Log.d("데이터", "${document.data}")
                } else {
                    Log.d("마이페이지", "No such document")
                }
            }
    }

    fun ImgPacth(character: String) {
        when (character) {
            "헤이즈" -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_01haze)
            "쇼우" -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_02xiukai)
            "나딘" -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_03nadine)
            "나타폰" -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_04nathapon)
            "니키" -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_05nicty)
            "다니엘" -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_06daniel)
            "띠아" -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_07tia)
            "라우라" -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_08laura)
            "레녹스" -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_09lenox)
            "레온" -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_10leon)
        }
    }

}