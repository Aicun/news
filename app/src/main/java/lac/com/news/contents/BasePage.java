package lac.com.news.contents;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import lac.com.news.R;
import lac.com.news.activity.MainActivity;

/**
 * Created by Aicun on 11/4/2017.
 */

public class BasePage {

    public final Context context;
    public View rootView;
    public TextView titleView;
    public ImageButton ibMenu;
    public FrameLayout frameLayout;


    public BasePage(Context context) {
        this.context = context;
        rootView = initView();
    }

    private View initView() {
        View view = View.inflate(context, R.layout.base_pager,null);
        //x.view().inject(context,view);

        titleView = (TextView) view.findViewById(R.id.page_title);
        ibMenu = (ImageButton) view.findViewById(R.id.ib_menu);
        frameLayout = (FrameLayout) view.findViewById(R.id.fl_content);

        return view;
    }

    public void initData(){

    }
}
