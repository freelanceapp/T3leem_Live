package com.t3leem_live.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;
import com.t3leem_live.R;
import com.t3leem_live.models.SliderModel;
import com.t3leem_live.tags.Tags;

import java.util.List;

public class StudentSliderAdapter extends PagerAdapter {
    private List<SliderModel> list;
    private Context context;
    private LayoutInflater inflater;


    public StudentSliderAdapter(List<SliderModel> list, Context context) {
        this.list = list;


        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = inflater.inflate(R.layout.student_slider,container,false);
        ImageView imageView = view.findViewById(R.id.image);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvDescription = view.findViewById(R.id.tvContent);

        tvTitle.setText(list.get(position).getTitle());
        tvDescription.setText(list.get(position).getText());
        Log.e("dew",list.get(position).getText());
        Picasso.get().load(Uri.parse(Tags.IMAGE_URL+list.get(position).getImage())).fit().into(imageView);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
