package lac.com.news.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import lac.com.news.R;
import lac.com.news.activity.MainActivity;
import lac.com.news.beans.News;
import lac.com.news.contents.NewsCenterPage;
import lac.com.news.utils.DensityUtil;

/**
 * Created by Aicun on 11/1/2017.
 */

public class LeftMenuFragment extends BaseFragment {

    private List<News.DataBean> data;
    private LeftMenuFragmentAdapter adapter;
    private ListView lv;
    private int selectPosition;

    @Override
    public View initView() {
        lv = new ListView(context);
        lv.setPadding(0, DensityUtil.dip2px(context,40),0,0);
        lv.setDividerHeight(0);

        lv.setCacheColorHint(Color.TRANSPARENT);

        lv.setSelector(android.R.color.transparent);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectPosition = position;
                adapter.notifyDataSetChanged();

                MainActivity activity = (MainActivity) context;
                activity.getSlidingMenu().toggle();

                switchPage(selectPosition);
            }
        });
        return lv;
    }

    @Override
    public void initData() {
        super.initData();
    }

    public void setData(List<News.DataBean> data) {
        this.data = data;
        adapter = new LeftMenuFragmentAdapter();
        lv.setAdapter(adapter);
        switchPage(selectPosition);
    }

    private class LeftMenuFragmentAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = (TextView) View.inflate(context,R.layout.item_left_menu,null);
            tv.setText(data.get(position).getTitle());
            tv.setEnabled(selectPosition == position);
            return tv;
        }
    }

    private void switchPage(int position) {
        MainActivity activity = (MainActivity) context;
        MainContentFragment mainContentFragment = activity.getMainContentFragment();
        NewsCenterPage newsCenterPage = mainContentFragment.getNewsCenterPage();
        newsCenterPage.switchPager(position);
    }
}
