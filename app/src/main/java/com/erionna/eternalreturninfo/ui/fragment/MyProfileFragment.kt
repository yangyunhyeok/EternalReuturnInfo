package com.erionna.eternalreturninfo.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.MyprofileCharacterDialogBinding
import com.erionna.eternalreturninfo.databinding.MyprofileFragmentBinding
import com.erionna.eternalreturninfo.ui.activity.LoginPage
import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyProfileFragment : Fragment() {
    private val binding get() = _binding!!
    private var _binding: MyprofileFragmentBinding? = null
    private var auth: FirebaseAuth? = null
    var db = Firebase.firestore
    private lateinit var characterbinding: MyprofileCharacterDialogBinding

    companion object {
        fun newInstance() = MyProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyprofileFragmentBinding.inflate(inflater, container, false)
        characterbinding = MyprofileCharacterDialogBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        var uid = auth?.uid.toString()
        Patch(uid)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        var logoutBtn = binding.myprofileLogoutBtn
        var characterBtn = binding.myprofileCharacterImg
        logoutBtn.setOnClickListener {
            Firebase.auth.signOut()
            var intent = Intent(activity, LoginPage::class.java)
            startActivity(intent)
            requireActivity().finish()
        }


        //실험체 이미지 선택
        characterBtn.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.myprofile_character_dialog, null)
            val alertDialog = AlertDialog.Builder(requireActivity())
                .setView(dialogView)
                .create()
            val characterSpinner = dialogView.findViewById<Spinner>(R.id.myprofile_character_sp)
            val button = dialogView.findViewById<Button>(R.id.myprofile_select_btn)

            val characterlist = resources.getStringArray(R.array.character)
            val adapter = ArrayAdapter<String>(
                requireContext(),
                R.layout.signup_spinner,
                R.id.spinner_tv,
                characterlist
            )
            var selectCharacter = characterlist[0]
            characterSpinner.adapter = adapter

            characterSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectCharacter = characterlist[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
            // 실험체 선택버튼
            button.setOnClickListener {
                FirebaseFirestore.getInstance()
                    .collection("EternalReturnInfo")
                    .document(auth!!.uid!!)
                    .update("character",selectCharacter)
                    .addOnSuccessListener {
                        Log.d("실험체","성공")
                        var uid = auth?.uid.toString()
                        Patch(uid)
                    }
                    .addOnFailureListener {
                        Log.d("실험체","실패")
                    }
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
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

    override fun onResume() {
        super.onResume()
        var uid = auth?.uid.toString()
        Patch(uid)
    }

    fun GooglePatch() {

    }

    fun updateCharacter(character: String) {
        ImgPacth(character)
    }

}