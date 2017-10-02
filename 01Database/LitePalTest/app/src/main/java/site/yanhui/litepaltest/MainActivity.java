package site.yanhui.litepaltest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

import site.yanhui.litepaltest.DatabaseBean.Book;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button CreateDatabase = (Button) findViewById(R.id.Create_database);
        CreateDatabase.setOnClickListener(this);

        Button addDatabase = (Button) findViewById(R.id.add_data);
        addDatabase.setOnClickListener(this);

        Button updateDatebase= (Button) findViewById(R.id.update_data);
        updateDatebase.setOnClickListener(this);

        Button deleteDatabase = (Button) findViewById(R.id.delete_data);
        deleteDatabase.setOnClickListener(this);

        Button queryButton= (Button) findViewById(R.id.query_data);
        queryButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Create_database:
                Connector.getDatabase();
                break;

            case R.id.add_data:
                Book book= new Book();
                book.setName("人间失格");
                book.setAuthor("太宰治");
                book.setPages(454);
                book.setPrice(20.12);
                book.setId(10);
                book.setPress("Unknow");
                book.save();
                break;

            case R.id.update_data:
                Book book1 = new Book();
                book1.setName("斜阳");
                book1.setAuthor("太宰治");
                book1.setPrice(20.01);
                book1.setPages(999);
                book1.setPress("日本");
                book1.update(2);//需要改哪行的数据，就设置成哪行。
                break;

            case R.id.delete_data:
//                DataSupport.delete(Book.class,2); //删除表中id=2的数据
                DataSupport.deleteAll(Book.class,"price<?","108");


            case  R.id.query_data:
                List<Book> books= DataSupport.findAll(Book.class);

                for (Book book2 : books) {
                    Log.d(TAG, "=============================");
                    Log.d(TAG,"book name is "+ book2.getName());
                    Log.d(TAG, "book  author is "+book2.getAuthor());
                    Log.d(TAG, "book pages is "+ book2.getPages());
                    Log.d(TAG, "book price is " + book2.getPrice());
                    Log.d(TAG, "book.press is "+book2.getPress());
                }
                break;
            default:
                break;
        }
    }
}
