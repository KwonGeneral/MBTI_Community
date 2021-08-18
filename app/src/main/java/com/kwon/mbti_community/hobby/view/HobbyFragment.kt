package com.kwon.mbti_community.hobby.view

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

class HobbyFragment : Fragment(), AdapterView.OnItemSelectedListener {
    companion object{
        fun newInstance() : HobbyFragment {
            return HobbyFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TEST","HobbyFragment - onCreate")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("TEST","HobbyFragment - onAttach")
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("TEST","HobbyFragment - onCreateView")
        val view=inflater.inflate(R.layout.fragment_hobby, container, false)

        val bundle = Bundle()
        val bundle_arguments = arguments
        val username = bundle_arguments?.getString("username").toString()
        bundle.putString("username", username);

        return view
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    override fun onPause() {
        super.onPause()
        Log.d("TEST", "HobbyFragment - onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TEST", "HobbyFragment - onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("TEST", "HobbyFragment - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TEST", "HobbyFragment - onDestroy")
    }
}