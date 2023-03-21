package com.jpmctest.nycschools.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.jpmctest.nycschools.R
import com.jpmctest.nycschools.databinding.ActivityNewSchoolBinding
import com.jpmctest.nycschools.model.SATScoresViewModel
import com.jpmctest.nycschools.pojo.SATScores
import com.jpmctest.nycschools.pojo.School

class SchoolActivity : AppCompatActivity() {
    private var schoolBundle: School? = null
    private var satScoresViewModel: SATScoresViewModel? = null
    lateinit var binding: ActivityNewSchoolBinding

    //private Binding
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_school)
        binding.setLifecycleOwner(this);

//        setContentView(R.layout.activity_new_school)
        val toolbar = findViewById<Toolbar>(R.id.school_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //School object to setup the page
        schoolBundle = intent.getParcelableExtra("School")
        val directionsButton = findViewById<ImageButton>(R.id.directionsButton)
        directionsButton.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:" + schoolBundle?.latitude + "," + schoolBundle?.longitude + "?q=" + schoolBundle?.school_name)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
        val websiteBUtton = findViewById<ImageButton>(R.id.websiteButton)
        websiteBUtton.setOnClickListener { v ->
            val websiteIntentUri = Uri.parse(schoolBundle?.website)
            val websiteIntent = Intent(Intent.ACTION_VIEW, websiteIntentUri)
            v.context.startActivity(Intent.createChooser(websiteIntent, "Browse with"))
        }
        val callButton = findViewById<ImageButton>(R.id.callButton)
        callButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + schoolBundle?.phone_number)
            startActivity(intent)
        }
        val emailButton = findViewById<ImageButton>(R.id.emailButton)
        emailButton.setOnClickListener { v ->
            val intent = Intent(Intent.ACTION_SEND)
            intent.data = Uri.parse("mailto:" + schoolBundle?.school_email)
            v.context.startActivity(Intent.createChooser(intent, "Email School "))
        }

        // Loads SAT Scores for the School
        satScoresViewModel = ViewModelProvider(this).get(SATScoresViewModel::class.java)
        binding.vm = satScoresViewModel
        satScoresViewModel?.apply {
            school = schoolBundle
            val score = getScoresForSchool(school?.dbn)
            score.observe(this@SchoolActivity) { satScores -> if (satScores != null)
                satScoresUpdated(score.value)
            }
        }

    }

    /**
     * Updates the SAT Scores once available from DB
     * @param score
     */
    private fun satScoresUpdated(score: SATScores?) {
        if (score != null) {
            binding.satScoresTextView.text = score.toString()
        }
    }
}