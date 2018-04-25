package Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import library.GetInformationByPHP;

public class Fragment_ptype extends Fragment {
    View v;
    DisplayMetrics dm;
    TabLayout tabLayout;
    ViewPager viewPager;
    JSONObject json1, json2, json3, json4;
    Fragment_shop_content fragment_shop_content1, fragment_shop_content2, fragment_shop_content3, fragment_shop_content4;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ptype_layout, container, false);
        dm = getActivity().getApplicationContext().getResources().getDisplayMetrics();
        //setupTabIcons();
        initRecyclerView();
        tabLayout = v.findViewById(R.id.ptype_header_tablayout);
        tabLayout.setSelectedTabIndicatorHeight(6);
        viewPager = v.findViewById(R.id.ptype_viewpager);
        fragment_shop_content1 = new Fragment_shop_content();
        new Thread(new Runnable() {
            @Override
            public void run() {

                // fragment_shop_content4.setJson( new GetInformationByPHP().getBanner(3),new GetInformationByPHP().getIplist(3,1));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // viewPager.setAdapter(new ShopViewPagerAdapter(getFragmentManager(), getActivity().getApplicationContext().getResources().getStringArray(R.array.shop_header_title), new Fragment[]{fragment_shop_content1, fragment_shop_content2, fragment_shop_content3, fragment_shop_content4}));
                        tabLayout.setTabMode(TabLayout.MODE_FIXED);
                        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                        tabLayout.setupWithViewPager(viewPager, true);

                    }
                });
            }
        }).start();

        return v;
    }

    private void initRecyclerView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final JSONObject json = new GetInformationByPHP().getPtype();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView = v.findViewById(R.id.ptype_title);
                        recyclerView.setHasFixedSize(true);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (dm.widthPixels / 4 + 20 * dm.density)));
                        /*
                        PtypeRecyclerAdapter adapter=new PtypeRecyclerAdapter(getActivity().getApplicationContext(), ResolveJsonData.getPtypeDetail(json,0),(dm.widthPixels/4),(int)(dm.widthPixels/4+20*dm.density));
                        recyclerView.setAdapter(adapter);
                        adapter.setClickListener(new PtypeRecyclerAdapter.ClickListener() {
                            @Override
                            public void ItemClicked(View view, int postion, ArrayList<Map<String, String>> list) {
                                Toast.makeText(getContext(), ""+list.get(postion).get("title"), Toast.LENGTH_SHORT).show();
                            }
                        });
*/
                    }
                });
            }
        }).start();

    }

    /*
        private void setupTabIcons() {
            main_title = v.findViewById(R.id.ptype_tablayout);
            main_title.setTabMode(TabLayout.MODE_SCROLLABLE);
            main_title.setSelectedTabIndicatorHeight(0);
            //===========塞tab
            TextView textView;
            textView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.ptype_title, null);
            textView.setText("" + 0);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_member_w, 0, 0);
            main_title.addTab(main_title.newTab().setCustomView(textView));
            for (int i = 1; i < 5; i++) {
                textView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.ptype_title, null);
                textView.setText("" + i);
                textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_information_w, 0, 0);

                main_title.addTab(main_title.newTab().setCustomView(textView));
            }
            //===========塞tab
            main_title.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    TextView textView = (TextView) main_title.getTabAt(tab.getPosition()).getCustomView();
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_member_w, 0, 0);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    TextView textView = (TextView) main_title.getTabAt(tab.getPosition()).getCustomView();
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_information_w, 0, 0);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    */
    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume", "onResume");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.e("onAttach", "onAttach");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("onStart", "onStart");
    }

    public void setFilter() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                json1 = new GetInformationByPHP().getIplist(0, 1);
                json2 = new GetInformationByPHP().getIplist(1, 1);
                json3 = new GetInformationByPHP().getIplist(2, 1);
                json4 = new GetInformationByPHP().getIplist(3, 1);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fragment_shop_content1.setFilter(json1);
                        fragment_shop_content2.setFilter(json2);
                        fragment_shop_content3.setFilter(json3);
                        fragment_shop_content4.setFilter(json4);
                    }
                });

            }
        }).start();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            return;
        } else {  // 在最前端显示 相当于调用了onResume();

            setFilter();
            //网络数据刷新
        }
    }
}
