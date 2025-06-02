package com.example.nubo.view.fragments

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.nubo.R
import android.view.ViewGroup
import android.widget.Button
import com.example.nubo.Interfaces.FragmentChange
import com.example.nubo.databinding.FragmentMainBinding
import com.example.nubo.Interfaces.PageName

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    lateinit var fragmentChange: FragmentChange

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)


        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

        companion object {
            fun newInstance(param: Int, Fragmentch: FragmentChange): com.example.nubo.view.fragments.MainFragment {
                val fragment = MainFragment()
                val args = Bundle()
                args.putInt("param", param)
                fragment.arguments = args
                fragment.fragmentChange = Fragmentch
                return fragment
            }
        }


    }