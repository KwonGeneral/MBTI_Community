package com.kwon.mbti_community.board.view

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

class BoardFragment : Fragment(), AdapterView.OnItemSelectedListener {
    companion object{
        fun newInstance() : BoardFragment {
            return BoardFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TEST","BoardFragment - onCreate")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("TEST","BoardFragment - onAttach")
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("TEST","BoardFragment - onCreateView")
        val view=inflater.inflate(R.layout.fragment_board, container, false)

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
        Log.d("TEST", "BoardFragment - onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TEST", "BoardFragment - onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("TEST", "BoardFragment - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TEST", "BoardFragment - onDestroy")
    }
}