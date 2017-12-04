package lac.com.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Aicun on 11/22/2017.
 */

public class HorizontalScrollViewPager extends ViewPager {

    private float x;
    private float y;

    public HorizontalScrollViewPager(Context context) {
        super(context);
    }

    public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);//only current view receives the event
                x = ev.getX();
                y = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float positionX = ev.getX();
                float positionY = ev.getY();
                float distanceX = positionX - x;
                float distanceY = positionY - y;
                if(Math.abs(distanceX) > Math.abs(distanceY)) { //move in horizontal direction
                    if(getCurrentItem() == 0 && distanceX > 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else if((getCurrentItem() == getAdapter().getCount() - 1) && distanceX < 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
