package com.sendhand.xiyousecondhand.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.MyApplication;

import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;


public class MessageFragment extends ConversationListFragment {

    public MessageFragment() {
        super();
    }

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        startConversationList();
    }


   private Fragment initConversationList() {
       ConversationListFragment fragment = new ConversationListFragment();
       Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
               .appendPath("conversationlist")
               .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话，该会话聚合显示
               .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话，该会话非聚合显示
               .build();
       fragment.setUri(uri);  //设置 ConverssationListFragment 的显示属性
        return fragment;
   }

    private void startConversationList() {
        Map<String, Boolean> map = new HashMap<>();
        map.put(Conversation.ConversationType.PRIVATE.getName(), false); // 会话列表需要显示私聊会话, 第二个参数 true 代表私聊会话需要聚合显示
        map.put(Conversation.ConversationType.GROUP.getName(), false);  // 会话列表需要显示群组会话, 第二个参数 false 代表群组会话不需要聚合显示

        RongIM.getInstance().startConversationList(MyApplication.getContext(), map);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message,null);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}