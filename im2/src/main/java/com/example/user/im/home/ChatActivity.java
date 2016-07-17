package com.example.user.im.home;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.example.user.http.callback.JSONArrayCallBack;
import com.example.user.http.exception.AppException;
import com.example.user.http.net.NetWorkRunnable;
import com.example.user.http.net.Request;
import com.example.user.http.util.ThreadPool;
import com.example.user.im.IMApplication;
import com.example.user.im.R;
import com.example.user.im.baidu.Utils;
import com.example.user.im.db.ConversationDao;
import com.example.user.im.db.MessageDao;
import com.example.user.im.entity.Conversation;
import com.example.user.im.entity.Message;
import com.example.user.im.push.IMPushManager;
import com.example.user.im.push.IMPushWatcher;
import com.example.user.im.util.Contans;
import com.example.user.im.util.TimeHelper;
import com.example.user.im.util.UrlHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends BaseActivity {
    private Button mChatLoadMore;
    private ListView mChatLsv;
    private ChatAdapter chatAdapter;
    private List<Message> conversationList;
    private ThreadPool threadPool = new ThreadPool();
    private NetWorkRunnable netWorkRunnable;
    private MessageDao messageDao;
    private DisplayImageOptions options;
    private Handler handler = new Handler();


    private String targetId;
    private String selfId;

    private IMPushWatcher pushWatcher = new IMPushWatcher() {
        @Override
        public void updateMessage(Message message) {
            Log.i("msg", "PushWatcher接收的数据=" + message.toString());
//            //remove旧数据,添加新数据
//            Conversation conversation = message.copyTo(ChatActivity.this);
//            conversationList.remove(conversation);
//            conversationList.add(conversation);
//            conversationAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IMPushManager.getInstance(this).addWatcher(pushWatcher);
    }

    @Override
    protected void onPause() {
        super.onPause();
        IMPushManager.getInstance(this).removeWatcher(pushWatcher);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_chat);
    }

    @Override
    public void initView() {
        mChatLoadMore = (Button) findViewById(R.id.chatLoadMore);
        mChatLsv = (ListView) findViewById(R.id.chatLsv);

        mChatLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 加载更过
                loadDataFromServer(Contans.LOADMORE);
            }
        });
    }

    @Override
    public void initData() {
        String api_key = Utils.getMetaValue(ChatActivity.this, "api_key");
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, api_key);
        targetId = getIntent().getStringExtra(Contans.KEY_TATGETID);
        selfId = IMApplication.self_id;
        chatAdapter = new ChatAdapter();
        mChatLsv.setAdapter(chatAdapter);
        initImageLoaderOption();
        loadDataFromDB();
        loadDataFromServer(Contans.REFRESH);
    }

    private void loadDataFromDB() {
        try {
            messageDao = new MessageDao(ChatActivity.this);
            conversationList = messageDao.queryBySendAndReceiveId(targetId, selfId, 0);
            chatAdapter.notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步加载图片初始化构建
     */
    private void initImageLoaderOption() {
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    /**
     * 从服务器上加载数据
     *
     * @return
     */
    private List<Message> loadDataFromServer(final int state) {
        long timeStamp = 0;

        if (state == Contans.LOADMORE && conversationList != null && conversationList.size() > 0) {
            timeStamp = conversationList.get(0).getTimestamp();
        }
        String url = UrlHelper.loadAllMsg(targetId, state, timeStamp);
        Request request = new Request(url);
        request.setiCallBack(new JSONArrayCallBack<Message>() {

            @Override
            public ArrayList<Message> preRequest() {
                //只在loadMore时起作用
                if (state == Contans.LOADMORE) {
                    try {
                        messageDao = new MessageDao(ChatActivity.this);
                        return (ArrayList<Message>) messageDao.queryBySendAndReceiveId(targetId, selfId, conversationList.get(0).getTimestamp());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            public ArrayList<Message> postRequest(ArrayList<Message> messages) {
                //更新数据库操作
                if (messageDao == null) {
                    messageDao = new MessageDao(ChatActivity.this);
                    messageDao.addMessage(messages);
                }
                return messages;
            }

            @Override
            public void onSuccess(ArrayList<Message> messages) {
                if (messages != null && messages.size() > 0) {
                    if (conversationList == null) {
                        conversationList = new ArrayList<Message>();
                    }

                    if (state == Contans.LOADMORE && conversationList != null && conversationList.size() > 0) {
                        conversationList.addAll(0, messages);
                    } else {
                        //防止显示重复数据
                        conversationList.clear();
                        conversationList.addAll(messages);
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            chatAdapter.notifyDataSetChanged();

                        }
                    });
                } else {
                    //TODO 没有更多的数据
                }

                //但是似乎太频繁了
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadDataFromDB(); //保持界面与数据库一致
                    }
                });


            }

            @Override
            public void onFail(AppException e) {

            }
        });

        Map<String, String> heads = new HashMap<String, String>();
        heads.put("Accept", "application/json");
        heads.put("content-type", "application/json"); //这行必须得加
        heads.put("Authorization", IMApplication.getUserToken());
        request.setHeadParams(heads);
        request.setMethod(Request.Method.GET);
        netWorkRunnable = new NetWorkRunnable();
        netWorkRunnable.setRequest(request);
        threadPool.execute(netWorkRunnable);
        return null;
    }

    private class ChatAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return conversationList == null ? 0 : conversationList.size();
        }

        @Override
        public Object getItem(int i) {
            return conversationList == null ? null : conversationList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            Message message = conversationList.get(i);
            if (IMApplication.self_id.equalsIgnoreCase(message.getSenderId())) { //out
                convertView = createTextOutMsg(i, message);
            } else { //int
                convertView = createTextInMsg(i, message);
            }
            return convertView;
        }


        private View createTextInMsg(int position, Message message) {
            View convertView = LayoutInflater.from(ChatActivity.this).inflate(R.layout.activity_chat_text_in_item, null);
            ImageView mChatInAvatarImg = (ImageView) convertView.findViewById(R.id.mChatInAvatarImg);
            TextView mChatInMsgLabel = (TextView) convertView.findViewById(R.id.mChatInMsgLabel);
            TextView mChatTimeLabel = (TextView) convertView.findViewById(R.id.mChatTimeLabel);
            mChatTimeLabel.setText(TimeHelper.getTimeRule3(message.getTimestamp()));
            mChatInMsgLabel.setText(message.getContent());
            return convertView;
        }

        private View createTextOutMsg(int position, Message message) {
            View convertView = LayoutInflater.from(ChatActivity.this).inflate(R.layout.activity_chat_text_out_item, null);
            ImageView mChatOutAvatarImg = (ImageView) convertView.findViewById(R.id.mChatOutAvatarImg);
            TextView mChatOutMsgLabel = (TextView) convertView.findViewById(R.id.mChatOutMsgLabel);
            TextView mChatTimeLabel = (TextView) convertView.findViewById(R.id.mChatTimeLabel);
            mChatTimeLabel.setText(TimeHelper.getTimeRule3(message.getTimestamp()));
            mChatOutMsgLabel.setText(message.getContent());
            return convertView;
        }

    }
}
