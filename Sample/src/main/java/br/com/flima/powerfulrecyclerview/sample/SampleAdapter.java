package br.com.flima.powerfulrecyclerview.sample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Fernando on 31/03/2016.
 */
public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ViewHolder> {

    private List<String> mTexts;

    public SampleAdapter(String[] texts) {
        this.mTexts = new ArrayList<>(texts.length);
        for (String text : texts) {
            mTexts.add(text);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String text = mTexts.get(position);
        holder.mSampleText.setText(text);
    }

    @Override
    public int getItemCount() {
        if (mTexts == null) {
            return 0;
        }
        return mTexts.size();
    }

    public void addAll(String[] texts) {
        mTexts.clear();
        mTexts.addAll(Arrays.asList(texts));
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView mSampleText;

        public ViewHolder(View v) {
            super(v);
            mSampleText = (TextView) v.findViewById(R.id.item_sample_text);
        }
    }

}
