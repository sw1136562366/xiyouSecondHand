package com.sendhand.xiyousecondhand.view.fragment.home.core.mvvm;


import com.sendhand.xiyousecondhand.view.fragment.home.core.mvvm.binding.IBinding;

public abstract class ViewLayer<VM extends IBinding, Container> {

    protected Container mContainer;

    public ViewLayer(Container container) {
        mContainer = container;
    }

    protected abstract void onAttach(VM viewModel);

    protected abstract void onDetach();

}
