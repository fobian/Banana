package com.chijsh.banana.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.chijsh.banana.R;

import com.chijsh.banana.data.PostContract.PostEntry;
import com.chijsh.banana.sync.WeiboSyncAdapter;

/**
 * Created by chijsh on 10/20/14.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int POST_LOADER = 0;

    private ListView listView;
    private SimpleCursorAdapter mPostAdapter;

    private static final String[] POST_COLUMNS = {
            PostEntry.TABLE_NAME + "." + PostEntry._ID,
            PostEntry.COLUMN_CONTRIBUTOR,
            PostEntry.COLUMN_CONTRIBUTIONS

    };

    public static final int COL_POST_ID = 0;
    public static final int COL_POST_CONTRIBUTOR = 1;
    public static final int COL_POST_CONTRIBUTIONS = 2;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mPostAdapter =
                new SimpleCursorAdapter(
                        getActivity(),
                        R.layout.list_item,
                        null,
                        // the column names to use to fill the textviews
                        new String[]{PostEntry.COLUMN_CONTRIBUTOR,
                                PostEntry.COLUMN_CONTRIBUTIONS,

                        },
                        // the textviews to fill with the data pulled from the columns above
                        new int[]{R.id.contributor,
                                R.id.contributions,

                        },
                        0);

        mPostAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                switch (columnIndex) {
                    case COL_POST_CONTRIBUTOR: {
                        // we have to do some formatting and possibly a conversion
                        ((TextView) view).setText(cursor.getString(columnIndex));
                        return true;
                    }
                    case COL_POST_CONTRIBUTIONS: {
                        int num = cursor.getInt(columnIndex);
                        TextView dateView = (TextView) view;
                        dateView.setText(num + "");
                        return true;
                    }
                }
                return false;
            }
        });

        listView = (ListView)rootView.findViewById(R.id.test);
        listView.setAdapter(mPostAdapter);
        return rootView;
    }

    private void updateWeibo() {
        WeiboSyncAdapter.syncImmediately(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(POST_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        Uri uri = PostEntry.CONTENT_URI;
        String sortOrder = PostEntry._ID + " ASC";

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                uri,
                POST_COLUMNS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPostAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPostAdapter.swapCursor(null);
    }



}
