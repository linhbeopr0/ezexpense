package com.sanmboiboi.adapter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mcexpense.R;
import com.sanmboiboi.activity.AddPersonActivity;
import com.sanmboiboi.storage.PersonInfo;

public class ShowReportAdapter extends ArrayAdapter<PersonInfo> {
	private final Context mContext;
	private final int layout;
	private final ArrayList<PersonInfo> data;
	private Holder holder;

	public ShowReportAdapter(Context context, int resource,
			ArrayList<PersonInfo> _data) {
		super(context, resource);
		mContext = context;
		layout = resource;
		data = _data;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// Log.d("LINH", "getCount|size = " + data.size());
		return data.size();
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
			holder.txtName = (TextView) row.findViewById(R.id.rp_txtName);
			holder.txtBalance = (TextView) row.findViewById(R.id.rp_txtBalance);
			holder.imgView = (ImageView) row.findViewById(R.id.rp_imgAvatar);
			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}

		holder.txtName.setText(data.get(position).getName());
		Log.d("LINH", data.get(position).getName());
		holder.txtBalance.setText(data.get(position).getBalance() + "");
		String path = data.get(position).getPath();
		Log.d("MCE", "path in adapter " + path);
		if (path.equals("")) {
			Bitmap tmpBitmap = getRoundedShape(BitmapFactory.decodeResource(
					mContext.getResources(),
					R.drawable.icon_avatar_default_2));
			holder.imgView.setImageBitmap(tmpBitmap);
		} else {
			try {
				holder.imgView.setImageBitmap(getRoundedShape(decodeUri(Uri
						.parse(path))));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return row;
	}

	private static class Holder {
		TextView txtName;
		ImageView imgView;
		TextView txtBalance;
	}

	public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
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
		final int REQUIRED_SIZE = 250;

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

}
