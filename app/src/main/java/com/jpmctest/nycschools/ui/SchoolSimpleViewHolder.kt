package com.jpmctest.nycschools.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jpmctest.nycschools.R
import com.jpmctest.nycschools.pojo.School

class SchoolSimpleViewHolder private constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    private val schoolItemView: TextView
    private var school: School? = null

    init {
        itemView.setOnClickListener { v: View ->
            val intent = Intent(v.context, SchoolActivity::class.java)
            intent.putExtra("School", school)
            v.context.startActivity(intent)
        }
        schoolItemView = itemView.findViewById(R.id.textView)
    }

    fun bind(school: School) {
        this.school = school
        schoolItemView.text = school.school_name
    }

    companion object {
        @JvmStatic
        fun create(parent: ViewGroup): SchoolSimpleViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.schools_simple_recyclerview_item, parent, false)
            return SchoolSimpleViewHolder(view)
        }
    }
}