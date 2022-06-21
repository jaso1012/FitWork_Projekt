package com.example.fitworkmockup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

// sharedPreferences für erstes Open,

public class FirstOpenActivity extends AppCompatActivity {

    ViewPager mSlideViewPager;
    LinearLayout mDotLayout;
    Button zurueck_button, vor_button, ueberspringen_button;

    TextView[] dots;
    ViewPageAdapter viewPagerAdapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_open);

        zurueck_button = (Button) findViewById(R.id.ZURÜCK);
        vor_button = findViewById(R.id.VOR);
        ueberspringen_button = (Button) findViewById(R.id.ÜBERSPRINGEN);

        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.indicator_layout);
        viewPagerAdapter = new ViewPageAdapter(this);

        mSlideViewPager.setAdapter(viewPagerAdapter);
        setUpindicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);


        zurueck_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getitem(0) > 0)
                {
                    mSlideViewPager.setCurrentItem(getitem(-1),true);
                }
            }
        });

        vor_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getitem(0) < 4)
                    mSlideViewPager.setCurrentItem(getitem(1),true);
                else {

                    Intent i = new Intent(FirstOpenActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();

                }
            }
        });

        ueberspringen_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstOpenActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setUpindicator(int position){

        dots = new TextView[5];
        mDotLayout.removeAllViews();

        for (int i = 0 ; i < dots.length ; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.inactive,getApplicationContext().getTheme()));
            mDotLayout.addView(dots[i]);

        }

        dots[position].setTextColor(getResources().getColor(R.color.active,getApplicationContext().getTheme()));

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onPageSelected(int position) {

            setUpindicator(position);

            if (position > 0){

                zurueck_button.setVisibility(View.VISIBLE);

            }else {

                zurueck_button.setVisibility(View.INVISIBLE);

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getitem(int i){

        return mSlideViewPager.getCurrentItem() + i;
    }



}