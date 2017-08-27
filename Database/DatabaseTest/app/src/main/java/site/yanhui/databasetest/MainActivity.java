package site.yanhui.databasetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import site.yanhui.database.MyDatabaseHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MyDatabaseHelper databaseHelper;

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //在使用数据库的时候，先拿到databaseHelper对象
//        databaseHelper = new MyDatabaseHelper(this, "BooksStore.db", null, 1);

        //升级数据库添加一个category表
        databaseHelper = new MyDatabaseHelper(this, "BooksStore.db", null, 2);


        Button CreateDatabase = (Button) findViewById(R.id.Create_database);
        CreateDatabase.setOnClickListener(this);

        Button AddData = (Button) findViewById(R.id.add_data);
        AddData.setOnClickListener(this);

        Button DeleteData= (Button) findViewById(R.id.delete_data);
        DeleteData.setOnClickListener(this);

        Button UpdateData = (Button) findViewById(R.id.update_data);
        UpdateData.setOnClickListener(this);

        Button QueryData= (Button) findViewById(R.id.query_data);
        QueryData.setOnClickListener(this);
//        CreateDatabase.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //在这里是调用getWritableDatabase 或者是read权限就能够创建出来了。
//                databaseHelper.getWritableDatabase();
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Create_database:
                //在这里是调用getWritableDatabase 或者是read权限就能够创建出来了。
                databaseHelper.getWritableDatabase();
                break;
            case R.id.add_data:
                SQLiteDatabase db = databaseHelper.getWritableDatabase();//拿到写的权限
                ContentValues values = new ContentValues();
                //开始组装第一条数据
                values.put("name", "The Da Vinci Code");
                values.put("author", "Dan Brown");
                values.put("pages", 454);
                values.put("price", 16.96);
                db.insert("Book", null, values);

                //插入第二条数据
                values.put("name", "人间失格");
                values.put("author", "太宰治");
                values.put("pages", 180);
                values.put("price", 20.01);
                db.insert("Book", null, values);
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                break;

            case R.id.update_data:
                /**
                 * 1.创建的数据库的权限
                 * 2.contentvalues添加数据
                 * 3.SQL 的update语句
                 */
                SQLiteDatabase dbUpdate = databaseHelper.getWritableDatabase();
                ContentValues valuesUpdate = new ContentValues();
                valuesUpdate.put("price", 10.99);
                dbUpdate.update("Book", valuesUpdate, "name=?", new String[]{"人间失格"});
                break;


            case R.id.delete_data:
                SQLiteDatabase dbDelete = databaseHelper.getWritableDatabase();
//                ContentValues valuesDelete = new ContentValues(); 不需要了，删除的话，直接用db操作就行了
                //三个参数，第一个传入待删除的表名，删除pages大于400的书
                dbDelete.delete("Book","pages>?",new String[]{"400"});

                break;

            case R.id.query_data:
                SQLiteDatabase dbQuery = databaseHelper.getWritableDatabase();
                //将表中的数据封装成一个cursor对象
                Cursor cursor = dbQuery.query("Book", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));

                        Log.d(TAG, "book name is "+name);
                        Log.d(TAG,"book author is "+ author);
                        Log.d(TAG,"book pages is "+ pages);
                        Log.d(TAG,"book price is "+ price);
                    }while (cursor.moveToNext());
                }
                cursor.close();
                break;
            default:
                break;
        }
    }
}
