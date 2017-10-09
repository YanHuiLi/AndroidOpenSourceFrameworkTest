package site.yanhui.viewdispatchevent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import site.yanhui.viewdispatchevent.view.MyButton;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.btn_my_button)
    MyButton btnMyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        btnMyButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                switch (action)
                {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "onTouch ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.e(TAG, "onTouch ACTION_MOVE");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e(TAG, "onTouch ACTION_UP");
                        break;
                    default:
                        break;
                }

                return false;
            }
        });
    }


    @OnClick(R.id.btn_my_button)
    public void MyButtonClicked() {
//        Toast.makeText(this, "Mybutton is clicked", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "MyButtonClicked: MyButton");
    }
}

