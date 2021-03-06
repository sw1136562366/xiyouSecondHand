package com.sendhand.xiyousecondhand.view.fragment.home.core.mvvm.binding;

import android.databinding.ObservableList;

public class ListBinding implements IBinding {

    private ObservableList mList;
    private ObservableList.OnListChangedCallback mCallback;

    public ListBinding(ObservableList list, ObservableList.OnListChangedCallback callback) {
        mList = list;
        mCallback = callback;
    }

    @Override
    public void bind() {
        mList.addOnListChangedCallback(mCallback);
    }

    @Override
    public void unbind() {
        mList.removeOnListChangedCallback(mCallback);
        mCallback = null;
        mList = null;
    }
}
