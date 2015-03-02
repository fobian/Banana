package com.chijsh.banana.presentation.ui;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chijsh.banana.AccessTokenKeeper;
import com.chijsh.banana.R;
import com.chijsh.banana.utils.PrefUtil;
import com.chijsh.banana.utils.Utility;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = getActionBarToolbar();
        toolbar.setTitle(R.string.title_activity_settings);
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                navigateUpToFromChild(SettingsActivity.this,
//                        IntentCompat.makeMainActivity(new ComponentName(SettingsActivity.this,
//                                MainActivity.class)));
                finish();
            }
        });

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new SettingsFragment())
                    .commit();
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        public SettingsFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setupPreferencesScreen();
            Preference pref = findPreference(PrefUtil.PREF_SIGN_OUT);
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    if (Utility.getSDKVersion() >= 19) {
                        ActivityManager manager = (ActivityManager)getActivity().getSystemService(ACTIVITY_SERVICE);
                        manager.clearApplicationUserData();
                    } else {
                        //TODO clear data
                        AccessTokenKeeper.clear(getActivity());
                    }

                    Intent intent = new Intent(getActivity(), TimeLineActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                }
            });
            //register
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            //unregister
        }

        private void setupPreferencesScreen() {
            addPreferencesFromResource(R.xml.preferences);
        }


        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        }
    }
}
