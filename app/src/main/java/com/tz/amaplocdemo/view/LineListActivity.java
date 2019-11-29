package com.tz.amaplocdemo.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tz.amaplocdemo.R;
import com.tz.amaplocdemo.adapter.LineAdapter;
import com.tz.amaplocdemo.bean.Line;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineListActivity extends AppCompatActivity {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private LineAdapter lineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_list);
        ButterKnife.bind(this);

        List<Line> lines = LitePal.findAll(Line.class);
        lineAdapter = new LineAdapter(R.layout.item_line, lines);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(lineAdapter);
        lineAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(LineListActivity.this,LineDetailActivity.class);
                intent.putExtra("lineId",lineAdapter.getItem(position).getId());
                startActivity(intent);
            }
        });

    }


}
