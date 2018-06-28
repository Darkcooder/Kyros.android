package ru.e_meet.kyros.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SQLAdapter {

    // Вводим используемые значения и переменные:
    public static final String DATABASE_NAME = "MY_DATABASE";
    public static final String DATABASE_TABLE = "MY_TABLE";
    public static final int DATABASE_VERSION = 1;
    public static final String KEY_CONTENT = "Content";

    // Создаем таблицу с данными MY_DATABASE:
    private static final String SCRIPT_CREATE_DATABASE = "CREATE TABLE if not exists '" + DATABASE_TABLE + "' ('key' text, 'value' text);";

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    public SQLAdapter(Context c){context = c;
    }

    // Читаем данные с базы данных:
    public SQLAdapter openToRead() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        return this;
    }

    // Записываем данные в базу данных:
    public SQLAdapter openToWrite() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    // Закрываем sqLiteHelper:
    public void close(){sqLiteHelper.close();
    }

    public void putMap(Map<String, Object> map){
        Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
        openToWrite();
        ContentValues contentValues = new ContentValues();
        deleteAll();
        while (entries.hasNext()) {
            Map.Entry<String, Object> entry = entries.next();
            contentValues.put("key", entry.getKey());
            contentValues.put("value", (String)entry.getValue());
            sqLiteDatabase.insert(DATABASE_TABLE, null, contentValues);

        }

    }

    // Вставляем введенное содержимое в базу:
    public long insert(String key, String content){
        ContentValues contentValues = new ContentValues();
        contentValues.put(key, content);
        return sqLiteDatabase.insert(DATABASE_TABLE, null, contentValues);
    }

    // Удаляем все содержимое базы данных:
    public int deleteAll(){return sqLiteDatabase.delete(DATABASE_TABLE, null, null);
    }

    public Map<String, Object> getMap(){
        openToRead();
        String[] columns = new String[]{"key", "value"};
        Cursor cursor = sqLiteDatabase.query(DATABASE_TABLE, columns,null, null, null, null, null);
        Map<String, Object> map=new HashMap<>();
        int keyIndex=cursor.getColumnIndex("key");
        int valueIndex=cursor.getColumnIndex("value");
        for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()){
            map.put(cursor.getString(keyIndex), cursor.getString(valueIndex));
        }
        close();
        return map;
    }

    // Делаем запрос на получение текстовых данных с базы, отображаемых в виде столбца:
    public String queueAll(){
        String[] columns = new String[]{KEY_CONTENT};
        Cursor cursor = sqLiteDatabase.query(DATABASE_TABLE, columns,null, null, null, null, null);
        String result = "";

        int index_CONTENT = cursor.getColumnIndex(KEY_CONTENT);
        for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()){
            result = result + cursor.getString(index_CONTENT) + "\n";
        }
        return result;
    }

    public class SQLiteHelper extends SQLiteOpenHelper {
        public SQLiteHelper(Context context, String name,CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // Метод, создающий таблицу для хранения данных:
        @Override
        public void onCreate(SQLiteDatabase db) {db.execSQL(SCRIPT_CREATE_DATABASE);
        }

        // Метод для обновления базы данных, оставим пустым:
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}