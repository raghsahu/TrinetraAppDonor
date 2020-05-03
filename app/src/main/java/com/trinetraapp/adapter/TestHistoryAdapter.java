package com.trinetraapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.trinetraapp.BR;
import com.trinetraapp.R;
import com.trinetraapp.databinding.TestHistoryItemBinding;
import com.trinetraapp.model.test_history.Test_History_Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raghvendra Sahu on 03-May-20.
 */
public class TestHistoryAdapter extends RecyclerView.Adapter<TestHistoryAdapter.ViewHolder> {

    private List<Test_History_Data> dataModelList;
    Context context;


    public TestHistoryAdapter(List<Test_History_Data> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TestHistoryItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.test_history_item, parent, false);

        return new ViewHolder(binding);



    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Test_History_Data dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.setModel(dataModel);
        // holder.itemRowBinding.setItemClickListener(this);

        // Log.e("all_artisansSize", ""+dataModel.getProductName());


    }



    @Override
    public int getItemCount() {
        return dataModelList.size();//return arraylist size count item
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TestHistoryItemBinding itemRowBinding;

        public ViewHolder(TestHistoryItemBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.model, obj);
            itemRowBinding.executePendingBindings();
        }
    }





}
