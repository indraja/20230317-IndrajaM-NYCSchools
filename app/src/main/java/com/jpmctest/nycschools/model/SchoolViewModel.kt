package com.jpmctest.nycschools.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jpmctest.nycschools.pojo.School
import com.jpmctest.nycschools.repository.SchoolRepository

class SchoolViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository: SchoolRepository
    val allSchools: LiveData<List<School>>

    init {
        mRepository = SchoolRepository.getRepository(application.applicationContext)
        allSchools = mRepository.allSchools
    }

    fun getFilteredSchools(searchString: String?): LiveData<List<School>> {
        return mRepository.getFilteredSchools(searchString)
    }

    fun loadSchools() {
        mRepository.loadSchools()
    }
}