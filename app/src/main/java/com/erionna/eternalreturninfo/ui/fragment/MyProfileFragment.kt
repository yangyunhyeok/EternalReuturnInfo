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
import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyProfileFragment : Fragment() {
    private val binding get() = _binding!!
    private var _binding: MyprofileFragmentBinding? = null
    private var auth: FirebaseAuth? = null
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

    // 마이페이지 생성
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
                }
            }
    }

    // 이미지 패치
    fun ImgPacth(character: String) {
        val array: Array<String> = resources.getStringArray(R.array.character)
        when (character) {
            array[0] -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_01haze)
            array[1] -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_02xiukai)
            array[2] -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_03nadine)
            array[3] -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_04nathapon)
            array[4] -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_05nicty)
            array[5] -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_06daniel)
            array[6] -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_07tia)
            array[7] -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_08laura)
            array[8] -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_09lenox)
            array[9] -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_10leon)
        }
    }

    fun GooglePatch() {

    }

    fun updateCharacter(character: String) {
        ImgPacth(character)
    }

}