package com.example.pef.prathamopenschool;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "PrathamTabDB.db";
    public Utility Util;
    public SQLiteDatabase database;
    public SQLiteDatabase db;
    public ContentValues contentValues;
    public Context c;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 20);
        try {
            c = context;
            contentValues = new ContentValues();
            Util = new Utility();
        } catch (Exception e) {

        }

    }


    public SQLiteDatabase GetWriteableDatabaseInstance() {
        database = this.getWritableDatabase();
        return database;
    }

    public SQLiteDatabase GetReadableDatabaseInstance() {
        database = this.getReadableDatabase();
        return database;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(DatabaseInitialization.CreateAserTable);
            db.execSQL(DatabaseInitialization.CreateVillageTable);
            db.execSQL(DatabaseInitialization.CreateLogTable);
            db.execSQL(DatabaseInitialization.CreateGroupTable);
            db.execSQL(DatabaseInitialization.CreateStudentTable);
            db.execSQL(DatabaseInitialization.CreateScoreTable);
            db.execSQL(DatabaseInitialization.CreateAssessmentScoreTable);
            db.execSQL(DatabaseInitialization.CreateCRLTable);
            db.execSQL(DatabaseInitialization.CreateUserTypeTable);
            db.execSQL(DatabaseInitialization.InsertIndividualUserType);
            db.execSQL(DatabaseInitialization.InsertGroupUserType);
            db.execSQL(DatabaseInitialization.InsertAdminUserType);
            db.execSQL(DatabaseInitialization.InsertSuperAdminUserType);
            db.execSQL(DatabaseInitialization.CreateUserTable);
            db.execSQL(DatabaseInitialization.CreateSessionTable);
            db.execSQL(DatabaseInitialization.CreateStatusTable);
            db.execSQL(DatabaseInitialization.CreateAttendanceTable);
            db.execSQL(DatabaseInitialization.InsertToStatus);

        } catch (Exception e) {

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("Drop table if exists Village");
            db.execSQL("Drop table if exists Groups");
            db.execSQL("Drop table if exists Student");
            db.execSQL("Drop table if exists Logs");
            db.execSQL("Drop table if exists Scores");
            db.execSQL("Drop table if exists AssessmentScores");
            db.execSQL("Drop table if exists Status");
            db.execSQL("Drop table if exists Attendance");
            db.execSQL("Drop table if exists CRL");
            db.execSQL("Drop table if exists Aser");

            db.execSQL(DatabaseInitialization.CreateAserTable);
            db.execSQL(DatabaseInitialization.CreateCRLTable);
            db.execSQL(DatabaseInitialization.CreateVillageTable);
            db.execSQL(DatabaseInitialization.CreateLogTable);
            db.execSQL(DatabaseInitialization.CreateGroupTable);
            db.execSQL(DatabaseInitialization.CreateStudentTable);
            db.execSQL(DatabaseInitialization.CreateScoreTable);
            db.execSQL(DatabaseInitialization.CreateAssessmentScoreTable);
            db.execSQL(DatabaseInitialization.CreateStatusTable);
            db.execSQL(DatabaseInitialization.InsertToStatus);
            db.execSQL(DatabaseInitialization.CreateAttendanceTable);
        } catch (Exception e) {

        }
    }

}
