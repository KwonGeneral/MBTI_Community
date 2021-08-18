package com.kwon.mbti_community.home.view

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
import android.widget.ImageView

class HomeFragment : Fragment(), AdapterView.OnItemSelectedListener {
    companion object{
        fun newInstance() : HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TEST","HomeFragment - onCreate")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("TEST","HomeFragment - onAttach")
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("TEST","HomeFragment - onCreateView")
        val view=inflater.inflate(R.layout.fragment_home, container, false)

        val bundle = Bundle()
        val bundle_arguments = arguments
        val username = bundle_arguments?.getString("username").toString()
        bundle.putString("username", username)

        // 프로필 이미지 모서리 둥글게 설정

        return view
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}