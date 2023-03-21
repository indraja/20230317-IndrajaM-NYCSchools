package com.jpmctest.nycschools;

import android.content.Context;

import com.jpmctest.nycschools.database.SATScoresDao;
import com.jpmctest.nycschools.database.SchoolRoomDatabase;
import com.jpmctest.nycschools.pojo.SATScores;

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
public class SATScoresDaoTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private SATScoresDao scoresDao;
    private SchoolRoomDatabase db;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, SchoolRoomDatabase.class)
                .allowMainThreadQueries()
                .build();
        scoresDao = db.satScoresDao();
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    /**
     * Testing insertion deletion of SATScores
     * @throws Exception
     */
    @Test
    public void getInsertAllDeleteAll() throws Exception  {
        SATScores score1 = new SATScores("id_school_1");
        SATScores score2 = new SATScores("id_school_2");
        List<SATScores> scores= new ArrayList();
        scores.add(score1);
        scores.add(score2);
        scoresDao.insertAll(scores);
        SATScores testScores = LiveDataTestUtil.getValue(scoresDao.getScore(score1.getDbn()));
        assertEquals(testScores.getDbn(), score1.getDbn());
        List<SATScores> allScores = LiveDataTestUtil.getValue(scoresDao.getAllScores());
        assertEquals(allScores.get(0).getDbn(), score1.getDbn());
        assertEquals(allScores.get(1).getDbn(), score2.getDbn());
        scoresDao.deleteAll();
        List<SATScores> allSchools = LiveDataTestUtil.getValue(scoresDao.getAllScores());
        assertTrue(allSchools.isEmpty());
    }
}