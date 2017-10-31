package beyond_imagination.blubblub;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by laggu on 2017-07-06.
 */

/**
 * @author Yehun Park
 * @file Database.java
 * @breif This class is core of this application.
 * Store user setting value, read, update, delete user setting value in SQLite.
 */
public class Database {
    private static final String DBNAME = "blubblub";
    private static final String SETTINGTABLENAME = "setting";

    private static Activity activity;
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase db;

    /**
     * Set Database in application.
     * @param activity
     */
    public static void setDatabase(Activity activity){
        Database.activity = activity;

        databaseHelper = new DatabaseHelper();
        db = databaseHelper.getWritableDatabase();
    }

    /**
     * Create new setting row in Database.
     * @param setting
     */
    public static void insertRecord(Setting setting){
        try{
            SQLiteStatement p = db.compileStatement("insert into " + SETTINGTABLENAME + " values (?,?,?,?,?,?,?)");
            p.bindLong(1, 1);
            p.bindLong(2, setting.getAuto() ? 1 : 0);
            p.bindLong(3, setting.getFeed_cycle());
            p.bindLong(4, setting.getTmp_max());
            p.bindLong(5, setting.getTmp_min());
            p.bindLong(6, setting.getIllum_max());
            p.bindLong(7, setting.getIllum_min());

            p.execute();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Update user setting value in Database.
     * @param setting
     */
    public static void updateRecord(Setting setting){
        ContentValues recordValues = new ContentValues();
        recordValues.put("auto", setting.getAuto() ? 1 : 0);
        recordValues.put("feed_cycle", setting.getFeed_cycle());
        recordValues.put("tmp_max", setting.getTmp_max());
        recordValues.put("tmp_min", setting.getTmp_min());
        recordValues.put("illum_max", setting.getIllum_max());
        recordValues.put("illum_min", setting.getIllum_min());
        String[] whereArgs = {"1"};
        db.update(SETTINGTABLENAME, recordValues, "nu = ?", whereArgs);
    }

    /**
     * Delete user setting value in Database.
     * @param nu
     */
    public static void deleteRecord(int nu){
        String[] whereArgs = {String.valueOf(nu)};

        db.delete(SETTINGTABLENAME, "nu = ?", whereArgs);
    }

    /**
     * Read user setting value in Database. If there is not row in Database, create new row
     * @return
     */
    public static Setting readFromDatabase(){
        Setting setting = new Setting();
        Cursor c = null;
        try {
            c = db.rawQuery("select * from "+ SETTINGTABLENAME, null);
            int size = c.getCount();
            Log.i("db", ""+size);
            if(size <1){
                insertRecord(setting);

                return setting;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            c.moveToFirst();
            if (c.getInt(1)==1)
                setting.setAuto(true);
            else
                setting.setAuto(false);

            setting.setFeed_cycle(c.getInt(2));
            setting.setTmp_max(c.getInt(3));
            setting.setTmp_min(c.getInt(4));
            setting.setIllum_max(c.getInt(5)+1);
            setting.setIllum_min(c.getInt(6));
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "정보를 불러올수 없습니다.", Toast.LENGTH_SHORT).show();
        }
        return setting;
    }

    /**
     * Create new table in DB.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper{
        private DatabaseHelper(){
            super(activity, DBNAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("Database", "onCreate start");
            String sql = "create table " + SETTINGTABLENAME +
                    "(nu integer," +
                    "auto integer," +
                    "feed_cycle integer," +
                    "tmp_max integer," +
                    "tmp_min integer," +
                    "illum_max integer," +
                    "illum_min integer)";
            try{
                db.execSQL(sql);
            }
            catch (SQLException e){
                e.printStackTrace();
                Log.i("Database", "create fail");
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
