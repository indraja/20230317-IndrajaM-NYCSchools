package com.jpmctest.nycschools.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jpmctest.nycschools.R
import com.jpmctest.nycschools.model.SchoolViewModel
import com.jpmctest.nycschools.pojo.School
import com.jpmctest.nycschools.ui.SchoolListAdapter.SchoolDiff
import java.util.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private var mSchoolViewModel: SchoolViewModel? = null
    private var adapter: SchoolListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        adapter = SchoolListAdapter(SchoolDiff())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        mSchoolViewModel = ViewModelProvider(this).get(SchoolViewModel::class.java)

        // Update the cached copy of the words in the adapter.
        mSchoolViewModel?.allSchools?.observe(this) { list: List<School?> ->
            adapter?.submitList(
                list
            )
        }
        mSchoolViewModel?.loadSchools()
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    @SuppressLint("ResourceType")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.layout.menu, menu)
        val search = menu.findItem(R.id.action_search)
        val searchView = search.actionView as SearchView?
        searchView?.setOnQueryTextListener(this)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_refresh) {
            mSchoolViewModel?.loadSchools()
            return true
        } else if (id == R.id.action_search) {
            val searchView = item.actionView as SearchView?
            searchView?.setOnQueryTextListener(this)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        // Updates Search filter and runs DB Query using search string non capitalized.
        // We can additional for the entire Data instead
        var newText = newText
        newText = "%" + newText.lowercase(Locale.getDefault()) + "%"
        mSchoolViewModel?.getFilteredSchools(newText)?.observe(this) { list: List<School?> -> adapter?.submitList(list) }
        return true
    }
}