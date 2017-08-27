package site.yanhui.runtimepermissiontest;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final int CALL_PHONE_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button MakeCall = (Button) findViewById(R.id.make_call);

        MakeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首先进行的是动态权限适配
                //如果权限是没有授予的，那么授予权限。
                //否则就是直接调用方法
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest
                            .permission.CALL_PHONE)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("说明")
                                .setMessage("需要使用电话权限，进行电话测试")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(MainActivity.this, new
                                                String[]{android.Manifest.permission.CALL_PHONE}, CALL_PHONE_REQUEST_CODE);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .create()
                                .show();
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},CALL_PHONE_REQUEST_CODE );
                    }
                } else {
                    call();
                    Toast.makeText(MainActivity.this, "已经授权打电话", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }


    //RequestPermission的回调函数，上面的第三个参数，就是回调码
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CALL_PHONE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);//打电话
            //因为是调用的危险权限，所以需要捕获一场
            //快简介 ctrl+alt +t
            intent.setData(Uri.parse("tel:10086")); //拨打10086
            startActivity(intent);
        } catch (SecurityException e) {// 安全异常
            e.printStackTrace();
        }
    }
}
