package com.erionna.eternalreturninfo.ui.fragment.findduo

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.FindDuoFragmentBinding
import com.erionna.eternalreturninfo.databinding.FindduoPopupWindowActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class FindduoPopupWindow(private val context: Context) {
    private var popupWindow: PopupWindow? = null
    private var _binding: FindduoPopupWindowActivityBinding? = null
    private val binding get() = _binding!!
    private lateinit var mDbRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private var mUID = ""

    //파이어스터오랑 연동하기위한 코드
    private val firestore = FirebaseFirestore.getInstance()

    fun showPopup(anchorView: View) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        _binding = FindduoPopupWindowActivityBinding.inflate(inflater, null, false)
        mAuth = Firebase.auth
        mDbRef = Firebase.database.reference

        // PopupWindow
        popupWindow = PopupWindow(
            binding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow?.apply {
            elevation = 10.0f // 고도
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경
            isOutsideTouchable = true
            animationStyle = android.R.style.Animation_Dialog // 애니메이션
        }

        //서버 스피너

        val serverlist = context.resources.getStringArray(R.array.server)
        val adpater = ArrayAdapter<String>(
            context,
            R.layout.findduo_spinner,
            R.id.findduo_spinner_tv,
            serverlist
        )
        var selectServer: String? = null

        binding.findduoServerBtn.adapter = adpater

        binding.findduoServerBtn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectServer = serverlist[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectServer = null // 사용자가 선택을 취소할 때 null로 초기화
            }
        }

        //티어 스피너

        val tierlist = context.resources.getStringArray(R.array.tier)
        val adpater2 =
            ArrayAdapter<String>(context, R.layout.findduo_spinner, R.id.findduo_spinner_tv, tierlist)
        var selectTier: String? = null

        binding.findduoTierBtn.adapter = adpater2

        binding.findduoTierBtn.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectTier = tierlist[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    selectServer = null
                }
            }

        //성별 스피너

        val genderlist = context.resources.getStringArray(R.array.gender)
        val adpater3 = ArrayAdapter<String>(
            context,
            R.layout.findduo_spinner,
            R.id.findduo_spinner_tv,
            genderlist
        )
        var selectGender : String? = null

        binding.findduoGenderBtn.adapter = adpater3

        binding.findduoGenderBtn.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectGender = genderlist[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    selectServer = null
                }
            }

        //선호 실험체 스피너

        val mostlist = context.resources.getStringArray(R.array.character)
        val adpater4 = ArrayAdapter<String>(
            context,
            R.layout.findduo_spinner,
            R.id.findduo_spinner_tv,
            mostlist
        )
        var selectMost: String? = null

        binding.findduoMostBtn.adapter = adpater4

        binding.findduoMostBtn.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectMost = mostlist[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    selectServer = null
                }
            }

        binding.findduoYesBtn.setOnClickListener {
            updateUserInFirebase(selectServer, "server")
            updateUserInFirebase(selectTier, "tier")
            updateUserInFirebase(selectGender, "gender")
            updateUserInFirebase(selectMost, "most")
            addTimestampToFirebase()
            updateMostInFirestore(selectMost)
            dismissPopup()
        }

        // PopupWindow 표시
        popupWindow?.showAtLocation(anchorView, Gravity.CENTER, 0, 0)
    }

    private fun addTimestampToFirebase() {
        val timestamp = System.currentTimeMillis() // 현재 시간을 밀리초로 얻기

        updateUserInFirebase(timestamp.toString(), "timestamp")
    }

    fun dismissPopup() {
        popupWindow?.dismiss()
    }

    private fun updateUserInFirebase(finalSelection: String?, s: String) {

        // 사용자의 Firebase UID
        val userId = mAuth.currentUser?.uid

        // 사용자의 Firebase UID가 없으면 함수를 종료
        if (userId == null) {
            Log.w(ContentValues.TAG, "User UID is null")
            return
        }

        // 업데이트할 데이터를 맵으로 구성
        val updateData = mapOf(
            s to finalSelection
        )

        // Firebase Realtime Database 경로
        val databasePath = "user/$userId"

        // 데이터베이스에 업데이트할 내용을 설정
        mDbRef.child(databasePath).updateChildren(updateData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 업데이트가 성공한 경우
                    Log.d(ContentValues.TAG, "User info updated successfully")
                    // 여기에 추가적인 작업을 수행할 수 있습니다.
                } else {
                    // 업데이트가 실패한 경우
                    Log.e(ContentValues.TAG, "User info update failed: ${task.exception}")
                }
            }
    }

    private fun updateMostInFirestore(finalSelection: String?) {
        val userId = mAuth.currentUser?.uid
        if (userId != null) {
            val userRef = firestore.collection("EternalReturnInfo").document(userId)

            // Firestore의 'most' 필드 업데이트
            userRef.update("character", finalSelection)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Firestore 'most' 업데이트 성공")
                }
                .addOnFailureListener { e ->
                    Log.e(ContentValues.TAG, "Firestore 'most' 업데이트 실패: $e")
                }
        }
    }
}