package com.video.videomaster.activities;

import android.content.Context;
import android.util.AttributeSet;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.video.videomaster.R;

public class CompactTweetViewCustom extends CompactTweetView {


    public CompactTweetViewCustom(Context context, Tweet tweet) {
        super(context, tweet);
    }

    public CompactTweetViewCustom(Context context, Tweet tweet, int styleResId) {
        super(context, tweet, styleResId);
    }

    public CompactTweetViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CompactTweetViewCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected int getLayout() {
        return R.layout.custom_tweet_compact;
    }


}
