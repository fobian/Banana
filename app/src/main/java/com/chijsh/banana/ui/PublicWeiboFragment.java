package com.chijsh.banana.ui;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.chijsh.banana.R;
import com.chijsh.banana.api.WeiboAPI;

/**
 * Created by chijsh on 10/20/14.
 */
public class PublicWeiboFragment extends Fragment {
    private String text;
    private TextView mTextView;
    public PublicWeiboFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mTextView = (TextView)rootView.findViewById(R.id.test);
        new FetchWeiboTask().execute();
        return rootView;
    }

    public class FetchWeiboTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            text = WeiboAPI.getInstance().getContributors();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            mTextView.setText(text);
        }
    }

}
