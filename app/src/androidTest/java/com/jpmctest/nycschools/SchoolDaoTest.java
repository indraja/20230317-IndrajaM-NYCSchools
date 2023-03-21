package com.jpmctest.nycschools;

import android.content.Context;

import com.jpmctest.nycschools.database.SchoolDao;
import com.jpmctest.nycschools.database.SchoolRoomDatabase;
import com.jpmctest.nycschools.pojo.School;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SchoolDaoTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private SchoolDao schoolDao;
    private SchoolRoomDatabase db;


    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, SchoolRoomDatabase.class)
                .allowMainThreadQueries()
                .build();
        schoolDao = db.schoolDao();
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    @Test
    public void insertDeleteGetAll() throws Exception  {
        School school1 = new School("id_school_1");
        School school2 = new School("id_school_2");
        List<School> schools= new ArrayList<School>();
        schools.add(school1);
        schools.add(school2);
        schoolDao.insertAll(schools);
        List<School> allSchools = LiveDataTestUtil.getValue(schoolDao.getSchools());
        assertEquals(allSchools.get(0).getSchool_name(), school1.getSchool_name());
        assertEquals(allSchools.get(1).getSchool_name(), school2.getSchool_name());
        schoolDao.deleteAll();
        allSchools = LiveDataTestUtil.getValue(schoolDao.getSchools());
        assertTrue(allSchools.isEmpty());
    }

}