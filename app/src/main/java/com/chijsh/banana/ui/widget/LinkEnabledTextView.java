package com.chijsh.banana.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.chijsh.banana.R;

import java.util.regex.Pattern;

/**
 * Created by chijsh on 10/30/14.
 */
public class LinkEnabledTextView extends TextView {

    private Context context;

    public interface TextLinkClickListener {
        public void onTextLinkClick(View textView, String clickedString);
    }

    // A Listener Class for generally sending the Clicks to the one which requires it
    TextLinkClickListener mListener;

    // Pattern for gathering @usernames from the Text
    public static final Pattern SCREEN_NAME_PATTERN = Pattern.compile("@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}");
    public static final String SCREEN_NAME_SCHEME = "com.chijsh.banana://";

    // Pattern for gathering #hasttags# from the Text
    public static final Pattern HASH_TAGS_PATTERN = Pattern.compile("#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#");
    public static final String HASH_TAGS_SCHEME = "com.chijsh.banana://";
    // Pattern for gathering http:// links from the Text
    public static final Pattern HYPER_LINK_PATTERN = Pattern.compile("http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]");
    public static final String HYPER_LINK_SCHEME = "com.chijsh.banana://";

    public static final Pattern EMOTION_PATTERN = Pattern.compile("\\[(\\S+?)\\]");

    public LinkEnabledTextView(Context context) {
        super(context);
        this.context = context;
    }

    public LinkEnabledTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

    }

    public LinkEnabledTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LinkEnabledTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    public void gatherLinksForText(String text) {
        SpannableString value = SpannableString.valueOf(text);
        /*
         *  gatherLinks basically collects the Links depending upon the Pattern that we supply
         *  and add the links to the ArrayList of the links
         */
        Linkify.addLinks(value, SCREEN_NAME_PATTERN, SCREEN_NAME_SCHEME);
        Linkify.addLinks(value, HYPER_LINK_PATTERN, HYPER_LINK_SCHEME);
        Linkify.addLinks(value, HASH_TAGS_PATTERN, HASH_TAGS_SCHEME);

        URLSpan[] urlSpans = value.getSpans(0, value.length(), URLSpan.class);
        InternalURLSpan weiboSpan = null;
        for (URLSpan urlSpan : urlSpans) {
            weiboSpan = new InternalURLSpan(urlSpan.getURL());
            int start = value.getSpanStart(urlSpan);
            int end = value.getSpanEnd(urlSpan);
            value.removeSpan(urlSpan);
            value.setSpan(weiboSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        setText(value);
    }

    public void setOnTextLinkClickListener(TextLinkClickListener newListener) {
        mListener = newListener;
    }

    public class InternalURLSpan extends ClickableSpan {
        private String clickedSpan;

        public InternalURLSpan(String clickedString) {
            clickedSpan = clickedString;
        }

        @Override
        public void onClick(View textView) {
            mListener.onTextLinkClick(textView, clickedSpan);
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            tp.setColor(context.getResources().getColor(R.color.txt_link));
        }
    }

}