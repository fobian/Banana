package com.chijsh.banana.presentation.view;

import com.chijsh.banana.presentation.model.PostModel;
import com.chijsh.banana.presentation.model.PostsModel;

import java.util.List;

/**
 * Created by chijsh on 3/2/15.
 */
public interface TimeLineView extends LoadDataView {

    public void renderTimeLine(List<PostsModel> posts);

    public void viewPost(PostModel post);

}
