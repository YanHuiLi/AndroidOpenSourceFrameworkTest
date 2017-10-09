---
title: 事件分发机制原理分析(案例+源码)-View篇
top: 30
date: 2017-10-03 08:20:52
tags: [事件分发,view]
categories: Android
---

---

这篇文章用于记录我在学习Android事件分发的一些小感悟，在我自己学习的过程当中，我发现android事件分发这一块是一个非常关键的点，因为在自定义控件里面经常需要使用到，但是，现在普遍的博文，要么写得太晦涩，太难懂，要么就是好像是自己能用越高大上的词语表述就越显得自己牛，或者通俗到对于一个新手而言，根本看不懂的地步，总之就是看了等于白看，浪费时间。因此，我自己觉得应该写一篇，让初来乍到的人，看得懂的博文，告诉后来的人，为什么会这样子。也算为开源社区，尽一点绵薄之力了。

<!--more-->

### 对于事件分发机制的整体理解

什么是事件分发呢，从词义上去理解，就是一个一个的事件分别发送出去，在我们这篇博文里面，主要介绍的对象就是MotionEvent（点击事件）一系列操作，引起的分发过程。整体总是很抽象，是吧。不要紧，往下看。

### 业务场景需求还原

考虑下面一个场景，有时候在开发过程中，比如在一个自定义的Item里面放了一个checkBox，这个时候，我们需要点击Item的时候，顺便选中checkBox，然后对Item条目上的文字进行更新。很简单，为item设置一个OnclickListener，并把checkbox的ischeck方法作为返回值传给Item的ischeck，然后更改item上的条目，就行了呗。一切都是那么自然，但是很快业务就出现问题了，那就是当我们点击checkBox的时候，checkbox被选中，当时，item上的文字却没有更新。一图胜千言。

