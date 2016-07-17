package com.example.user.im;

import android.app.Application;
import android.util.Log;

import com.example.user.im.entity.Profile;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/7/10..
 * ClassName  :
 * Description  :
 */
public class IMApplication extends Application {
    public static String self_id = null;
    private static Profile profile;

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
    }

    public void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static void setProfile(Profile tmp) {
        profile = tmp;
        self_id = tmp.getUserId();
        Log.i("msg", "用户的Id=" + self_id);
    }

    public static Profile getProfile() {
        return profile;
    }

    public static String getUserToken() {
        if (profile == null) {
            return null;
        }
        return profile.getAccess_token();
    }
}

