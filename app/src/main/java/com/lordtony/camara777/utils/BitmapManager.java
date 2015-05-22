package com.lordtony.camara777.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

public class BitmapManager {

	public static Bitmap setPic(ImageView pictureTaken, String currentPhotoPath) {
        Log.e("Tag90", currentPhotoPath);
		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		int targetW = pictureTaken.getWidth();
		int targetH = pictureTaken.getHeight();
        Log.d("tag90", "medidas " + targetW + " " + targetH);

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
        Log.d("tag90", "medidas2 " + photoW + " " + photoH);
		
		/* Figure out which way needs to be reduced less */
		int scaleFactor = 2;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW/targetW, photoH/targetH);
            Log.d("Tag90", "Entro al if en bitmapManager " + scaleFactor);
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;
        Log.d("Tag90", "Despues " + bmOptions.outWidth + " " + bmOptions.outHeight);
		/* Decode the JPEG file into a Bitmap */
		return BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
	}
}
