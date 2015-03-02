package com.chijsh.banana.presentation.presenter;

import com.chijsh.banana.presentation.model.PostModel;

/**
 * Created by chijsh on 3/2/15.
 */
public interface TimeLinePresenter extends Presenter {

    public void loadTimeLine();

    public void showError();

    public void addFavourite(String postId);

    public void removeFavourite(String postId);

}
