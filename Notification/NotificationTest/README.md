## Notification的使用小结

### 什么是Notification？

直译为通知，看看官方给的说明是怎么说得吧。

![](http://ogtmd8elu.bkt.clouddn.com/201708300924_671.png)

大意是：一个类，用来表示一种持续化的通知效果，被展现在用户面前。使用NotificationManager来管理，使用NotificationCompat.Builder来创建。

### Notification的作用

Notification可以说是Android系统中比较有特色的一种功能，比如说，当某个应用希望给自己的用户发送一些信息，但是该应用又没有在前台运行，这样一来，就可以使用通知来实现，发出一条通知的时候，手机的状态栏中就会显示一个通知的图标用于告知用户一些信息。下拉状态栏以后可以看到详细的内容。

### Notification的基本用法

#### 效果图：

![](http://ogtmd8elu.bkt.clouddn.com/201709052036_383.gif)

以上就是notification效果图，

#### 代码实现

![](http://ogtmd8elu.bkt.clouddn.com/201709052036_915.png)

以上就是notification的核心实现代码，布局很简单，我就不贴出来了，在最后我都会放个源码的地址，强烈建议感兴趣的朋友，可以star下，自己钻研钻研。

1. 我们先使用Context 的getSystemService方法并传入一个NOTIFICATION_SERVICE字符串，这样就可以得到一个NotificationManager用于管理我们需要发送的通知。

2. 
  new NotificationCompat.Builder(this)构建出一个notification

3. ```java
   .setContentTitle()//设置标题
   .setSmallIcon()//设置通知的小图标
   .setLargeIcon()//设置通知大图标。
   .build()//构建出一个notification对象 
   //最后调用notificationManager的notify方法传入两个参数。
   //第一个参数该notification1的id
   //第二个参数传入构建出来了notification1  
   ```


### Notification的高级功能

相信你很快就会发现上面的通知是不可以点击，这不符合用户的操作习惯因此我们需要更进一步的学习通知的高级功能

#### 效果图

![](http://ogtmd8elu.bkt.clouddn.com/201709052112_200.gif)

#### 代码实现

![](http://ogtmd8elu.bkt.clouddn.com/201709052147_534.png)

以上就是通知的基本用法，相信你都会了。接着往下看

![](http://ogtmd8elu.bkt.clouddn.com/201709052147_20.png)

这里的代码就涉及到了一些必须在真机上才能实现的方法，比如说震动，和指示灯，值得一提的是，setDefault方法，传入以上的参数，就会得到一个默认值，默认当前手机的声音和震动的取值。

![](http://ogtmd8elu.bkt.clouddn.com/201709052148_111.png)

这里有个设置优先级的方式，我们传入了最大值，但是有些厂商去掉了这个方法，但是你放心，通知还是会收到的。

### 取消通知的第二种方式

![](http://ogtmd8elu.bkt.clouddn.com/201709052156_270.png)

在跳转过去的activity里面的使用通知管理器的cancel方法传入我们之前给notification的id即可取消。

两种取消的方式二选一即可。

### 踩过的坑

* 注意使用V4包下的notificationCompat得到的notification是全兼容的版本的，v7包可能不兼容所有版本。
* .setStyle方法里，可以放一段bigText或者是BigPicture,只能2选1，如果手机不支持bigText，则使用的是setcontentText。
* 墙内尽量避免使用带google服务的模拟器，因为很多服务被墙拿不到。

### 源码地址

有志者，事竟成。

[NotificationTest源码](https://github.com/YanHuiLi/AndroidOpenSourceFrameworkTest/tree/master/Notification/NotificationTest)