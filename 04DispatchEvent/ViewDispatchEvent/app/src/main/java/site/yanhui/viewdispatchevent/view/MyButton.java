package site.yanhui.viewdispatchevent.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Archer on 2017/10/2.
 * <p>
 * 功能描述： 自定义一个button，用来理解view的事件分发机制
 *
 */

public class MyButton extends android.support.v7.widget.AppCompatButton {

    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
