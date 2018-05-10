package ykk.xc.com.wms;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainTabFragmentActivity extends FragmentActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lin_title)
    LinearLayout linTitle;
    @BindView(R.id.radio1)
    RadioButton radio1;
    @BindView(R.id.tab1)
    TextView tab1;
    @BindView(R.id.relative1)
    RelativeLayout relative1;
    @BindView(R.id.radio2)
    RadioButton radio2;
    @BindView(R.id.tab2)
    TextView tab2;
    @BindView(R.id.relative2)
    RelativeLayout relative2;
    @BindView(R.id.radio3)
    RadioButton radio3;
    @BindView(R.id.tab3)
    TextView tab3;
    @BindView(R.id.relative3)
    RelativeLayout relative3;
    @BindView(R.id.radio4)
    RadioButton radio4;
    @BindView(R.id.tab4)
    TextView tab4;
    @BindView(R.id.relative4)
    RelativeLayout relative4;
    @BindView(R.id.lin_tab)
    LinearLayout linTab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private MainTabFragmentActivity context = this;
    private TextView curTv;
    private RadioButton curRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aa_main);
        ButterKnife.bind(this);

        initDatas();
    }

    private void initDatas() {
        curTv = tab1;
        curRadio = radio1;

        List<Fragment> listFragment = new ArrayList<Fragment>();
        listFragment.add(new MainTabFragment1());
        listFragment.add(new MainTabFragment2());
        listFragment.add(new MainTabFragment3());
        listFragment.add(new MainTabFragment4());
        //ViewPager设置适配器
        viewPager.setAdapter(new MainTabFragmentAdapter(getSupportFragmentManager(), listFragment));
        //ViewPager显示第一个Fragment
        viewPager.setCurrentItem(0);
        //ViewPager页面切换监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        relative1.performClick();
                        break;
                    case 1:
                        relative2.performClick();
                        break;
                    case 2:
                        relative3.performClick();
                        break;
                    case 3:
                        relative4.performClick();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 选中之后改变样式
     */
    private void tabSelected(TextView tv, RadioButton rb) {
        curRadio.setChecked(false);
        curTv.setTextColor(Color.parseColor("#1a1a1a"));
        rb.setChecked(true);
        tv.setTextColor(Color.parseColor("#303f9f"));
        curRadio = rb;
        curTv = tv;
    }


    @OnClick({R.id.relative1, R.id.relative2, R.id.relative3, R.id.relative4,
              R.id.radio1, R.id.radio2, R.id.radio3, R.id.radio4})
    public void onViewClicked(View view) {
        // setCurrentItem第二个参数控制页面切换动画
        //  true:打开/false:关闭
        //  viewPager.setCurrentItem(0, false);

        switch (view.getId()) {
            case R.id.relative1:
                tabSelected(tab1, radio1);
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.relative2:
                tabSelected(tab2, radio2);
                viewPager.setCurrentItem(1, false);
                break;
            case R.id.relative3:
                tabSelected(tab3, radio3);
                viewPager.setCurrentItem(2, false);
                break;
            case R.id.relative4:
                tabSelected(tab4, radio4);
                viewPager.setCurrentItem(3, false);
                break;
            case R.id.radio1: // RadioButton
                tabSelected(tab1, radio1);
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.radio2:
                tabSelected(tab2, radio2);
                viewPager.setCurrentItem(1, false);
                break;
            case R.id.radio3:
                tabSelected(tab3, radio3);
                viewPager.setCurrentItem(2, false);
                break;
            case R.id.radio4:
                tabSelected(tab4, radio4);
                viewPager.setCurrentItem(3, false);
                break;
        }
    }

}
