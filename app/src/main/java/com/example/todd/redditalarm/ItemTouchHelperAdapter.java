package com.example.todd.redditalarm;

/**
 * Created by Todd on 6/21/2016.
 */
public interface ItemTouchHelperAdapter {
    boolean moveDrag(int from, int to);

    void dismissSwipe(int position);
}
