package zerone.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

/**
 * Created by Administrator on 2018-11-19.
 */

public class MyGridView extends GridView {

    public MyGridView(Context context) {

        super(context);

    }

    public MyGridView(Context context, AttributeSet attrs) {

        super(context, attrs);

    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

    }

    /**
     * 设置上下不滚动
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_MOVE){
            return true;//true:禁止滚动
        }
        return super.dispatchTouchEvent(ev);
    }
}
