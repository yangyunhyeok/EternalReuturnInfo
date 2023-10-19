package com.erionna.eternalreturninfo.ui.fragment.findduo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.FindDuoFragmentBinding

class FindDuoFragment : Fragment() {
    companion object {
        fun newInstance() = FindDuoFragment()

    }
    private var _binding: FindDuoFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FindDuoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initView() = with(binding) {
        val dialogBtn : Button = findduoRegisterBtn

        dialogBtn.setOnClickListener { showDialog() }
    }

    private fun showDialog() {
        val mSelectedServer:ArrayList<String> = arrayListOf()

        val builder:AlertDialog.Builder =AlertDialog.Builder(requireContext())

        builder.setTitle("해당 사항을 선택해 주세요")

        builder.setMultiChoiceItems(R.array.server,null){
                _, which, isChecked->

            val server:Array<String> = resources.getStringArray(R.array.server)

            if(isChecked){
                mSelectedServer.add(server[which])
            }else{
                mSelectedServer.remove(server[which])
            }
        }

        builder.setPositiveButton("완료"){
            p0,p1 ->
            var finalSelection = ""

            for(item: String in mSelectedServer){
                finalSelection = finalSelection +"\n" + item
            }

            Toast.makeText(requireContext(),"선택된 아이템은 ${finalSelection}",Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("취소"){
            dialog,p1->dialog.cancel()
        }

        val alertDialog:AlertDialog = builder.create()
        alertDialog.show()
    }
}