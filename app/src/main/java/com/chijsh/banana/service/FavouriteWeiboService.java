package com.chijsh.banana.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

import com.chijsh.banana.AccessTokenKeeper;
import com.chijsh.banana.R;
import com.chijsh.banana.api.WeiboAPI;
import com.chijsh.banana.event.MessageEvent;
import com.chijsh.banana.ui.TimeLineFragment;
import com.chijsh.banana.data.PostContract.PostEntry;

import de.greenrobot.event.EventBus;
import retrofit.client.Response;

/**
 * Created by chijsh on 11/7/14.
 */
public class FavouriteWeiboService extends IntentService {

    public FavouriteWeiboService() {
        super("FavouriteWeiboService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String postId = intent.getStringExtra(TimeLineFragment.POST_ID_EXTRA);
        boolean isFavourited = intent.getBooleanExtra(TimeLineFragment.IS_FAVOURITED_EXTRA, false);
        String token = AccessTokenKeeper.readAccessToken(this).getToken();
        Response response;
        if (isFavourited) {
            response = WeiboAPI.getInstance().deleteFavorites(token, Long.parseLong(postId));
            if (response.getStatus() == 200) {
                EventBus.getDefault().post(new MessageEvent(getString(R.string.deleted_favourited)));
                updateLocalData(postId, false);
            }
        } else {
            response = WeiboAPI.getInstance().addFavorites(token, Long.parseLong(postId));
            if (response.getStatus() == 200) {
                EventBus.getDefault().post(new MessageEvent(getString(R.string.added_to_favourited)));
                updateLocalData(postId, true);
            }
        }

    }

    private void updateLocalData(String postId, boolean isFavorited) {
        ContentValues values = new ContentValues();
        values.put(PostEntry.COLUMN_POST_FAVORITED, isFavorited);
        String selection = PostEntry.COLUMN_POST_ID + " = '" + postId + "'";
        getContentResolver().update(PostEntry.CONTENT_URI, values, selection, null);
    }
}
