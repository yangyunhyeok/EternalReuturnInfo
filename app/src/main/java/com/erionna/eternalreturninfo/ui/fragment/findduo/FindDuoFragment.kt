package com.erionna.eternalreturninfo.ui.fragment.findduo

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.FindDuoFragmentBinding
import com.erionna.eternalreturninfo.model.ERModel
import com.erionna.eternalreturninfo.model.User
import com.erionna.eternalreturninfo.ui.activity.BoardDialog
import com.erionna.eternalreturninfo.ui.activity.ChatActivity
import com.erionna.eternalreturninfo.ui.activity.DialogListener
import com.erionna.eternalreturninfo.ui.activity.MainActivity
import com.erionna.eternalreturninfo.ui.fragment.signin.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class FindDuoFragment : Fragment() {
    companion object {
        fun newInstance() = FindDuoFragment()

    }

    private var _binding: FindDuoFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var mDbRef: DatabaseReference

    private lateinit var mAuth: FirebaseAuth

    private var mUID = ""

    //파이어스터오랑 연동하기위한 코드

    private val firestore = FirebaseFirestore.getInstance()

    private val linearManager: LinearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private val adapter: FindduoAdapter by lazy {
        FindduoAdapter(
            requireContext(),
            onClickUser = { position, item ->
                Log.d("choco5733", "$item")
                if (item.uid != mAuth.uid) {
                    val customDialog = BoardDialog(requireContext(), item.name ?: "",object :
                        DialogListener {
                        override fun onOKButtonClicked() {
                            startActivity(
                                ChatActivity.newIntent(
                                    requireContext(),
                                    item
                                )
                            )
                        }
                    })
                    customDialog.show()
                } else {
                    val mainActivity = activity as MainActivity
                    mainActivity.binding.tabLayout.getTabAt(4)?.select()
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FindDuoFragmentBinding.inflate(inflater, container, false)

        mAuth = FirebaseAuth.getInstance()
        // db 초기화
        mDbRef = FirebaseDatabase.getInstance().reference
        // mUID 초기화
        mUID = mAuth.currentUser?.uid ?: ""

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.findduoRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.findduoRecyclerview.adapter = adapter
        initView()

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initView() = with(binding) {

        findduoServerBtn.setOnClickListener { showServerDialog() }
        findduoGenderBtn.setOnClickListener { showGenderDialog() }
        findduoTierBtn.setOnClickListener { showTierDialog() }
        findduoMostBtn.setOnClickListener { showMostDialog() }

        adapter.notifyDataSetChanged()

        loadAllUserDataFromFirebase()
    }


    private fun showServerDialog() {
        val mSelectedServer: ArrayList<String> = arrayListOf()

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        builder.setTitle("해당 사항을 선택해 주세요")

        builder.setMultiChoiceItems(R.array.server, null) { _, which, isChecked ->

            val server: Array<String> = resources.getStringArray(R.array.server)

            if (isChecked) {
                mSelectedServer.add(server[which])
            } else {
                mSelectedServer.remove(server[which])
            }
        }

        builder.setPositiveButton("완료") { _, _ ->
            var finalSelection = ""

            for (item: String in mSelectedServer) {
                finalSelection = finalSelection + "\n" + item
            }

            updateUserInFirebase(finalSelection, "server")

            Toast.makeText(requireContext(), finalSelection, Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.cancel()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }


    private fun showGenderDialog() {
        val mSelectedServer: ArrayList<String> = arrayListOf()

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        builder.setTitle("해당 사항을 선택해 주세요")

        builder.setMultiChoiceItems(R.array.gender, null) { _, which, isChecked ->

            val gender: Array<String> = resources.getStringArray(R.array.gender)

            if (isChecked) {
                mSelectedServer.add(gender[which])
            } else {
                mSelectedServer.remove(gender[which])
            }
        }

        builder.setPositiveButton("완료") { _, _ ->
            var finalSelection = ""

            for (item: String in mSelectedServer) {
                finalSelection = finalSelection + "\n" + item
            }

            updateUserInFirebase(finalSelection, "gender")

            Toast.makeText(requireContext(), finalSelection, Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.cancel()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun showTierDialog() {
        val mSelectedServer: ArrayList<String> = arrayListOf()

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        builder.setTitle("해당 사항을 선택해 주세요")

        builder.setMultiChoiceItems(R.array.tier, null) { _, which, isChecked ->

            val tier: Array<String> = resources.getStringArray(R.array.tier)

            if (isChecked) {
                mSelectedServer.add(tier[which])
            } else {
                mSelectedServer.remove(tier[which])
            }
        }

        builder.setPositiveButton("완료") { _, _ ->
            var finalSelection = ""

            for (item: String in mSelectedServer) {
                finalSelection = finalSelection + "\n" + item
            }

            updateUserInFirebase(finalSelection, "tier")

            Toast.makeText(requireContext(), finalSelection, Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.cancel()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun showMostDialog() {
        val mSelectedServer: ArrayList<String> = arrayListOf()

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        builder.setTitle("해당 사항을 선택해 주세요")

        builder.setMultiChoiceItems(R.array.most, null) { _, which, isChecked ->

            val most: Array<String> = resources.getStringArray(R.array.most)

            if (isChecked) {
                mSelectedServer.add(most[which])
            } else {
                mSelectedServer.remove(most[which])
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

            Toast.makeText(requireContext(), finalSelection, Toast.LENGTH_SHORT).show()
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
            Log.w(TAG, "User UID is null")
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
                    Log.d(TAG, "User info updated successfully")
                    // 여기에 추가적인 작업을 수행할 수 있습니다.
                } else {
                    // 업데이트가 실패한 경우
                    Log.e(TAG, "User info update failed: ${task.exception}")
                }
            }
    }


    private fun loadAllUserDataFromFirebase() {
        // Firebase Realtime Database 경로
        val databasePath = "user"

        // 데이터베이스에서 모든 사용자 정보 가져오기
        mDbRef.child(databasePath).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val filteredUsersList = ArrayList<ERModel>() // 필터링된 사용자 목록

                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(ERModel::class.java)

                        // 유저의 필드 중 하나라도 null이 아니면 필터링 대상에 포함
                        if (user != null &&
                            !user.server.isNullOrEmpty() &&
                            !user.name.isNullOrEmpty() &&
                            !user.gender.isNullOrEmpty() &&
                            !user.tier.isNullOrEmpty()
                        ) {
                            filteredUsersList.add(user)
                        }
                    }

                    // RecyclerView 어댑터의 데이터 소스에 필터링된 사용자 정보 추가
                    adapter.items.clear()
                    adapter.items.addAll(filteredUsersList)
                    // RecyclerView 갱신
                    adapter.notifyDataSetChanged()

                    val filteredUserCount = filteredUsersList.size
                    binding.findduoTotalNumber.text = filteredUserCount.toString() // 필터링된 사용자 수 표시
                } else {
                    // 데이터가 존재하지 않는 경우
                    Log.d(TAG, "No user data found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // 데이터 가져오기가 실패한 경우
                Log.e(TAG, "Data retrieval failed: $error")
            }
        })
    }

    private fun updateMostInFirestore(finalSelection: String) {
        val userId = mAuth.currentUser?.uid
        if (userId != null) {
            val userRef = firestore.collection("EternalReturnInfo").document(userId)

            // Firestore의 'most' 필드 업데이트
            userRef.update("character", finalSelection)
                .addOnSuccessListener {
                    Log.d(TAG, "Firestore 'most' 업데이트 성공")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Firestore 'most' 업데이트 실패: $e")
                }
        }
    }

}