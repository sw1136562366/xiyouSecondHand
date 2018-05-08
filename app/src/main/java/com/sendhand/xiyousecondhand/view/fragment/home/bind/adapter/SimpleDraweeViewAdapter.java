package com.sendhand.xiyousecondhand.view.fragment.home.bind.adapter;

import android.databinding.BindingAdapter;

import com.facebook.drawee.view.SimpleDraweeView;


public class SimpleDraweeViewAdapter {

    @BindingAdapter(value = {"actualImageUri"})
    public static void setImageUrl(SimpleDraweeView draweeView, String url) {
        draweeView.setImageURI(url);
    }
}
