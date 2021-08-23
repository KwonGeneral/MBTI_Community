package com.kwon.mbti_community.qna.view

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

class QnaFragment : Fragment(), AdapterView.OnItemSelectedListener {
    companion object{
        fun newInstance() : QnaFragment {
            return QnaFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TEST","QnaFragment - onCreate")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("TEST","QnaFragment - onAttach")
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("TEST","QnaFragment - onCreateView")
        val view=inflater.inflate(R.layout.fragment_qna, container, false)

        val bundle = Bundle()
        val bundle_arguments = arguments
        val username = bundle_arguments?.getString("username").toString()
        bundle.putString("username", username);

        val qna_select_free = view.findViewById<TextView>(R.id.qna_select_free)
        val qna_select_question = view.findViewById<TextView>(R.id.qna_select_question)
        val qna_select_hobby = view.findViewById<TextView>(R.id.qna_select_hobby)
        val qna_select_job = view.findViewById<TextView>(R.id.qna_select_job)
        val qna_select_support = view.findViewById<TextView>(R.id.qna_select_support)

        qna_select_free.setOnClickListener {
            qna_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            qna_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        qna_select_question.setOnClickListener {
            qna_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            qna_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        qna_select_hobby.setOnClickListener {
            qna_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            qna_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        qna_select_job.setOnClickListener {
            qna_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            qna_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        qna_select_support.setOnClickListener {
            qna_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
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
        Log.d("TEST", "QnaFragment - onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TEST", "QnaFragment - onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("TEST", "QnaFragment - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TEST", "QnaFragment - onDestroy")
    }
}