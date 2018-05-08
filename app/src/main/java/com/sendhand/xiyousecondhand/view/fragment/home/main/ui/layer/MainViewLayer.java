package com.sendhand.xiyousecondhand.view.fragment.home.main.ui.layer;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.MyApplication;
import com.sendhand.xiyousecondhand.databinding.ActivityMainBinding;
import com.sendhand.xiyousecondhand.databinding.LayoutNavHeadBinding;
import com.sendhand.xiyousecondhand.view.fragment.PersonFragment;
import com.sendhand.xiyousecondhand.view.fragment.home.core.mvvm.ViewLayer;
import com.sendhand.xiyousecondhand.view.fragment.home.main.model.IMainViewModel;
import com.sendhand.xiyousecondhand.view.fragment.home.main.ui.HomeFragment;
import com.sendhand.xiyousecondhand.view.fragment.home.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

public class MainViewLayer extends ViewLayer<IMainViewModel, AppCompatActivity> {

    public static final int TAB_INDEX_HOME = 0;
    public static final int TAB_INDEX_FIND = 1;
    public static final int TAB_INDEX_MESSAGE = 2;
    public static final int TAB_INDEX_MINE = 3;

    private IMainViewModel mViewModel;

    private ActivityMainBinding mBinding;
    private LayoutNavHeadBinding mNavHeadBinding;

    private Drawer mDrawer;

    private List<Fragment> mFragmentList;
    private HomeFragment mHomeFragment;
    private Fragment mFindFragment;
    private Fragment mMessageFragment;
    private Fragment mMineFragment;

    private List<View> mTabViewList;

    public MainViewLayer(AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
    }

    @Override
    protected void onAttach(IMainViewModel viewModel) {
        mViewModel = viewModel;

        initView();
        initDataBinding();
    }

    @Override
    protected void onDetach() {
        mNavHeadBinding.unbind();
        mBinding.unbind();
    }

    private void initView() {
        mBinding = DataBindingUtil.setContentView(mContainer, R.layout.activity_main);
        mNavHeadBinding = DataBindingUtil.inflate(mContainer.getLayoutInflater(),
                R.layout.layout_nav_head, null, false);
        int width = DisplayUtil.getScreenWidth(mContainer)
                - DisplayUtil.dip2px(mContainer, 64);
        int height = width * 5 / 9;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        mNavHeadBinding.layoutNavHead.setLayoutParams(params);

        mDrawer = new DrawerBuilder()
                .withActivity(mContainer)
                .withHasStableIds(true)
                .withSelectedItem(-1)
                .withStickyHeader(mNavHeadBinding.getRoot())
                .build();

        mDrawer.setItems(mViewModel.getDrawerItems());

        mDrawer.getRecyclerView().setVerticalScrollBarEnabled(false);
        mDrawer.getDrawerLayout().addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View content = mDrawer.getDrawerLayout().getChildAt(0);
                View menu = drawerView;

                int menuWidth = menu.getMeasuredWidth();
                content.setTranslationX((int) (menuWidth * slideOffset));
                content.setAlpha(0.5f + 0.5f * (1 - slideOffset));
                content.invalidate();
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        mFragmentList = new ArrayList<>();
        mHomeFragment = HomeFragment.newInstance();
        mFindFragment = new Fragment();
        //融云会话列表
        mMessageFragment = initConversationList();
//        mMessageFragment = MessageFragment.newInstance();
        mMineFragment = PersonFragment.newInstance();
        mFragmentList.add(mHomeFragment);
        mFragmentList.add(mFindFragment);
        mFragmentList.add(mMessageFragment);
        mFragmentList.add(mMineFragment);

        mTabViewList = new ArrayList<>();
        mTabViewList.add(mBinding.layoutBottom.home);
        mTabViewList.add(mBinding.layoutBottom.find);
        mTabViewList.add(mBinding.layoutBottom.message);
        mTabViewList.add(mBinding.layoutBottom.mine);
    }

    /**
     * 得到融云会话列表实例
     * @return
     */
    private Fragment initConversationList() {
        ConversationListFragment fragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + MyApplication.getContext().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话，该会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话，该会话非聚合显示
                .build();
        fragment.setUri(uri);  //设置 ConverssationListFragment 的显示属性
        return fragment;
    }

    private void initDataBinding() {
        mViewModel.addDrawerItemsChangedCallback(new ObservableList.OnListChangedCallback() {
            @Override
            public void onChanged(ObservableList observableList) {
                mDrawer.getAdapter().notifyAdapterDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList observableList, int positionStart, int itemCount) {
                mDrawer.getAdapter().notifyAdapterItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(ObservableList observableList, int positionStart, int itemCount) {
                mDrawer.getAdapter().notifyAdapterItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(ObservableList observableList, int fromPosition, int toPosition, int itemCount) {
                mDrawer.getAdapter().notifyAdapterItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onItemRangeRemoved(ObservableList observableList, int positionStart, int itemCount) {
                mDrawer.getAdapter().notifyAdapterItemRangeRemoved(positionStart, itemCount);
            }
        });

        mViewModel.addCurrentIndexChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int propertyId) {
                ObservableInt index = (ObservableInt) observable;
                if (mTabViewList.get(index.get()).isSelected()) {
                    return;
                }

                for (int i = 0; i < mTabViewList.size(); i++) {
                    View view = mTabViewList.get(i);
                    if (i == index.get()) {
                        view.setSelected(true);
                    } else if (view.isSelected()) {
                        view.setSelected(false);
                    }
                }

                FragmentTransaction transaction = mContainer.getSupportFragmentManager().beginTransaction();
                Fragment fragment = mFragmentList.get(index.get());
                if (!fragment.isAdded()) {
                    transaction.add(R.id.container, fragment);
                }
                transaction.show(fragment);
                for (int i = 0; i < mFragmentList.size(); i++) {
                    if (i == index.get()) {
                        continue;
                    }
                    fragment = mFragmentList.get(i);
                    if (fragment.isAdded()
                            && fragment.isVisible()) {
                        transaction.hide(fragment);
                    }
                }

                transaction.commit();
            }
        });
    }
}
