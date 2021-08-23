package com.kwon.mbti_community.write.view

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

class WriteFragment : Fragment(), AdapterView.OnItemSelectedListener {
    companion object{
        fun newInstance() : WriteFragment {
            return WriteFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TEST","WriteFragment - onCreate")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("TEST","WriteFragment - onAttach")
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("TEST","WriteFragment - onCreateView")
        val view=inflater.inflate(R.layout.fragment_write, container, false)

        val bundle = Bundle()
        val bundle_arguments = arguments
        val username = bundle_arguments?.getString("username").toString()
        bundle.putString("username", username)

        val write_select_daily = view.findViewById<TextView>(R.id.write_select_daily)
        val write_select_free = view.findViewById<TextView>(R.id.write_select_free)
        val write_select_question = view.findViewById<TextView>(R.id.write_select_question)
        val write_select_hobby = view.findViewById<TextView>(R.id.write_select_hobby)
        val write_select_job = view.findViewById<TextView>(R.id.write_select_job)
        val write_select_support = view.findViewById<TextView>(R.id.write_select_support)

        write_select_daily.setOnClickListener {
            write_select_daily.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            write_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        write_select_free.setOnClickListener {
            write_select_daily.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            write_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        write_select_question.setOnClickListener {
            write_select_daily.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            write_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        write_select_hobby.setOnClickListener {
            write_select_daily.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            write_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        write_select_job.setOnClickListener {
            write_select_daily.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            write_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        write_select_support.setOnClickListener {
            write_select_daily.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
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
        Log.d("TEST", "WriteFragment - onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TEST", "WriteFragment - onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("TEST", "WriteFragment - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TEST", "WriteFragment - onDestroy")
    }
}