package com.jpmctest.nycschools.database;

import android.content.Context;

import com.jpmctest.nycschools.pojo.SATScores;
import com.jpmctest.nycschools.pojo.School;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {School.class, SATScores.class}, version = 1, exportSchema = false)
public abstract class SchoolRoomDatabase extends RoomDatabase {

    public abstract SchoolDao schoolDao();
    public abstract SATScoresDao satScoresDao();

    private static volatile SchoolRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 10;
    // Uses the Executer Service to run DB operations in background and concurrently
    public static final ExecutorService databaseWriteExecutor =  Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * DB Singleton
     * @param context
     * @return
     */
    public static SchoolRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SchoolRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SchoolRoomDatabase.class, "school_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Handle any setup after DB is created for the first time
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                SchoolDao schoolDao = INSTANCE.schoolDao();
                schoolDao.deleteAll();
                SATScoresDao scoresDao = INSTANCE.satScoresDao();
                scoresDao.deleteAll();
            });
        }
    };
}
