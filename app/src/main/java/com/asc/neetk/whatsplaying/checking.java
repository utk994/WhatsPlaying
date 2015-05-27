package com.asc.neetk.whatsplaying;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.wefika.flowlayout.FlowLayout;


public class checking extends ActionBarActivity {
    private FlowLayout mFlowLayout;
    Integer i =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking);

        mFlowLayout = (FlowLayout) findViewById(R.id.flow);
    }

    public void addItem(View view) {

        int color = getResources().getColor(R.color.darkpurple);



        final TextView newView = new TextView(this);
        newView.setGravity(Gravity.CENTER);
        TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        newView.setLayoutParams(params1);
        newView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        newView.setTextColor(getResources().getColor(R.color.white));
        newView.setBackgroundResource(R.drawable.roundedrect);
        newView.setPadding(10,10,10,10);
        newView.setText("Breaking Benjamin "+String.valueOf(i));
        i = i+1;

        newView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int index=  mFlowLayout.indexOfChild(view);

                Log.d("index",String.valueOf(index));

                mFlowLayout.removeViewAt(index);

            }
        });

        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = 10;
        params.bottomMargin =10;
        newView.setLayoutParams(params);

        mFlowLayout.addView(newView);
    }

    public void removeItem(View view) {


        mFlowLayout.removeView(getLastView());

    }

    public void toggleItem(View view) {

        View last = getLastView();

        if(last.getVisibility() == View.VISIBLE) {
            last.setVisibility(View.GONE);
        } else {
            last.setVisibility(View.VISIBLE);
        }

    }

    private View getLastView() {
        return mFlowLayout.getChildAt(mFlowLayout.getChildCount() - 1);
    }

}