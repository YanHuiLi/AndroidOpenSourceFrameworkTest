package site.yanhui.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Archer on 2017/8/24.
 * 创建数据库
 * <p>
 * integer 表示整型
 * real 表示浮点型
 * text  文本类型
 * blob  文本类型
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_BOOK = "create table Book("
            //特别注意逗号 隔开，看到sql语句，其实就是一个整长句，找规律。
            //创建的顺序的就是最终在数据库里面的呈现方式
            + "id integer primary key autoincrement,"
            + "author text,"
            + "price real,"
            + "pages integer,"
            + "name text)";

    public static final String CREATE_CATEGORY = "create table Category("
            + "id integer primary key autoincrement,"
            + "category_name text,"
            + "category_code integer)";


    private Context mContext;


    //看清这个构造传入一个上下文，和数据库的名字，第三个参数返回一个cursor一般传null，第四个参数是版本号
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        mContext = context;
    }

    //创建数据库
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);//创建第一个表
        Toast.makeText(mContext, "Create succeeded!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //在这里升级
        db.execSQL("drop table if exists Book");
        db.execSQL("drop table if exists Category");
        onCreate(db);
    }
}
