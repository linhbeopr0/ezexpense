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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.example.mcexpense.R;
import com.sanmboiboi.adapter.CategoryAdapter;

public class ListCategoryActivity extends Activity {

	private ListView category_lstView;
	private CategoryAdapter cAdapter;
	private static String[] data = { "Ăn uống", "Tự trả", "Đi lại", "Ăn chơi",
			"Mua sắm", "Thuốc thang", "Tình yêu tình báo", "Loại nào đây",
			"Nợ nần", "Thêm mới" };
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_category);
		mContext = this;
		category_lstView = (ListView) findViewById(R.id.category_lstView);
		cAdapter = new CategoryAdapter(mContext,
				R.layout.activity_list_category_item, data);
		category_lstView.setAdapter(cAdapter);

		category_lstView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				// on going: 20/02/2014 13:50
				// Toast.makeText(mContext, "20/02/2014 13:50",
				// Toast.LENGTH_LONG).show();
				Intent i = new Intent();
				i.putExtra("category", data[pos]);
				i.putExtra("pos", pos);
				setResult(RESULT_OK, i);
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
		txtTitle.setText("Danh mục");
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
