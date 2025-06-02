package com.example.nubo.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nubo.Interfaces.FragmentChange
import com.example.nubo.R
import com.example.nubo.databinding.FragmentMainBinding
import com.example.nubo.Interfaces.PageName
import com.example.nubo.view.fragments.MainFragment

class MainActivity : AppCompatActivity(), FragmentChange {

    private lateinit var binding_main: FragmentMainBinding

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding_main = FragmentMainBinding.inflate(layoutInflater)

        setContentView(R.layout.activity_main)

        var view1 = binding_main

        setFrag(PageName.MAIN.ordinal)
    }

    override fun setFrag(fragNum: Int) {
        val ft = supportFragmentManager.beginTransaction()

        //fragnum에 따라 fragment 교체
        when (fragNum) {
            PageName.MAIN.ordinal -> {
                ft.replace(R.id.fragmentFrame, MainFragment.newInstance(5, this))
                    .commit()
            }

        }
    }
}
