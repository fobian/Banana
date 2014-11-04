package com.chijsh.banana.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.chijsh.banana.R;

public class PostDialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_post_dialog);

    }

}
