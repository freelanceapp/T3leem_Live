package com.t3leem_live.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.t3leem_live.R;
import com.t3leem_live.databinding.SpinnerRowBinding;
import com.t3leem_live.models.GroupOfTeacher;
import com.t3leem_live.models.StageClassModel;

import java.util.List;

import io.paperdb.Paper;

public class SpinnerGroupOfTeacherAdapter extends BaseAdapter {
    private List<GroupOfTeacher> list;
    private Context context;
    private String lang;
    private LayoutInflater inflater;
    public SpinnerGroupOfTeacherAdapter(List<GroupOfTeacher> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") SpinnerRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.spinner_row,viewGroup,false);
        GroupOfTeacher model= list.get(i);
        binding.setTitle(model.getTitle()+" "+model.getDesc());

        return binding.getRoot();
    }
}
