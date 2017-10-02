package site.yanhui.notificationtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.File;

/**
 * Notification总结：
 * 1.使用V4包下的notificationCompat得到的notification是全兼容的版本的，v7包可能不兼容所有版本。
 * 2. .setStyle方法里，可以放一段bigText或者是BigPicture,只能2选1，如果手机不支持bigText，则使用的是setcontentText
 * 3.墙内尽量避免使用带google服务的模拟器，因为很多服务被墙拿不到。
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button SendNotification2 = (Button) findViewById(R.id.sendNotification2);
        SendNotification2.setOnClickListener(this);

        Button SendNotification1 = (Button) findViewById(R.id.sendNotification1);
        SendNotification1.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.sendNotification1:
                NotificationManager notificationManager1 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification notification1 = new NotificationCompat.Builder(this)
                        .setContentTitle("New Message")
                        .setContentText("You've received new messages.")
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.pig_meitu_3))
                        .build();
                notificationManager1.notify(1, notification1);
                break;

            case R.id.sendNotification2:

                //0.新建一个NotificationActivity
                //1.构建一个intent对象
                Intent intent = new Intent(this, NotificationActivity.class);

                //2.把intent放入进一个PendingIntent对象里面。
                //pend 延迟的意思。 因为我们必须要点了notification才应该跳转的。
                //第二个和第四个参数一般传入0即可
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                //3.拿到NotificationManager对象
                NotificationManager notificationManager3 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                //4.实例化一个Notification对象
                Notification notification3 = new NotificationCompat.Builder(this)
                        .setContentTitle("this is content title.")//设置title
                        .setContentText("Learn how to build notification,send and sysnc data ,and use voice actions . Get the official Android")//设置内容，内容过长就被...因为本身就是一个小推送
                        .setWhen(System.currentTimeMillis())//设置时间
                        .setSmallIcon(R.mipmap.ic_launcher_round)//设置状态栏小图标
                        // 要求接收的是一个bitmap对象所以使用bitmapFactory把int数转化为一个bitmap对象
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setAutoCancel(true)//点击了以后自动取消第一种取消的方式。
                        //6.设置一段提示音和震动（真机实现）
                        .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))//没找到这段提示音
                        .setVibrate(new long[]{0,1000,1000,1000})//震动一秒停一秒，震动一秒

                        //7.设置指示灯,三个参数，一个颜色，一个1s亮起，一个1s熄灭（真机）
                        .setLights(Color.GREEN,1000,1000)

                        //8.设置成系统默认铃声和震动（真机）
                        .setDefaults(NotificationCompat.DEFAULT_ALL)

                        //9.如果非要使用长文本推送的话使用如下操作，如果支持长文本操作就显示这段内容，如果不支持就显示setContentText里的内容
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(("Learn how to build notification,send and sysnc data ,and use voice actions . Get the official ")))

//                        setstyle只能2选1，如果都选了，后面选择的会覆盖前面的用法
//                        10.还可以插入一张大图片使用bitmapfactory得到一个bitmap对象
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.pig_meitu_3)))

                        .setContentIntent(pendingIntent)//要求传入一个pendingIntent对象

                        .setPriority(NotificationCompat.PRIORITY_MAX)//5.0，6.0，7.0都有用

                        .build();

                //5.调用通知的方法，发送这个notification
                notificationManager3.notify(3, notification3);
                break;
            default:
                break;
        }
    }
}
