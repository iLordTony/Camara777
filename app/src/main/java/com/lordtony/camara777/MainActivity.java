package com.lordtony.camara777;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.lordtony.camara777.utils.AlbumStorageDirFactory;
import com.lordtony.camara777.utils.BitmapManager;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static final int TAKE_PICTURE = 1;
    private static final int SELECT_PICTURE = 2;
    private static final String ALBUM_NAME = "Album90";
    private Bitmap mImageBitmap;
    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private ImageView pictureTaken;
    private String currentPhotoPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageBitmap = null;
        pictureTaken = (ImageView) findViewById(R.id.picture_taken);
        Log.d("Tag90", "Se ejecute el onCreate" + pictureTaken.getHeight() + " " + pictureTaken.getWidth());
    }



    public void takePictureFromCamera(View v) {
        if (isIntentAvailable(this, MediaStore.ACTION_IMAGE_CAPTURE)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = null;
            try{
                f = AlbumStorageDirFactory.setUpPhotoFile(ALBUM_NAME);
                currentPhotoPath = f.getAbsolutePath();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                Log.d("Tag90", currentPhotoPath);
                Log.d("Tag90", "Entro al try takepictureFromcamera");

            }catch(IOException e){
                e.printStackTrace();
                f = null;
                currentPhotoPath = null;
                Log.e("Tag90", "Es nulo?");
            }
            startActivityForResult(intent, TAKE_PICTURE);
        }
    }

    public void takePictureFromGallery(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture plis"), SELECT_PICTURE);

    }

    private void addPictureToGallery(){
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        intent.setData(contentUri);
        this.sendBroadcast(intent);
        Log.d("Tag90", "Entro a addPictureToGallery");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Log.d("Tag90", "Antes del currentPhotoPath");
            Log.d("Tag90", currentPhotoPath);
            switch (requestCode) {
                case TAKE_PICTURE: {
                    Log.d("Tag90", "Entro al case take_picture");
                    if (currentPhotoPath != null) {
                        Log.d("Tag90", "Inicio del if currentPhotoPath");
                        mImageBitmap = BitmapManager.setPic(pictureTaken, currentPhotoPath);
                        pictureTaken.setImageBitmap(mImageBitmap);
                        addPictureToGallery();
                        currentPhotoPath = null;
                    }
                    break;
                }
                case SELECT_PICTURE: {
                    Log.d("Tag90", "Entro al select_picture");
                    Uri selectedImageUri = data.getData();
                    currentPhotoPath = AlbumStorageDirFactory.getImageFromGalleryPath(this, selectedImageUri);
                    pictureTaken.setImageURI(selectedImageUri);
                    break;
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
        super.onSaveInstanceState(outState);
        Log.d("Tag90", "Se ejecute el onSave");

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
        Log.d("Tag90", "Se ejecute el onRestore");

    }

    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
