package com.chijsh.banana.presentation.view;

/**
 * Created by chijsh on 3/2/15.
 */
public interface LoadDataView extends View {

    public void showLoading();

    public void hideLoading();

    //void showRetry();

    //void hideRetry();

    public void showError(String message);

}
