package com.sendhand.xiyousecondhand.view.fragment.home.main.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.databinding.FragmentHomeBinding;
import com.sendhand.xiyousecondhand.view.fragment.home.main.model.IHomeViewModel;
import com.sendhand.xiyousecondhand.view.fragment.home.main.model.impl.HomeViewModel;
import com.sendhand.xiyousecondhand.view.fragment.home.main.ui.layer.HomeViewLayer;


public class HomeFragment extends Fragment {

    private IHomeViewModel mViewModel;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentHomeBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        mViewModel = new HomeViewModel(new HomeViewLayer(binding, this));
        mViewModel.bind();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.startRefresh(true);
    }

    @Override
    public void onDestroyView() {
        mViewModel.unbind();
        super.onDestroyView();
    }
}
