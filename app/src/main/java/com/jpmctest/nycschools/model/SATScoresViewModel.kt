package com.jpmctest.nycschools.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jpmctest.nycschools.pojo.SATScores
import com.jpmctest.nycschools.pojo.School
import com.jpmctest.nycschools.repository.SchoolRepository

class SATScoresViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository: SchoolRepository
    var score = MutableLiveData<String>();
    var school: School? = null

    init {
        mRepository = SchoolRepository.getRepository(application.applicationContext)
    }

    fun getScoresForSchool(schoolDBN: String?): LiveData<SATScores> {
        return mRepository.getSATScoresForSchool(schoolDBN)
    }
}