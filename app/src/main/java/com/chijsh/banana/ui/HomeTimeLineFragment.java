package com.chijsh.banana.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chijsh.banana.R;

import com.chijsh.banana.data.PostContract.PostEntry;
import com.chijsh.banana.sync.WeiboSyncAdapter;
import com.chijsh.banana.widget.fab.FloatingActionButton;
import com.chijsh.banana.widget.satellitemenu.SatelliteMenu;
import com.chijsh.banana.widget.satellitemenu.SatelliteMenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chijsh on 10/20/14.
 */
public class HomeTimeLineFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
                                                                SwipeRefreshLayout.OnRefreshListener {
    private static final int POST_LOADER = 0;

    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mRecyclerView;
    private CursorRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mFloatingButton;

    private static final String[] POST_COLUMNS = {
            PostEntry.TABLE_NAME + "." + PostEntry._ID,
            PostEntry.COLUMN_CREATED_AT,
            PostEntry.COLUMN_POST_ID,
            PostEntry.COLUMN_POST_TEXT,
            PostEntry.COLUMN_POST_SOURCE,
            PostEntry.COLUMN_POST_FAVORITED,
            PostEntry.COLUMN_POST_PICURLS,
            PostEntry.COLUMN_POST_GEO,
            PostEntry.COLUMN_USER_ID,
            PostEntry.COLUMN_USER_SCREENNAME,
            PostEntry.COLUMN_USER_AVATAR,
            PostEntry.COLUMN_RETWEETED_ID,
            PostEntry.COLUMN_RETWEETED_USER_SCREENNAME,
            PostEntry.COLUMN_RETWEETED_TEXT,
            PostEntry.COLUMN_REPOST_COUNT,
            PostEntry.COLUMN_COMMENT_COUNT,
            PostEntry.COLUMN_ATTITUDE_COUNT,

    };

    public HomeTimeLineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

        mSwipeLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_container);
        mSwipeLayout.setColorSchemeColors(getResources().getColor(R.color.theme_accent));
        mSwipeLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.time_line);

        //mFloatingButton = (FloatingActionButton)rootView.findViewById(R.id.fab);
        //mFloatingButton.attachToRecyclerView(mRecyclerView);


        SatelliteMenu menu = (SatelliteMenu) rootView.findViewById(R.id.sat);
        //menu.setRotation(270);

        List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
        items.add(new SatelliteMenuItem(4, R.drawable.ic_camera_alt_black_24dp));
        items.add(new SatelliteMenuItem(3, R.drawable.ic_image_black_24dp));
        items.add(new SatelliteMenuItem(2, R.drawable.ic_format_quote_black_24dp));
        items.add(new SatelliteMenuItem(1, R.drawable.ic_room_black_24dp));
//        items.add(new SatelliteMenuItem(5, R.drawable.sat_item));
        menu.addItems(items);

        menu.setOnItemClickedListener(new SatelliteMenu.SateliteClickedListener() {

            public void eventOccured(int id) {
                Log.i("sat", "Clicked on " + id);
            }
        });

        menu.attachToRecyclerView(mRecyclerView);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(getActivity(), null);
        mRecyclerView.setAdapter(mAdapter);


        return rootView;
    }

    @Override
    public void onRefresh() {
        refreshTimeLine();
    }

    private void refreshTimeLine() {
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
        mAdapter.swapCursor(data);
        mSwipeLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}
