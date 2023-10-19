package com.erionna.eternalreturninfo.ui.fragment.findduo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.erionna.eternalreturninfo.databinding.FindDuoFragmentBinding

class FindDuoActivity : Fragment() {
    companion object {
        fun newInstance() = FindDuoActivity()

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
        // TODO: connect adapter
    }
}