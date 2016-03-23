package com.sanmboiboi.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mcexpense.R;
import com.sanmboiboi.adapter.ListPersonAdapter;

public class ListPersonActivity extends Activity {
    private ListView lstPerson;
    private Button lstBtnSave;
    private ListPersonAdapter lAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense_list_person);
        mContext = this;
        lstPerson = (ListView) findViewById(R.id.listPerson);
        lstBtnSave = (Button) findViewById(R.id.listPerson_saveBtn);
        lAdapter = new ListPersonAdapter(mContext, R.layout.activity_add_expense_list_person_item, MainActivity.data);
        lstPerson.setAdapter(lAdapter);

        lstBtnSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        
    	// Action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.activity_back_actionbar, null);
		ImageView btnBack = (ImageView) v.findViewById(R.id.btnBackAcb);
		@SuppressWarnings("deprecation")
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		actionBar.setCustomView(v, lp);
		TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
		txtTitle.setText("Chia tiền nào");
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						AddExpenseActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});

	}
    
}
