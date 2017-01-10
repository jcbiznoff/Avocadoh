package com.chung.jay.avocadoh.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.chung.jay.avocadoh.R;
import com.chung.jay.avocadoh.fragment.MyBooksFragment;
import com.chung.jay.avocadoh.fragment.MyFavoriteBooksFragment;
import com.chung.jay.avocadoh.fragment.RecentBooksFragment;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jaychung on 5/20/16.
 */
public class BooksActivity extends BaseActivity {

    @Bind(R.id.vpBookList)
    ViewPager mViewPager;

    @Bind(R.id.fab_new_book)
    FloatingActionButton mMainFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybooks);

        // Create the adapter that will return a fragment for each section
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[]{
                    RecentBooksFragment.newInstance(),
                    MyBooksFragment.newInstance(),
                    MyFavoriteBooksFragment.newInstance()
            };

            private final String[] mFragmentNames = new String[]{
                    "Books",
                    "My Books",
                    "Favorites"
            };

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        handleFabAction(true);
    }

    @OnClick(R.id.fab_new_book)
    public void addNewBook() {
        startActivity(new Intent(BooksActivity.this, AddNewBookActivity.class));
    }


    public static void scaleUp(Context context, View viewToScale) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_up_fab);
        animation.setAnimationListener(makeFabVisible(viewToScale));
        viewToScale.startAnimation(animation);
    }

    public static void scaleDown(Context context, View viewToScale) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_down_fab);
        animation.setAnimationListener(makeFabInvisible(viewToScale));
        viewToScale.startAnimation(animation);
    }

    private static Animation.AnimationListener makeFabVisible(final View viewToScale) {
        return new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewToScale.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        };
    }

    private static Animation.AnimationListener makeFabInvisible(final View viewToScale) {
        return new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewToScale.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        };
    }

    public void handleFabAction(boolean show) {
        if (show) {
            scaleUp(this, mMainFab);
        } else {
            scaleDown(this, mMainFab);
        }
    }
}
