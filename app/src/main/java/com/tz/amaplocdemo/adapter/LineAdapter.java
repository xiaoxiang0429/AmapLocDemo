package com.tz.amaplocdemo.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tz.amaplocdemo.R;
import com.tz.amaplocdemo.bean.Line;
import com.tz.amaplocdemo.util.DateUtils;

import java.util.List;

public class LineAdapter extends BaseQuickAdapter<Line, BaseViewHolder> {

    public LineAdapter(int layoutResId, @Nullable List<Line> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Line item) {
        helper.setText(R.id.tv_line_name,item.getLineName())
                .setText(R.id.tv_create_time, DateUtils.stamp2Date(item.getCreateTime(),DateUtils.simpleFormat))
                .setText(R.id.tv_end_time, DateUtils.stamp2Date(item.getEndTime(),DateUtils.simpleFormat));
    }
}
