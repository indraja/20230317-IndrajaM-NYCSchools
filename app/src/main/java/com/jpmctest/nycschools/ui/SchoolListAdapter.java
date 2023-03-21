package com.jpmctest.nycschools.ui;

import android.view.ViewGroup;

import com.jpmctest.nycschools.pojo.School;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class SchoolListAdapter extends ListAdapter<School, SchoolSimpleViewHolder> {

    public SchoolListAdapter(@NonNull DiffUtil.ItemCallback<School> diffCallback) {
        super(diffCallback);
    }

    @Override
    public SchoolSimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return SchoolSimpleViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(SchoolSimpleViewHolder holder, int position) {
        School current = getItem(position);
        holder.bind(current);
    }

    public static class SchoolDiff extends DiffUtil.ItemCallback<School> {

        @Override
        public boolean areItemsTheSame(@NonNull School oldItem, @NonNull School newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull School oldItem, @NonNull School newItem) {
            return oldItem.getSchool_name().equals(newItem.getSchool_name());
        }
    }
}
