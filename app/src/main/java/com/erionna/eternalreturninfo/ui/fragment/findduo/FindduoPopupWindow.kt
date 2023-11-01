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
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.FindDuoFragmentBinding
import com.erionna.eternalreturninfo.databinding.FindduoPopupWindowActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore

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

        // PopupWindow 생성
        popupWindow = PopupWindow(binding.root, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        popupWindow?.apply {
            elevation = 10.0f // 필요시 고도(elevation) 설정
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 투명한 배경 설정
            isOutsideTouchable = true
            animationStyle = android.R.style.Animation_Dialog // 애니메이션 스타일 설정
        }

        // 버튼에 대한 클릭 리스너 설정
        binding.findduoServerBtn.setOnClickListener { showServerDialog() }
        binding.findduoGenderBtn.setOnClickListener { showGenderDialog() }
        binding.findduoTierBtn.setOnClickListener { showTierDialog() }
        binding.findduoMostBtn.setOnClickListener { showMostDialog() }

        // PopupWindow 표시
        popupWindow?.showAtLocation(anchorView, Gravity.CENTER, 0, 0)
    }

    fun dismissPopup() {
        popupWindow?.dismiss()
    }



    private fun showServerDialog() {
        val mSelectedServer: ArrayList<String> = arrayListOf()

        val serverArray = context.resources.getStringArray(R.array.server)

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        builder.setTitle("해당 사항을 선택해 주세요")

        builder.setMultiChoiceItems(serverArray, null) { _, which, isChecked ->

            if (isChecked) {
                mSelectedServer.add(serverArray[which])
            } else {
                mSelectedServer.remove(serverArray[which])
            }
        }

        builder.setPositiveButton("완료") { _, _ ->
            var finalSelection = ""

            for (item: String in mSelectedServer) {
                finalSelection = finalSelection + "\n" + item
            }

            updateUserInFirebase(finalSelection, "server")

            Toast.makeText(context, finalSelection, Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.cancel()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }


    private fun showGenderDialog() {
        val mSelectedServer: ArrayList<String> = arrayListOf()

        val genderArray = context.resources.getStringArray(R.array.gender)

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        builder.setTitle("해당 사항을 선택해 주세요")

        builder.setMultiChoiceItems(genderArray, null) { _, which, isChecked ->


            if (isChecked) {
                mSelectedServer.add(genderArray[which])
            } else {
                mSelectedServer.remove(genderArray[which])
            }
        }

        builder.setPositiveButton("완료") { _, _ ->
            var finalSelection = ""

            for (item: String in mSelectedServer) {
                finalSelection = finalSelection + "\n" + item
            }

            updateUserInFirebase(finalSelection, "gender")

            Toast.makeText(context, finalSelection, Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.cancel()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun showTierDialog() {
        val mSelectedServer: ArrayList<String> = arrayListOf()

        val tierArray = context.resources.getStringArray(R.array.tier)

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        builder.setTitle("해당 사항을 선택해 주세요")

        builder.setMultiChoiceItems(tierArray, null) { _, which, isChecked ->

            if (isChecked) {
                mSelectedServer.add(tierArray[which])
            } else {
                mSelectedServer.remove(tierArray[which])
            }
        }

        builder.setPositiveButton("완료") { _, _ ->
            var finalSelection = ""

            for (item: String in mSelectedServer) {
                finalSelection = finalSelection + "\n" + item
            }

            updateUserInFirebase(finalSelection, "tier")

            Toast.makeText(context, finalSelection, Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.cancel()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun showMostDialog() {
        val mSelectedServer: ArrayList<String> = arrayListOf()

        val mostArray = context.resources.getStringArray(R.array.most)

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        builder.setTitle("해당 사항을 선택해 주세요")

        builder.setMultiChoiceItems(mostArray, null) { _, which, isChecked ->

            if (isChecked) {
                mSelectedServer.add(mostArray[which])
            } else {
                mSelectedServer.remove(mostArray[which])
            }
        }

        builder.setPositiveButton("완료") { _, _ ->
            var finalSelection = ""

            for (item: String in mSelectedServer) {
                finalSelection = item
            }

            // 파이어스토어에 모스트 값을 업데이트
            updateMostInFirestore(finalSelection)

            //파이어베이스에
            updateUserInFirebase(finalSelection, "most")

            Toast.makeText(context, finalSelection, Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.cancel()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun updateUserInFirebase(finalSelection: String, s: String) {

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

    private fun updateMostInFirestore(finalSelection: String) {
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