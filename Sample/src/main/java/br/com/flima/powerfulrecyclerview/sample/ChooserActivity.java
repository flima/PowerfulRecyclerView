package br.com.flima.powerfulrecyclerview.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ChooserActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
        initializeComponents();
    }

    private void initializeComponents() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        findViewById(R.id.btn_chooser_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooserActivity.this, PowerfulRecyclerSampleActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_chooser_empty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooserActivity.this, PowerfulRecyclerSampleActivity.class);
                intent.putExtra(PowerfulRecyclerSampleActivity.PARAM_SHOW_EMPTY, true);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_chooser_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooserActivity.this, PowerfulRecyclerSampleActivity.class);
                intent.putExtra(PowerfulRecyclerSampleActivity.PARAM_SHOW_ERROR, true);
                startActivity(intent);
            }
        });
    }
}
