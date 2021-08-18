package com.kwon.mbti_community.mypage.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kwon.mbti_community.R
import android.widget.AdapterView
import android.util.Log
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.TextView

class MypageFragment : Fragment(), AdapterView.OnItemSelectedListener {
    companion object{
        fun newInstance() : MypageFragment {
            return MypageFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TEST","MypageFragment - onCreate")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("TEST","MypageFragment - onAttach")
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("TEST","MypageFragment - onCreateView")
        val view=inflater.inflate(R.layout.fragment_mypage, container, false)

        val bundle = Bundle()
        val bundle_arguments = arguments
        val username = bundle_arguments?.getString("username").toString()
        bundle.putString("username", username)

        val select_feel_very_good = view.findViewById<TextView>(R.id.select_feel_very_good)
        val select_feel_good = view.findViewById<TextView>(R.id.select_feel_good)
        val select_feel_so = view.findViewById<TextView>(R.id.select_feel_so)
        val select_feel_bad = view.findViewById<TextView>(R.id.select_feel_bad)
        val select_feel_very_bad = view.findViewById<TextView>(R.id.select_feel_very_bad)

        select_feel_very_good.setOnClickListener {
            select_feel_very_good.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            select_feel_good.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_so.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_bad.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_very_bad.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        select_feel_good.setOnClickListener {
            select_feel_very_good.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_good.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            select_feel_so.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_bad.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_very_bad.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        select_feel_so.setOnClickListener {
            select_feel_very_good.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_good.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_so.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            select_feel_bad.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_very_bad.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        select_feel_bad.setOnClickListener {
            select_feel_very_good.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_good.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_so.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_bad.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            select_feel_very_bad.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        select_feel_very_bad.setOnClickListener {
            select_feel_very_good.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_good.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_so.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_bad.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_very_bad.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
        }

        return view
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    override fun onPause() {
        super.onPause()
        Log.d("TEST", "MypageFragment - onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TEST", "MypageFragment - onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("TEST", "MypageFragment - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TEST", "MypageFragment - onDestroy")
    }
}