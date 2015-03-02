package com.chijsh.banana.presentation.presenter;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by chijsh on 12/18/14.
 */
public interface LoginPresenter extends Presenter {
    public void auth(Activity activity);
    public void onActivityResult(int requestCode, int resultCode, Intent data);
}
