package br.com.flima.powerfulrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

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

    private RecyclerView mRecyclerView;

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
    }

    private void initLayout() {
        inflate(getContext(), R.layout.template_layout, this);

        if (isInEditMode()) {
            return;
        }

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //TODO: SETUP HERE ANOTHER ATTRIBUTES FROM RECYCLER
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
        }
        if (mErrorLayout != null) {
            mErrorLayout.setVisibility(GONE);
        }
        if (mLoadingLayout != null) {
            mLoadingLayout.setVisibility(GONE);
        }
    }

    private void hideEmpty() {
        showRecyclerView();
    }

    public void showError() {
        inflateErrorLayout();
        if (mErrorLayout != null) {
            mErrorLayout.setVisibility(VISIBLE);
            mRecyclerView.setVisibility(GONE);
        }
        if (mEmptyLayout != null) {
            mEmptyLayout.setVisibility(GONE);
        }
        if (mLoadingLayout != null) {
            mLoadingLayout.setVisibility(GONE);
        }
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
}
