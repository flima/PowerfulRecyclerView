package br.com.flima.powerfulrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import br.com.flima.powerfulrecyclerview.listener.OnViewInflateListener;

/**
 * Created by Fernando on 30/03/2016.
 */
public class PowerfulRecyclerView extends FrameLayout {

    private View mLoadingLayout;
    private View mEmptyLayout;
    private View mErrorLayout;

    private int mLoadingLayoutId;
    private int mEmptyLayoutId;
    private int mErrorLayoutId;

    private OnViewInflateListener mOnLoadingInflateListener;
    private OnViewInflateListener mOnEmptyInflateListener;
    private OnViewInflateListener mOnErrorInflateListener;

    private int mPadding;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingBottom;
    private int mPaddingTop;

    private boolean mSwipeRefreshEnabled;
    private int mSwipeRefreshColorOne;
    private int mSwipeRefreshColorTwo;
    private int mSwipeRefreshColorThree;
    private int mSwipeRefreshColorFour;
    private int mSwipeRefreshSize;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public PowerfulRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initLayout();
    }

    public PowerfulRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initLayout();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.PowerfulRecyclerView);
        mLoadingLayoutId = array.getResourceId(R.styleable.PowerfulRecyclerView_layout_loading, 0);
        mEmptyLayoutId = array.getResourceId(R.styleable.PowerfulRecyclerView_layout_empty, 0);
        mErrorLayoutId = array.getResourceId(R.styleable.PowerfulRecyclerView_layout_error, 0);
        mPadding = (int) array.getDimension(R.styleable.PowerfulRecyclerView_recycler_padding, 0);
        if (mPadding == 0) {
            mPaddingLeft = (int) array.getDimension(R.styleable.PowerfulRecyclerView_recycler_padding_left, 0);
            mPaddingRight = (int) array.getDimension(R.styleable.PowerfulRecyclerView_recycler_padding_right, 0);
            mPaddingBottom = (int) array.getDimension(R.styleable.PowerfulRecyclerView_recycler_padding_bottom, 0);
            mPaddingTop = (int) array.getDimension(R.styleable.PowerfulRecyclerView_recycler_padding_top, 0);
        }
        mSwipeRefreshEnabled = array.getBoolean(R.styleable.PowerfulRecyclerView_swipe_to_refresh_enabled, false);
        if (mSwipeRefreshEnabled) {
            mSwipeRefreshColorOne = array.getResourceId(R.styleable.PowerfulRecyclerView_swipe_to_refresh_color_one, 0);
            mSwipeRefreshColorTwo = array.getResourceId(R.styleable.PowerfulRecyclerView_swipe_to_refresh_color_two, 0);
            mSwipeRefreshColorThree = array.getResourceId(R.styleable.PowerfulRecyclerView_swipe_to_refresh_color_three, 0);
            mSwipeRefreshColorFour = array.getResourceId(R.styleable.PowerfulRecyclerView_swipe_to_refresh_color_four, 0);
            mSwipeRefreshSize = array.getInt(R.styleable.PowerfulRecyclerView_swipe_to_refresh_size, SwipeRefreshLayout.DEFAULT);
        }
    }

    private void initLayout() {
        inflate(getContext(), R.layout.template_layout, this);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setLayoutParams(layoutParams);
        setRecyclerPadding();
        if (mSwipeRefreshEnabled) {
            mSwipeRefreshLayout = new SwipeRefreshLayout(getContext());
            mSwipeRefreshLayout.setLayoutParams(layoutParams);
            mSwipeRefreshLayout.addView(mRecyclerView);
            addView(mSwipeRefreshLayout);
            initSwipeRefreshOptions();
        } else {
            addView(mRecyclerView);
        }
    }

    private void initSwipeRefreshOptions() {
        final int[] colorSchemeResources = getColorSchemeResourcesValid();
        if (colorSchemeResources != null) {
            mSwipeRefreshLayout.setColorSchemeResources(getColorSchemeResourcesValid());
        }
        mSwipeRefreshLayout.setSize(mSwipeRefreshSize);
    }

    private void inflateLoadingLayout() {
        if (mLoadingLayout == null && mLoadingLayoutId != 0) {
            ViewStub mLoadingViewStub = ((ViewStub) findViewById(R.id.stub_view_loading));
            mLoadingViewStub.setLayoutResource(mLoadingLayoutId);
            mLoadingLayout = mLoadingViewStub.inflate();
            if (mOnLoadingInflateListener != null) {
                mOnLoadingInflateListener.onInflate(mLoadingLayout);
            }
        }
    }

    private void inflateEmptyLayout() {
        if (mEmptyLayout == null && mEmptyLayoutId != 0) {
            ViewStub mLoadingViewStub = ((ViewStub) findViewById(R.id.stub_view_empty));
            mLoadingViewStub.setLayoutResource(mEmptyLayoutId);
            mEmptyLayout = mLoadingViewStub.inflate();
            if (mOnEmptyInflateListener != null) {
                mOnEmptyInflateListener.onInflate(mEmptyLayout);
            }
        }
    }

    private void inflateErrorLayout() {
        if (mErrorLayout == null && mErrorLayoutId != 0) {
            ViewStub mLoadingViewStub = ((ViewStub) findViewById(R.id.stub_view_error));
            mLoadingViewStub.setLayoutResource(mErrorLayoutId);
            mErrorLayout = mLoadingViewStub.inflate();
            if (mOnErrorInflateListener != null) {
                mOnErrorInflateListener.onInflate(mErrorLayout);
            }
        }
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        mRecyclerView.setLayoutManager(manager);
    }

    public void setAdapter(RecyclerView.Adapter newAdapter) {
        mRecyclerView.setAdapter(newAdapter);
        newAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (mRecyclerView.getAdapter().getItemCount() == 0) {
                    showEmpty();
                } else {
                    hideEmpty();
                }
            }
        });
    }

    public void showLoading() {
        inflateLoadingLayout();
        if (mLoadingLayout != null) {
            mLoadingLayout.setVisibility(VISIBLE);
            mRecyclerView.setVisibility(GONE);
            setupSwipeRefreshLayoutToDisabled();
        }
        if (mEmptyLayout != null) {
            mEmptyLayout.setVisibility(GONE);
        }
        if (mErrorLayout != null) {
            mErrorLayout.setVisibility(GONE);
        }
    }

    public void hideLoading() {
        showRecyclerView();
    }

    private void showEmpty() {
        inflateEmptyLayout();
        if (mEmptyLayout != null) {
            mEmptyLayout.setVisibility(VISIBLE);
            mRecyclerView.setVisibility(GONE);
            setupSwipeRefreshLayoutToDisabled();
        }
        if (mErrorLayout != null) {
            mErrorLayout.setVisibility(GONE);
        }
        if (mLoadingLayout != null) {
            mLoadingLayout.setVisibility(GONE);
        }
        swipeRefreshLayoutRemoveRefresh();
    }

    private void hideEmpty() {
        showRecyclerView();
    }

    public void showError() {
        inflateErrorLayout();
        if (mErrorLayout != null) {
            mErrorLayout.setVisibility(VISIBLE);
            mRecyclerView.setVisibility(GONE);
            setupSwipeRefreshLayoutToDisabled();
        }
        if (mEmptyLayout != null) {
            mEmptyLayout.setVisibility(GONE);
        }
        if (mLoadingLayout != null) {
            mLoadingLayout.setVisibility(GONE);
        }
        swipeRefreshLayoutRemoveRefresh();
    }

    public void hideError() {
        showRecyclerView();
    }

    private void showRecyclerView() {
        mRecyclerView.setVisibility(VISIBLE);
        if (mEmptyLayout != null) {
            mEmptyLayout.setVisibility(GONE);
        }
        if (mErrorLayout != null) {
            mErrorLayout.setVisibility(GONE);
        }
        if (mLoadingLayout != null) {
            mLoadingLayout.setVisibility(GONE);
        }
        swipeRefreshLayoutRemoveRefresh();
        setupSwipeRefreshLayoutToEnabled();
    }

    private int[] getColorSchemeResourcesValid() {
        List<Integer> colors = new ArrayList<Integer>();
        if (mSwipeRefreshColorOne != 0) {
            colors.add(mSwipeRefreshColorOne);
        }
        if (mSwipeRefreshColorTwo != 0) {
            colors.add(mSwipeRefreshColorTwo);
        }
        if (mSwipeRefreshColorThree != 0) {
            colors.add(mSwipeRefreshColorThree);
        }
        if (mSwipeRefreshColorFour != 0) {
            colors.add(mSwipeRefreshColorFour);
        }
        if (colors.isEmpty()) {
            return null;
        } else {
            int[] colorsReturn = new int[colors.size()];
            for (int i = 0; i < colors.size(); i++) {
                colorsReturn[i] = colors.get(i);
            }
            return colorsReturn;
        }
    }

    private void setRecyclerPadding() {
        if (mPadding == 0) {
            mRecyclerView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
        } else {
            mRecyclerView.setPadding(mPadding, mPadding, mPadding, mPadding);
        }
    }

    private void setupSwipeRefreshLayoutToEnabled() {
        if (mSwipeRefreshEnabled) {
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    private void setupSwipeRefreshLayoutToDisabled() {
        if (mSwipeRefreshEnabled) {
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    private void swipeRefreshLayoutRemoveRefresh() {
        if (mSwipeRefreshEnabled) {
            if (swipeRefreshLayoutIsRefreshing()) {
                setSwipeRefreshLayoutRefreshing(false);
            }
        }
    }

    public boolean swipeRefreshLayoutIsRefreshing() {
        if (!mSwipeRefreshEnabled) {
            return false;
        }
        return mSwipeRefreshLayout.isRefreshing();
    }

    public void setSwipeRefreshLayoutRefreshing(boolean refreshing) {
        if (mSwipeRefreshEnabled) {
            mSwipeRefreshLayout.setRefreshing(refreshing);
        }
    }

    public void setSwipeRefreshLayoutSize(int size) {
        if (mSwipeRefreshEnabled) {
            mSwipeRefreshLayout.setSize(size);
        }
    }

    public void setSwipeRefreshLayoutOnRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        if (mSwipeRefreshEnabled) {
            mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        }
    }

    public void setSwipeRefreshLayoutColorSchemeResources(int... colors) {
        if (mSwipeRefreshEnabled) {
            mSwipeRefreshLayout.setColorSchemeResources(colors);
        }
    }

    public void setOnLoadingInflate(OnViewInflateListener onLoadingInflate) {
        mOnLoadingInflateListener = onLoadingInflate;
    }

    public void setOnEmptyInflate(OnViewInflateListener onEmptyInflate) {
        this.mOnEmptyInflateListener = onEmptyInflate;
    }

    public void setOnErrorInflate(OnViewInflateListener onErrorInflate) {
        this.mOnErrorInflateListener = onErrorInflate;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }
}
