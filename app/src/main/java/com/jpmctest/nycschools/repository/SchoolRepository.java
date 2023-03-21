package com.jpmctest.nycschools.repository;

import android.content.Context;

import com.jpmctest.nycschools.pojo.SATScores;
import com.jpmctest.nycschools.database.SATScoresDao;
import com.jpmctest.nycschools.pojo.School;
import com.jpmctest.nycschools.database.SchoolDao;
import com.jpmctest.nycschools.database.SchoolRoomDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SchoolRepository {
    private SchoolDao mSchoolDao;
    private SATScoresDao mScoresDao;
    private LiveData<List<School>> mAllSchools;
    private final static String NYC_SCHOOLS_URL = "https://data.cityofnewyork.us/resource/s3k6-pzi2.json";
    private final static String NYC_SCHOOLS_SAT_SCORES_URL = "https://data.cityofnewyork.us/resource/f9bf-2cp4.json";

    private static volatile SchoolRepository INSTANCE;

    SchoolRepository(Context context) {
        SchoolRoomDatabase db = SchoolRoomDatabase.getDatabase(context);
        mSchoolDao = db.schoolDao();
        mScoresDao = db.satScoresDao();
        mAllSchools = mSchoolDao.getSchools();
    }

    /**
     * Singleton Instance
     *
     * @param context
     * @return
     */
    public static SchoolRepository getRepository(final Context context) {
        if (INSTANCE == null) {
            synchronized (SchoolRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SchoolRepository(context);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Will fetch Schools list as LiveData so that it can be executed in the background
     *
     * @return
     */
    public LiveData<List<School>> getAllSchools() {
        return mAllSchools;
    }

    /**
     * Search in DB
     *
     * @param searchString
     * @return
     */
    public LiveData<List<School>> getFilteredSchools(String searchString) {
        return mSchoolDao.getSchoolsFiltered(searchString);
    }

    /**
     * Get SATScores for School DBN
     *
     * @param schoolDBN
     * @return
     */
    public LiveData<SATScores> getSATScoresForSchool(String schoolDBN) {
        return mScoresDao.getScore(schoolDBN);
    }

    /**
     * Insert Schools into DB in background
     *
     * @param schools
     */
    void insertAll(List<School> schools) {
        SchoolRoomDatabase.databaseWriteExecutor.execute(() -> {
            mSchoolDao.insertAll(schools);
        });
    }

    /**
     * Insert Scores into DB in background
     *
     * @param scores
     */
    void insertAllScores(List<SATScores> scores) {
        SchoolRoomDatabase.databaseWriteExecutor.execute(() -> {
            mScoresDao.insertAll(scores);
        });
    }

    /**
     * From here lies all code related to REST API calls using OKHTTP.
     * We can put them in another class to handle them.
     */

    public void loadSchools() {
        fetchSchoolsData();
        fetchSATScores();
    }

    /**
     * Fetches School Data from NYC Schools API
     */
    void fetchSchoolsData() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(NYC_SCHOOLS_URL)
                .method("GET", null)
                .build();
        try {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String jsonData = response.body().string();
                    // Load data as School Object using Gson
                    Type listType = new TypeToken<List<School>>() {
                    }.getType();
                    List<School> schools = new Gson().fromJson(jsonData, listType);
                    insertAll(schools);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches SATScores Data from NYC Schools API
     */
    void fetchSATScores() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(NYC_SCHOOLS_SAT_SCORES_URL)
                .method("GET", null)
                .build();
        try {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String jsonData = response.body().string();
                    // Load data as SATScores Object using Gson
                    Type listType = new TypeToken<List<SATScores>>() {
                    }.getType();
                    List<SATScores> satScores = new Gson().fromJson(jsonData, listType);
                    insertAllScores(satScores);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
