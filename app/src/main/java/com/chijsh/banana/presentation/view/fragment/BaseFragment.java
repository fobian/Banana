package com.chijsh.banana.presentation.view.fragment;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by chijsh on 3/3/15.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        initPresenter();
    }

    abstract void initPresenter();
}


