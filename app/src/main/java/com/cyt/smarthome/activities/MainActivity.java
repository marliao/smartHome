package com.cyt.smarthome.activities;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cyt.smarthome.R;
import com.cyt.smarthome.bean.User;
import com.cyt.smarthome.frags.Fragment_main;
import com.cyt.smarthome.utils.ImageUtils;
import com.cyt.smarthome.utils.L;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private FrameLayout mFlContent;
    private ListView mNavigationView;
    private TextView tv_u_name;
    private String[] titles;
    private String[] icons;
    private ImageView ivUIcon;
    private User currentUser;


    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }


    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected void initLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mFlContent = (FrameLayout) findViewById(R.id.fl_content);
        mNavigationView = (ListView) findViewById(R.id.navigationView);
        ivUIcon = (ImageView) findViewById(R.id.iv_u_icon);
    }

    @Override
    protected void init() {
        initFragment();
        initNavigationView();
    }

    private void initFragment() {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_content, new Fragment_main()).commit();

    }


    private void initNavigationView() {
        //登录

        tv_u_name = (TextView) findViewById(R.id.tv_u_name);
        titles = new String[]{getString(R.string.item1), getString(R.string.item2), getString(R.string.item3), getString(R.string.item4), getString(R.string.resetpassword), getString(R.string.item5)};
        icons = new String[]{getString(R.string.icon1), getString(R.string.icon2), getString(R.string.icon3), getString(R.string.icon4), getString(R.string.unlock), getString(R.string.icon5)};
        mNavigationView.setAdapter(new MyBaseAdapter());
//        menuItem1 = (MenuItem) mNavigationView.getMenu().findItem(R.id.item_1);
//        menuItem2 = (MenuItem) mNavigationView.getMenu().findItem(R.id.item_2);
//        menuItem3 = (MenuItem) mNavigationView.getMenu().findItem(R.id.item_3);
//        menuItem4 = (MenuItem) mNavigationView.getMenu().findItem(R.id.item_4);
        //通过actionbardrawertoggle将toolbar与drawablelayout关联起
        if (currentUser == null) {
            currentUser = BmobUser.getCurrentUser(User.class);
        }
//        L.e("currentUser",currentUser.toString());
        if (currentUser.getuName() != null) {
            tv_u_name.setText(currentUser.getuName());
        } else {
            currentUser.setuName("未命名");
            currentUser.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        L.e("修改成功");
                    } else {
                        L.e(e.toString());
                    }
                }
            });
            tv_u_name.setText(currentUser.getuName());
        }
        if (currentUser.getAvatar() != null) {
            L.e(currentUser.getAvatar().getUrl());
            ImageUtils.setCircleBitmap(currentUser.getAvatar().getUrl(), ivUIcon);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, R.string.home_drawer_open, R.string.home_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //可以重新侧滑方法,该方法实现侧滑动画,整个布局移动效果
                //获取mDrawerLayout中的第一个子布局，也就是布局中的RelativeLayout
                //获取抽屉的view
                View mContent = mDrawerLayout.getChildAt(0);
                float scale = 1 - slideOffset;
                float endScale = 0.8f + scale * 0.2f;
                float startScale = 1 - 0.3f * scale;

                //设置左边菜单滑动后的占据屏幕大小
                drawerView.setScaleX(startScale);
                drawerView.setScaleY(startScale);
                //设置菜单透明度
                drawerView.setAlpha(0.6f + 0.4f * (1 - scale));

                //设置内容界面水平和垂直方向偏转量
                //在滑动时内容界面的宽度为 屏幕宽度减去菜单界面所占宽度
                mContent.setTranslationX(drawerView.getMeasuredWidth() * (1 - scale));
                //设置内容界面操作无效（比如有button就会点击无效）
                mContent.invalidate();
                //设置右边菜单滑动后的占据屏幕大小
                mContent.setScaleX(endScale);
                mContent.setScaleY(endScale);
            }
        };

        toggle.syncState();
        mDrawerLayout.addDrawerListener(toggle);

        //设置图片为本身的颜色
        //  mNavigationView.setItemIconTintList(null);
        mNavigationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getApplicationContext(), PersonalCenterActivity.class));
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(), SetingActivity.class));
                        finish();
                        break;
                    case 4:
                        startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class));
                        finish();
                        break;
                    case 5:
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                        break;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });

//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
////                L.i("item",item.getTitle().toString());
//                if (item == menuItem1) {
//                    mDrawerLayout.closeDrawer(GravityCompat.START);
//                } else if (item == menuItem2) {
//                    mDrawerLayout.closeDrawer(GravityCompat.START);
//                } else if (item == menuItem3) {
//                    mDrawerLayout.closeDrawer(GravityCompat.START);
//                } else if (item == menuItem4) {
//                    mDrawerLayout.closeDrawer(GravityCompat.START);
//                    startActivity(new Intent(getApplicationContext(), SetingActivity.class));
//                }
//                return true;
//            }
//        });

    }

    class MyBaseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return titles.length;
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
            View inflate = View.inflate(MainActivity.this, R.layout.layout_menu_item, null);
            ViewHolder viewHolder = new ViewHolder(inflate);
            viewHolder.tv_menu_title.setText(titles[position]);
            viewHolder.iv_menu_icon.setBackgroundResource(getResources().getIdentifier(icons[position], "drawable", getPackageName()));
            return inflate;
        }

        public class ViewHolder {
            public View rootView;
            public TextView tv_menu_title;
            public ImageView iv_menu_icon;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.tv_menu_title = (TextView) rootView.findViewById(R.id.tv_menu_title);
                this.iv_menu_icon = (ImageView) rootView.findViewById(R.id.iv_menu_icon);
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser = BmobUser.getCurrentUser(User.class);
        if (currentUser.getAvatar() != null) {
            L.e(currentUser.getAvatar().getUrl());
            ImageUtils.setCircleBitmap(currentUser.getAvatar().getUrl(), ivUIcon);
        }
    }
}
