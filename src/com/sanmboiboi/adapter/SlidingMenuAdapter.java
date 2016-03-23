package com.sanmboiboi.adapter;

import java.io.FileNotFoundException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mcexpense.R;

public class SlidingMenuAdapter extends ArrayAdapter<String> {
	private final Context mContext;
	private final int layout;
	private final String[] data;
	private Holder holder;

	public SlidingMenuAdapter(Context context, int resource, String[] objects) {
		super(context, resource, objects);
		mContext = context;
		layout = resource;
		data = objects;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.length;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = convertView;

		if (row == null) {
			holder = new Holder();
			row = inflater.inflate(layout, null);
			holder.txtView = (TextView) row
					.findViewById(R.id.txtSlidingMenuItem);
			holder.imgView = (ImageView) row
					.findViewById(R.id.imgSlidingMenuItem);
			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}

		holder.txtView.setText(data[position]);

		if (data[position].equals("Thêm người")) {
			holder.imgView.setImageBitmap(makeBitmapFromRes(mContext, R.drawable.sldmenu_add));
		} else if (data[position].equals("Tổng kết")) {
			holder.imgView.setImageBitmap(makeBitmapFromRes(mContext, R.drawable.sldmenu_about));
		} else if (data[position].equals("Tìm hiểu")) {
			holder.imgView.setImageBitmap(makeBitmapFromRes(mContext, R.drawable.sldmenu_about));
		} else if (data[position].equals("Khởi tạo lại")) {
			holder.imgView.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sldmenu_reset), 60, 60, true));
		}

		return row;
	}
	
	private Bitmap makeBitmapFromRes(Context mContext, int res) {
		Bitmap bm;
		bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mContext.getResources(), res), 80, 80, true);
		return bm;
	}
	private Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		int targetWidth = 150;
		int targetHeight = 150;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
				targetHeight), null);
		return targetBitmap;
	}

	private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(mContext.getContentResolver()
				.openInputStream(selectedImage), null, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 200;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeStream(mContext.getContentResolver()
				.openInputStream(selectedImage), null, o2);
	}


	private static class Holder {
		TextView txtView;
		ImageView imgView;
	}

}
