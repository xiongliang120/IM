package com.example.user.im.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import com.example.user.im.entity.Conversation;
import com.example.user.im.entity.Message;
import com.example.user.im.push.IMPushManager;
import com.example.user.im.push.IMPushWatcher;
import com.example.user.im.util.Contans;
import com.example.user.im.util.UrlHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversationActivity extends BaseActivity {
    private ListView mConversationLsv;
    private ConversationAdapter conversationAdapter;
    private List<Conversation> conversationList;
    private ThreadPool threadPool = new ThreadPool();
    private NetWorkRunnable netWorkRunnable;
    private DisplayImageOptions options;
    private ConversationDao conversationDao;
    private Handler handler = new Handler();

    //发送的消息  userId 52d5091c1b00004000cdccc4
    // {"_id":"1111111","content":"1","receiverId":"52d5091c1b00004000cdccc4","receiver_name":"小宋","senderId":"3333333333","sender_name":"宋华生","timestamp":"1244555556662"}

    private IMPushWatcher pushWatcher = new IMPushWatcher() {
        @Override
        public void updateMessage(Message message) {
            Log.i("msg", "PushWatcher接收的数据=" + message.toString());
            //remove旧数据,添加新数据
            Conversation conversation = message.copyTo(ConversationActivity.this);
            conversationList.remove(conversation);
            conversationList.add(conversation);
            conversationAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.activity_conversation);
    }

    @Override
    public void initView() {
        mConversationLsv = (ListView) findViewById(R.id.conversationLsv);
        mConversationLsv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Conversation conversation = conversationList.get(i);
                Intent intent = new Intent(ConversationActivity.this, ChatActivity.class);
                intent.putExtra(Contans.KEY_TATGETID, conversation.getTargetId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        String api_key = Utils.getMetaValue(ConversationActivity.this, "api_key");
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, api_key);

        conversationAdapter = new ConversationAdapter();
        mConversationLsv.setAdapter(conversationAdapter);
        initImageLoaderOption();
        loadDataFromDB();
        loadDataFromServer();
    }

    private void loadDataFromDB() {
        try {
            conversationDao = new ConversationDao(this);
            conversationList = conversationDao.queryAllByTimeDesc();
            conversationAdapter.notifyDataSetChanged();
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
    private List<Message> loadDataFromServer() {
        String url = UrlHelper.loadConversation();
        Request request = new Request(url);
        request.setiCallBack(new JSONArrayCallBack<Message>() {

            @Override
            public void onSuccess(ArrayList<Message> messages) {
                Log.i("msg", "打印获取数据集合的大小=" + messages.size());
                //将Messages转存到Conversation
                for (Message message : messages) {
                    conversationDao.syncConversation(message);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadDataFromDB();
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

    private class ConversationAdapter extends BaseAdapter {
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
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(ConversationActivity.this).inflate(R.layout.activity_conversation_lsv_item, null);
                viewHolder = new ViewHolder();
                viewHolder.mConversationAvatarImg = (ImageView) convertView.findViewById(R.id.mConversationAvatarImg);
                viewHolder.mConversationNumTip = (TextView) convertView.findViewById(R.id.mConversationNumTip);
                viewHolder.mConversationContentLabel = (TextView) convertView.findViewById(R.id.mConversationContentLabel);
                viewHolder.mConversationUsernameLabel = (TextView) convertView.findViewById(R.id.mConversationUsernameLabel);
                viewHolder.mConversationStatusLabel = (TextView) convertView.findViewById(R.id.mConversationStatusLabel);
                viewHolder.mConversationTimestampLabel = (TextView) convertView.findViewById(R.id.mConversationTimestampLabel);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ImageLoader.getInstance().displayImage(conversationList.get(i).getTargetPicture(), viewHolder.mConversationAvatarImg);

            //viewHolder.mConversationTimestampLabel.setText();
            viewHolder.mConversationUsernameLabel.setText(conversationList.get(i).getTargetName());
            viewHolder.mConversationContentLabel.setText(conversationList.get(i).getContent());
            viewHolder.mConversationNumTip.setText(conversationList.get(i).getUnReadNum() + "");
            return convertView;
        }
    }

    public class ViewHolder {
        private TextView mConversationTimestampLabel;
        private TextView mConversationStatusLabel;
        private TextView mConversationUsernameLabel;
        private TextView mConversationContentLabel;
        private TextView mConversationNumTip;
        private ImageView mConversationAvatarImg;
    }
}
