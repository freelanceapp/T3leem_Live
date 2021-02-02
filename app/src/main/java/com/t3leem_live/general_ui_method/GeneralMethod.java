package com.t3leem_live.general_ui_method;

import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.t3leem_live.R;
import com.t3leem_live.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class GeneralMethod {

    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }








    @BindingAdapter("user_image")
    public static void user_image(View view, String endPoint) {
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).placeholder(R.drawable.ic_avatar).into(imageView);
            } else {
                Picasso.get().load(R.drawable.ic_avatar).into(imageView);

            }
        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).placeholder(R.drawable.ic_avatar).fit().into(imageView);
            } else {
                Picasso.get().load(R.drawable.ic_avatar).into(imageView);

            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).placeholder(R.drawable.ic_avatar).fit().into(imageView);
            } else {
                Picasso.get().load(R.drawable.ic_avatar).into(imageView);

            }
        }

    }

    @BindingAdapter("image")
    public static void image(View view, String endPoint) {
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).into(imageView);
            }
        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).fit().into(imageView);
            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).fit().into(imageView);
            }
        }

    }



    @BindingAdapter("video_frame")
    public static void chat_video_frame(View view, String endPoint) {
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (endPoint!=null){
                RequestOptions requestOptions = new RequestOptions().frame(5000000);
                Glide.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL+endPoint))
                        .apply(requestOptions)
                        .into(imageView);
            }


        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;
            if (endPoint!=null){
                RequestOptions requestOptions = new RequestOptions().frame(5000000);
                Glide.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL+endPoint))
                        .apply(requestOptions)
                        .into(imageView);
            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            if (endPoint!=null){
                RequestOptions requestOptions = new RequestOptions().frame(5000000);
                Glide.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL+endPoint))
                        .apply(requestOptions)
                        .into(imageView);
            }

        }

    }

    @BindingAdapter("date")
    public static void date(View view,String date){
        String[] dates = date.split("T");
        String d = dates[0];
        if (view instanceof TextView){
            TextView textView = (TextView) view;
            textView.setText(d);
        }
    }

    @BindingAdapter("rate")
    public static void rate(SimpleRatingBar ratingBar,double rate) {
        ratingBar.setRating((float) rate);

    }

    @BindingAdapter("rate2")
    public static void rate2(TextView textView,double rate2) {
        textView.setText(String.format(Locale.ENGLISH,"(%.2f)",rate2));
    }
}










