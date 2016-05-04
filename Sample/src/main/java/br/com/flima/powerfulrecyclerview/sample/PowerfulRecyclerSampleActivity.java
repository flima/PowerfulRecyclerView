package br.com.flima.powerfulrecyclerview.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import br.com.flima.powerfulrecyclerview.PowerfulRecyclerView;
import br.com.flima.powerfulrecyclerview.listener.OnViewInflateListener;

/**
 * Created by Fernando on 02/04/2016.
 */
public class PowerfulRecyclerSampleActivity extends AppCompatActivity {

    public static final String PARAM_SHOW_ERROR = "PARAM_SHOW_ERROR";
    public static final String PARAM_SHOW_EMPTY = "PARAM_SHOW_EMPTY";

    private Toolbar mToolbar;

    private SampleAdapter mAdapter;
    private PowerfulRecyclerView mPowerfulRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_powerful_recycler_sample);
        setupToolbar();
        setupPowerfulRecyclerView();
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    private void setupPowerfulRecyclerView() {
        mPowerfulRecyclerView = (PowerfulRecyclerView) findViewById(R.id.powerful_recycler_view);
        mAdapter = new SampleAdapter(getResources().getStringArray(R.array.sample_texts));
        mPowerfulRecyclerView.showLoading();
        mPowerfulRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPowerfulRecyclerView.setAdapter(mAdapter);

        // It is just to show how to receive the view inflated and manipulate it.
        // There are listeners to receive the empty and error view too.
        mPowerfulRecyclerView.setOnLoadingInflate(new OnViewInflateListener() {
            @Override
            public void onInflate(View view) {
                final TextView loadingText = (TextView) view.findViewById(R.id.loading_text);
                loadingText.setText(R.string.loading);
            }
        });

        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Intent intent = getIntent();
                        final boolean showEmptyLayout = intent.getBooleanExtra(PARAM_SHOW_EMPTY, false);
                        final boolean showErrorLayout = intent.getBooleanExtra(PARAM_SHOW_ERROR, false);
                        if (showEmptyLayout) {
                            // When there aren't items, will be displayed empty view
                            mAdapter.addAll(new String[]{});
                        } else if (showErrorLayout) {
                            // This call is necessary because the lib doesn't have control over the error.
                            mPowerfulRecyclerView.showError();
                        } else {
                            mPowerfulRecyclerView.hideLoading();
                        }
                    }
                }, 3000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
