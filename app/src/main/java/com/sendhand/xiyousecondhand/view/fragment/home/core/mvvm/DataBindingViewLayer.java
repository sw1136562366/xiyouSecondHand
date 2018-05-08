package com.sendhand.xiyousecondhand.view.fragment.home.core.mvvm;

import android.databinding.ViewDataBinding;

import com.sendhand.xiyousecondhand.view.fragment.home.core.mvvm.binding.IBinding;


public abstract class DataBindingViewLayer<DB extends ViewDataBinding, VM extends IBinding, Container> extends ViewLayer<VM, Container> {

    protected DB mBinding;

    public DataBindingViewLayer(DB binding, Container container) {
        super(container);
        mBinding = binding;
    }

    @Override
    protected void onDetach() {
        mBinding.unbind();
    }
}