![](http://ogtmd8elu.bkt.clouddn.com/201710030845_172.png)

很常见的业务需求，但是为什么当我们点击checkBox的时候，虽然checkbox切换成为了ischeck的状态，但是文字没有更新呢，这就涉及到了，今天要分析的内容，就是因为点击事件最终传递到了， Item的子view，checkBox里面，而更新item的文字的业务逻辑代码是写在Item里面的，所以当然没法更新了。这里只是简单提一下，什么业务下会经常处理这个问题。因为要讲清楚这个问题，需要重新开一篇博文了，还是比较有价值的，有时间我会整理一篇出来的。今天就先分析View的事件分发。

### 案例分析

在这里需要提一下，我对代码世界的整体理解，那就是代码世界本质上也是人生活世界的一种投射，所以很多设计本质上都是遵循人在日常生活中的一些基本规律，只是站在了一个更高的角度上去用一门语言去描述罢了。所以开始我们的代码吧。

#### 新建一个自定义的button

```java
package site.yanhui.viewdispatchevent.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Archer on 2017/10/2.
 * <p>
 * 功能描述： 自定义一个button，用来理解view的事件分发机制
 *
 */

public class MyButton extends android.support.v7.widget.AppCompatButton {

    private static final String TAG = "MyButton";
    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        int action = event.getAction();

        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "dispatchTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "dispatchTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "dispatchTouchEvent ACTION_UP");
                break;

            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = event.getAction();

        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "onTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "onTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "onTouchEvent ACTION_UP");
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

}
```

#### 在xml中引用

```java
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="site.yanhui.viewdispatchevent.MainActivity">


<site.yanhui.viewdispatchevent.view.MyButton
    android:id="@+id/btn_my_button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="MyButton"/>

</LinearLayout>
```

#### 最后看一下MainActivity

```java
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
```

很简单的布局，照着默认的来就行，然后给button设置一个touch和onclick的监听事件就可以了。接下来我们就可以开始分析了。

```java
//down
10-03 01:17:24.581 16564-16564/site.yanhui.viewdispatchevent E/MyButton: dispatchTouchEvent ACTION_DOWN
10-03 01:17:24.581 16564-16564/site.yanhui.viewdispatchevent E/MainActivity: onTouch ACTION_DOWN
10-03 01:17:24.581 16564-16564/site.yanhui.viewdispatchevent E/MyButton: onTouchEvent ACTION_DOWN

//move
10-03 01:17:24.644 16564-16564/site.yanhui.viewdispatchevent E/MyButton: dispatchTouchEvent ACTION_MOVE
10-03 01:17:24.646 16564-16564/site.yanhui.viewdispatchevent E/MainActivity: onTouch ACTION_MOVE
10-03 01:17:24.646 16564-16564/site.yanhui.viewdispatchevent E/MyButton: onTouchEvent ACTION_MOVE

//move
10-03 01:17:24.664 16564-16564/site.yanhui.viewdispatchevent E/MyButton: dispatchTouchEvent ACTION_MOVE
10-03 01:17:24.664 16564-16564/site.yanhui.viewdispatchevent E/MainActivity: onTouch ACTION_MOVE
10-03 01:17:24.664 16564-16564/site.yanhui.viewdispatchevent E/MyButton: onTouchEvent ACTION_MOVE

//move
10-03 01:17:24.721 16564-16564/site.yanhui.viewdispatchevent E/MyButton: dispatchTouchEvent ACTION_MOVE
10-03 01:17:24.722 16564-16564/site.yanhui.viewdispatchevent E/MainActivity: onTouch ACTION_MOVE
10-03 01:17:24.722 16564-16564/site.yanhui.viewdispatchevent E/MyButton: onTouchEvent ACTION_MOVE

//up
10-03 01:17:24.723 16564-16564/site.yanhui.viewdispatchevent E/MyButton: dispatchTouchEvent ACTION_UP
10-03 01:17:24.723 16564-16564/site.yanhui.viewdispatchevent E/MainActivity: onTouch ACTION_UP
10-03 01:17:24.724 16564-16564/site.yanhui.viewdispatchevent E/MyButton: onTouchEvent ACTION_UP

//onclick
10-03 01:17:24.728 16564-16564/site.yanhui.viewdispatchevent D/MainActivity: MyButtonClicked: MyButton
```

注意看输出，event.getAction()的值有三个，012分别对应了，按下，移动，抬起三种状态。仔细看上面的代码，当我们给Button设置了touch的监听以后，onclick方法是最后才执行的。也就是说是紧跟up这个动作之后，从现实规律的角度来考虑，我们应该分解一下点击事件，分别是按下（down），抬起（up），中间可能手抖一下，就出现了移动（move），然后才会完成点击的这次事件。因此我们的log才会打印出数据。所以touch方法的优先顺序肯定是高于click方法的，因为click方法本质上是一系列touch方法结束以后的回调。

接下来我们看一下，我们针对上面的代码看一下具体的执行顺序。

首先执行的dispatchTouchEvent >onTouch > onTouchEvent 。

然后我们把touch方法的返回值设置为true看看有什么效果。

```java
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

            return true;//设置为true，之前为false，运行一下程序
        }
    });
}
```

结果：

```java
//down
10-03 02:23:20.417 12088-12088/site.yanhui.viewdispatchevent E/MyButton: dispatchTouchEvent ACTION_DOWN
10-03 02:23:20.418 12088-12088/site.yanhui.viewdispatchevent E/MainActivity: onTouch ACTION_DOWN

//move
10-03 02:23:20.423 12088-12088/site.yanhui.viewdispatchevent E/MyButton: dispatchTouchEvent ACTION_MOVE
10-03 02:23:20.423 12088-12088/site.yanhui.viewdispatchevent E/MainActivity: onTouch ACTION_MOVE

10-03 02:23:20.501 12088-12088/site.yanhui.viewdispatchevent E/MyButton: dispatchTouchEvent ACTION_MOVE
10-03 02:23:20.501 12088-12088/site.yanhui.viewdispatchevent E/MainActivity: onTouch ACTION_MOVE

10-03 02:23:20.517 12088-12088/site.yanhui.viewdispatchevent E/MyButton: dispatchTouchEvent ACTION_MOVE
10-03 02:23:20.517 12088-12088/site.yanhui.viewdispatchevent E/MainActivity: onTouch ACTION_MOVE

10-03 02:23:20.535 12088-12088/site.yanhui.viewdispatchevent E/MyButton: dispatchTouchEvent ACTION_MOVE
10-03 02:23:20.535 12088-12088/site.yanhui.viewdispatchevent E/MainActivity: onTouch ACTION_MOVE

10-03 02:23:20.552 12088-12088/site.yanhui.viewdispatchevent E/MyButton: dispatchTouchEvent ACTION_MOVE
10-03 02:23:20.552 12088-12088/site.yanhui.viewdispatchevent E/MainActivity: onTouch ACTION_MOVE

10-03 02:23:20.568 12088-12088/site.yanhui.viewdispatchevent E/MyButton: dispatchTouchEvent ACTION_MOVE
10-03 02:23:20.568 12088-12088/site.yanhui.viewdispatchevent E/MainActivity: onTouch ACTION_MOVE

//up
10-03 02:23:20.568 12088-12088/site.yanhui.viewdispatchevent E/MyButton: dispatchTouchEvent ACTION_UP
10-03 02:23:20.568 12088-12088/site.yanhui.viewdispatchevent E/MainActivity: onTouch ACTION_UP
```

看一下，发现，OnTouchEvent方法就没有执行了，仔细看看，onclick方法也没有执行了，也就是说，当我们把Touch方法的返回值设置为true的时候，首先是onTouchEvent方法失效，然后导致了onclick方法失效了，也就是OnTouchEvent方法里面肯定存在一个onclick回调函数，让我们在mainActivity里面实现，当我们ontouch置为true以后，导致了事件分发的中断。（事件被消费了）

首先你需要知道一点，只要你触摸到了任何一个控件，就一定会调用该控件的dispatchTouchEvent方法。那当我们去点击按钮的时候，就会去调用Button类里的dispatchTouchEvent方法，可是你会发现Button类里并没有这个方法，那么就到它的父类TextView里去找一找，你会发现TextView里也没有这个方法，那没办法了，只好继续在TextView的父类View里找一找，这个时候你终于在View里找到了这个方法。这是什么呢，这就是继承，在源码设计里面的体现，大大的减少了代码的冗余性

![](http://ogtmd8elu.bkt.clouddn.com/201710031036_432.png)

也就是说问题的根源就在这个最先执行的dispatchTouchEvent里面。图来自

### 源码分析

#### View的dispatchTouchEvent方法

```java
//行号在9988的地方
/**
 * Pass the touch screen motion event down to the target view, or this
 * view if it is the target.
 *
 * @param event The motion event to be dispatched.
 * @return True if the event was handled by the view, false otherwise.
 */
public boolean dispatchTouchEvent(MotionEvent event) {
    // If the event should be handled by accessibility focus first.
    if (event.isTargetAccessibilityFocus()) {
        // We don't have focus or no virtual descendant has it, do not handle the event.
        if (!isAccessibilityFocusedViewOrHost()) {
            return false;
        }
        // We have focus and got the event, then use normal event dispatch.
        event.setTargetAccessibilityFocus(false);
    }

  //第一个注意的点，初始值为false
    boolean result = false;

    if (mInputEventConsistencyVerifier != null) {
        mInputEventConsistencyVerifier.onTouchEvent(event, 0);
    }

    final int actionMasked = event.getActionMasked();
    if (actionMasked == MotionEvent.ACTION_DOWN) {
        // Defensive cleanup for new gesture
        stopNestedScroll();
    }

    if (onFilterTouchEventForSecurity(event)) {
        if ((mViewFlags & ENABLED_MASK) == ENABLED && handleScrollBarDragging(event)) {
            result = true;
        }
     //第二个注意点
        //noinspection SimplifiableIfStatement
        ListenerInfo li = mListenerInfo;
        if (li != null && li.mOnTouchListener != null
                && (mViewFlags & ENABLED_MASK) == ENABLED
                && li.mOnTouchListener.onTouch(this, event)) {
            result = true;
        }

      //第三个注意的点
        if (!result && onTouchEvent(event)) {
            result = true;
        }
    }

    if (!result && mInputEventConsistencyVerifier != null) {
        mInputEventConsistencyVerifier.onUnhandledEvent(event, 0);
    }

    // Clean up after nested scrolls if this is the end of a gesture;
    // also cancel it if we tried an ACTION_DOWN but we didn't want the rest
    // of the gesture.
    if (actionMasked == MotionEvent.ACTION_UP ||
            actionMasked == MotionEvent.ACTION_CANCEL ||
            (actionMasked == MotionEvent.ACTION_DOWN && !result)) {
        stopNestedScroll();
    }
c
}
```

可以看到，相比于5.0的源码，我使用的8.0的源码作为分析，可以看到家里很多部分，顺着下来仔细看看，什么一致性确认，安全性，手势什么的等的单词，说明之后的android的安全性的确做了很多的优化，看不懂没关系，这些不是我们关心的。

#### 行号20

```java
 boolean result = false;
```

result的初始值是false。



#### 行号64

```java
    return result;
```

看到这个方法最终返回的值是一个result，也就是说只要result一旦被赋值就会被立马返回出去。

#### 行号37-44

```java
    //noinspection SimplifiableIfStatement
    ListenerInfo li = mListenerInfo;
    if (li != null && li.mOnTouchListener != null
            && (mViewFlags & ENABLED_MASK) == ENABLED
            && li.mOnTouchListener.onTouch(this, event)) {
        result = true;
    }
```
 我们看到了一个if语句（40到42行），里面有四个条件的判断。

* li != nul
* li.mOnTouchListener != null
* (mViewFlags & ENABLED_MASK) == ENABLED
* li.mOnTouchListener.onTouch(this, event)

当这四个值的返回值都为真的时候，result置为true

**第一个条件**`ListenerInfo li = mListenerInfo;`决定了第一行条件为true。

**第二个条件**，我们看一下，在view中找到如下方法

```java
/**
 * Register a callback to be invoked when a touch event is sent to this view.
 * @param l the touch listener to attach to this view
 */
public void setOnTouchListener(OnTouchListener l) {
    getListenerInfo().mOnTouchListener = l;
}
```

setOnTouchListener，是不是有点眼熟啊，对啊，这不是我们在mainActivity里面设置的嘛，所以肯定不为空啊

**第三个条件**(mViewFlags & ENABLED_MASK) == ENABLED是判断当前点击的控件是否是enable的，按钮默认都是enable的，因此这个条件恒定为true。

**第四个条件**li.mOnTouchListener.onTouch(this, event)，看它调用的是onTouch方法，当然要跟进去看看咯。

```java
public interface OnTouchListener {
    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *        the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    boolean onTouch(View v, MotionEvent event);
}
```

找到了吧，我们在给button设置 OnTouchListener的时候，当然要实现这个onTouch方法了，读读注释，当返回true的时候，这个事件就被消费了，所以就满足了四个条件为真，后面的代码就不会再执行了。

### 小结一下

分析到这里，先小结一下。如果你想要让一个可以点击的控件禁用其点击事件，那么需要把dispatchTouchEvent的返回值置为true就行了，那么到底是要怎么把它置为true，就是把OnTouchListener的回调OnTouch方法的返回值置为true。因为一旦OnTouch方法返回true，四个条件全部满足，result=true，直接返回出去了，OnTouchEvent方法就不会再执行，而Onclick方法的回调是在OntouchEvent方法中得到的回调。所以不执行就没有Onclick方法。如果你能看懂我现在得到的结论，说明你已经很好的理解了，我谈及的内容，如果不理解，回头多读几遍，接下来我们当然不希望控件的Ontouch方法返回true了，因此还是置为false，这样我们才可以继续分析源码的调用过程。

### 行号47-49

```java
if (!result && onTouchEvent(event)) {
            result = true;
        }

```

看到这里是不是很明了了，首先需要明白一点，代码能够走到这里，说明了，Ontouch方法一定是返回的false，因为一旦Ontouch方法返回的是true那么直接弹出去了，因此说明，result还是false，if的两个条件第一个肯定为真了，就看第二个的onTouchEvent的返回值。

### onTouchEvent(event)源码分析

```java
/**
 * Implement this method to handle touch screen motion events.
 * <p>
 * If this method is used to detect click actions, it is recommended that
 * the actions be performed by implementing and calling
 * {@link #performClick()}. This will ensure consistent system behavior,
 * including:
 * <ul>
 * <li>obeying click sound preferences
 * <li>dispatching OnClickListener calls
 * <li>handling {@link AccessibilityNodeInfo#ACTION_CLICK ACTION_CLICK} when
 * accessibility features are enabled
 * </ul>
 *
 * @param event The motion event.
 * @return True if the event was handled, false otherwise.
 */
public boolean onTouchEvent(MotionEvent event) {
    final float x = event.getX();
    final float y = event.getY();
    final int viewFlags = mViewFlags;
    final int action = event.getAction();

    if ((viewFlags & ENABLED_MASK) == DISABLED) {
        if (action == MotionEvent.ACTION_UP && (mPrivateFlags & PFLAG_PRESSED) != 0) {
            setPressed(false);
        }
        // A disabled view that is clickable still consumes the touch
        // events, it just doesn't respond to them.
        return (((viewFlags & CLICKABLE) == CLICKABLE
                || (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE)
                || (viewFlags & CONTEXT_CLICKABLE) == CONTEXT_CLICKABLE);
    }
    if (mTouchDelegate != null) {
        if (mTouchDelegate.onTouchEvent(event)) {
            return true;
        }
    }

    if (((viewFlags & CLICKABLE) == CLICKABLE ||
            (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE) ||
            (viewFlags & CONTEXT_CLICKABLE) == CONTEXT_CLICKABLE) {
        switch (action) {
            case MotionEvent.ACTION_UP:
                boolean prepressed = (mPrivateFlags & PFLAG_PREPRESSED) != 0;
                if ((mPrivateFlags & PFLAG_PRESSED) != 0 || prepressed) {
                    // take focus if we don't have it already and we should in
                    // touch mode.
                    boolean focusTaken = false;
                    if (isFocusable() && isFocusableInTouchMode() && !isFocused()) {
                        focusTaken = requestFocus();
                    }

                    if (prepressed) {
                        // The button is being released before we actually
                        // showed it as pressed.  Make it show the pressed
                        // state now (before scheduling the click) to ensure
                        // the user sees it.
                        setPressed(true, x, y);
                   }

                    if (!mHasPerformedLongPress && !mIgnoreNextUpEvent) {
                        // This is a tap, so remove the longpress check
                        removeLongPressCallback();

                        // Only perform take click actions if we were in the pressed state
                        if (!focusTaken) {
                            // Use a Runnable and post this rather than calling
                            // performClick directly. This lets other visual state
                            // of the view update before click actions start.
                            if (mPerformClick == null) {
                                mPerformClick = new PerformClick();
                            }
                            if (!post(mPerformClick)) {
                                performClick();
                            }
                        }
                    }

                    if (mUnsetPressedState == null) {
                        mUnsetPressedState = new UnsetPressedState();
                    }

                    if (prepressed) {
                        postDelayed(mUnsetPressedState,
                                ViewConfiguration.getPressedStateDuration());
                    } else if (!post(mUnsetPressedState)) {
                        // If the post failed, unpress right now
                        mUnsetPressedState.run();
                    }

                    removeTapCallback();
                }
                mIgnoreNextUpEvent = false;
                break;

            case MotionEvent.ACTION_DOWN:
                mHasPerformedLongPress = false;

                if (performButtonActionOnTouchDown(event)) {
                    break;
                }

                // Walk up the hierarchy to determine if we're inside a scrolling container.
                boolean isInScrollingContainer = isInScrollingContainer();

                // For views inside a scrolling container, delay the pressed feedback for
                // a short period in case this is a scroll.
                if (isInScrollingContainer) {
                    mPrivateFlags |= PFLAG_PREPRESSED;
                    if (mPendingCheckForTap == null) {
                        mPendingCheckForTap = new CheckForTap();
                    }
                    mPendingCheckForTap.x = event.getX();
                    mPendingCheckForTap.y = event.getY();
                    postDelayed(mPendingCheckForTap, ViewConfiguration.getTapTimeout());
                } else {
                    // Not inside a scrolling container, so show the feedback right away
                    setPressed(true, x, y);
                    checkForLongClick(0, x, y);
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                setPressed(false);
                removeTapCallback();
                removeLongPressCallback();
                mInContextButtonPress = false;
                mHasPerformedLongPress = false;
                mIgnoreNextUpEvent = false;
                break;

            case MotionEvent.ACTION_MOVE:
                drawableHotspotChanged(x, y);

                // Be lenient about moving outside of buttons
                if (!pointInView(x, y, mTouchSlop)) {
                    // Outside button
                    removeTapCallback();
                    if ((mPrivateFlags & PFLAG_PRESSED) != 0) {
                        // Remove any future long press/tap checks
                        removeLongPressCallback();

                        setPressed(false);
                    }
                }
                break;
        }

        return true;
    }

    return false;
}
```

#### 行号24-33



```java
 if ((viewFlags & ENABLED_MASK) == DISABLED) {
        if (action == MotionEvent.ACTION_UP && (mPrivateFlags & PFLAG_PRESSED) != 0) {
            setPressed(false);
        }
        // A disabled view that is clickable still consumes the touch
        // events, it just doesn't respond to them.
        return (((viewFlags & CLICKABLE) == CLICKABLE
                || (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE)
                || (viewFlags & CONTEXT_CLICKABLE) == CONTEXT_CLICKABLE);
    }
```
注释写的很清楚了，即便是一disabled的view，只要被点击了仍然会消费点击事件。

#### 行号34-38

```java
if (mTouchDelegate != null) {
        if (mTouchDelegate.onTouchEvent(event)) {
            return true;
        }
    }
```

如果有代理就交给代理去做

#### 行号40-148

这个部分就相当于，剩下的代码部分，就是我们需要重点看的部分了。

```java
 if (((viewFlags & CLICKABLE) == CLICKABLE ||
            (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE) ||
            (viewFlags & CONTEXT_CLICKABLE) == CONTEXT_CLICKABLE)
```

先判断这个view是不是可以点击，可以的话就进入到if循环里面，后面陆续做一些获取焦点的判断和方法，但这并不是我们的重点，我们继续往前更近。

#### 行号67-76

```java
                           if (!focusTaken) {
                            // Use a Runnable and post this rather than calling
                            // performClick directly. This lets other visual state
                            // of the view update before click actions start.
                            if (mPerformClick == null) {
                                mPerformClick = new PerformClick();
                            }
                            if (!post(mPerformClick)) {
                                performClick();
                            }
                        }
```

首先一路看着下来，我们发现focusTaken为false，置反以后呢，恒为true，也就是说，当一个view有焦点并且点击的时候，首先会去看看这个mPerformClick的情况，如果为为空就闯建一个，然后post过去，最终会调用到一个performClick()方法，更进去看一眼。

#### performClick()

```java
 /**
     * Call this view's OnClickListener, if it is defined.  Performs all normal
     * actions associated with clicking: reporting accessibility event, playing
     * a sound, etc.
     *
     * @return True there was an assigned OnClickListener that was called, false
     *         otherwise is returned.
     */
    public boolean performClick() {
        final boolean result;
        final ListenerInfo li = mListenerInfo;
        if (li != null && li.mOnClickListener != null) {
            playSoundEffect(SoundEffectConstants.CLICK);
            li.mOnClickListener.onClick(this);
            result = true;
        } else {
            result = false;
        }

        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
        return result;
    }
```

看了上面这个方法，是不是觉得有点眼熟啊，尤其是if里面的几个条件，当全部满足的时候，执行的是` li.mOnClickListener.onClick(this);`，当然得跟进去看看了。

```java
 public interface OnClickListener {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        void onClick(View v);
    }
```

现在是不是很明白了，这就是说，当我们给一个控件绑定OnClickListener的时候会调用的到的onClick方法，聊到这里，需要特别注意的是，这个方法，是在up（手抬起来）以后触发的，这也就解释了，为什么onclic方法为什么会在更在后面，逻辑很清晰。

```java
 /**
     * Register a callback to be invoked when this view is clicked. If this view is not
     * clickable, it becomes clickable.
     *
     * @param l The callback that will run
     *
     * @see #setClickable(boolean)
     */
    public void setOnClickListener(@Nullable OnClickListener l) {
        if (!isClickable()) {
            setClickable(true);
        }
        getListenerInfo().mOnClickListener = l;
    }
```

这就是给一个view绑定setOnClickListener的过程，这也就保证了`li.mOnClickListener!=null`的条件，因为我们在MainActivity里面绑定的，也实现了onClick方法。

#### 行号150

这里还需要注意一点，就是在onTouchEvent方法里面，只要能够进行action的判断，那么最终的返回值是true，而且onTouchEvent的返回值也决定了dispatchTouchEvent的返回值，也就是说决定是事件是否被消费了。在这里的demo的控件的的传递中，当然是被这个button消费了。所以调用了onTouchEvent方法。分析到这里，相信你也明白，这个事件分发是什么情况了。

#### 参考文献和相关推荐

[郭霖的相关分享](http://blog.csdn.net/lmj623565791/article/details/38960443)

[鸿洋的相关分享](http://blog.csdn.net/lmj623565791/article/details/38960443)

[zxy的相关分享](http://blog.csdn.net/android_zyf/article/details/60466323)

[工匠若水的相关分享](http://blog.csdn.net/yanbober/article/details/45887547)

[Carson_Ho的相关分享](http://blog.csdn.net/carson_ho/article/details/54136311)