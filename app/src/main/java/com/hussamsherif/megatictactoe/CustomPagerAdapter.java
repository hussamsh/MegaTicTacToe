package com.hussamsherif.megatictactoe;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.hussamsherif.megatictactoe.Activities.MainActivity;

public class CustomPagerAdapter extends PagerAdapter {
    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view) == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (position == 0)
            imageView.setImageDrawable(ContextCompat.getDrawable(container.getContext() , R.drawable.first_page));
        else if (position == 1)
            imageView.setImageDrawable(ContextCompat.getDrawable(container.getContext() , R.drawable.second_page));
        else if (position == 2){
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.welcome_last_view , null);
            Button button = (Button) view.findViewById(R.id.start_playing_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container.getContext().startActivity(new Intent(container.getContext() , MainActivity.class));
                }
            });
            container.addView(view);
            return view;
        }
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
