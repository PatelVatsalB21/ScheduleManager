package com.example.schedulemanager;//package com.example.firebase;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
//import androidx.annotation.Nullable;
//
//public class DBHelper extends SQLiteOpenHelper {
//    private String DATABASE_NAME = "ALLDATA.db";
//    private String TABLE_NAME = "DATASTRINGS";
//    public static final String COL_1 = "ID";
//    public static final String COL_2 = "DATA";
//
//    public DBHelper(@Nullable Context context) {
//        super(context, "ALLDATA", null, 1);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,DATA TEXTr )");
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
//
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        onCreate(db);
//    }
//
//    public boolean insertData(Integer id1, Integer id2, Integer id3, String Data1, String Data2, String Data3) {
//        SQLiteDatabase db = DBHelper.this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(COL_1, id1);
//        cv.put(COL_2, Data1);
//        cv.put(COL_1, id2);
//        cv.put(COL_2, Data2);
//        cv.put(COL_1, id3);
//        cv.put(COL_2, Data3);
//        long result = db.insert(TABLE_NAME, null, cv);
//        if (result == -1) return false;
//        else return true;
//    }
//
//    public Cursor getData(Integer id) {
//        SQLiteDatabase db = DBHelper.this.getWritableDatabase();
//        String query = "SELECT * FROM " + TABLE_NAME + " WHERE ID='" + id + "'";
//        Cursor cursor = db.rawQuery(query, null);
//        return cursor;
//    }
//
//    public boolean updateData(String id, String Data) {
//        SQLiteDatabase db = DBHelper.this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(COL_1, id);
//        cv.put(COL_2, Data);
//        db.update(TABLE_NAME, cv, "ID= ?", new String[]{id});
//        Log.d("UPDATEDATA", Data);
//        return true;
//    }
//
//    public Integer deleteData(String id) {
//        SQLiteDatabase db = DBHelper.this.getWritableDatabase();
//        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
//    }
//
//    public Cursor getAllData() {
//        SQLiteDatabase db = DBHelper.this.getWritableDatabase();
//        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
//        return res;
//    }
//}
