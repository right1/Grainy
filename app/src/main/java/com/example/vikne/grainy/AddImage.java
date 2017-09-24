package com.example.vikne.grainy;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;


import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.*;

import java.io.*;

import javax.net.ssl.ManagerFactoryParameters;

import static android.support.v4.content.FileProvider.getUriForFile;
import static android.widget.ImageView.ScaleType.FIT_CENTER;
import static android.widget.ImageView.ScaleType.FIT_XY;


public class AddImage extends AppCompatActivity {

    TextView outputText;
    ImageView ivImage;
    boolean run=false, showedges=true;
    Bitmap edges, image;
    Integer REQUEST_CAMERA = 1;
    Integer SELECT_FILE = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String pictureImagePath="";
    int flip=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ivImage= (ImageView) findViewById(R.id.imageView);

        ivImage.setScaleType(FIT_CENTER);
        float oldScaleX = ivImage.getScaleX();
        float oldScaleY = ivImage.getScaleY();
        oldScaleX *= 1.2f;
        oldScaleY *= 1.2f;
        ivImage.setScaleX(oldScaleX);
        ivImage.setScaleY(oldScaleY);

        outputText = (TextView) findViewById(R.id.outputText);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show(); */

                SelectImage();
            }
        });
        FloatingActionButton switchbutton = (FloatingActionButton) findViewById(R.id.switchbutton);
        switchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwitchImages();
            }
        });
        FloatingActionButton button01=(FloatingActionButton) findViewById(R.id.Button01);
        button01.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.cameraview.demo");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }
        });
        FloatingActionButton flipbutton=(FloatingActionButton) findViewById(R.id.flipbutton);
        flipbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (ivImage.getRotation()==0.0) {
                    ivImage.setRotation(90);
                }
                else if (ivImage.getRotation()==90.0) {
                    ivImage.setRotation(180);
                }
                else if (ivImage.getRotation()==180.0) {
                    ivImage.setRotation(270);
                }
                else if (ivImage.getRotation()==270.00) {
                    ivImage.setRotation(0);
                }
            }

        });
        //FloatingActionButton fab2=(FloatingActionButton) findViewById(R.id.fab);
        //fab2.setOnClickListener()
        ActivityCompat.requestPermissions(AddImage.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MEDIA_CONTENT_CONTROL}, 1);
    }


    private void SelectImage() {
        /*
        final CharSequence[] items={"Camera","Gallery","Cancel"};
        //AlertDialog.Builder builder=new AlertDialog.Builder(AddImage.this);
        //builder.setTitle("Add Image");
        //builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            //public void onClick(DialogInterface dialogInterface, int i) {
                //if (items[i].equals("Camera [BROKEN]")){
                 //   dialogInterface.dismiss();
                //}
                //else if(items[i].equals("Gallery")){*/
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Select File"),SELECT_FILE);

                /*}
                else if(items[i].equals("Cancel")){
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();*/

    }


    private void SwitchImages() {
        if (run==false){
            return;
        }

        if (showedges==true) {
            ivImage.setImageBitmap(edges);
            showedges=false;

        }
        else{
            ivImage.setImageBitmap(image);
            showedges=true;
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_image, menu);
        return true;
    }


  /*  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            ImageView imageview = (ImageView) findViewById(R.id.ImageView01); //sets imageview as the bitmap
            imageview.setImageBitmap(image);
        }
    }*/


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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    private void openBackCamera() {
        pictureImagePath = "";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName =timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        if(cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if(requestCode == REQUEST_CAMERA) {
                File imgFile = new File(pictureImagePath);

                try {
                    ImageView myImage = (ImageView) findViewById(R.id.ivImage);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;

                    FileInputStream fileStream = new FileInputStream(imgFile);
                    BitmapFactory.decodeStream(fileStream, null, options);

                    final double TARGET_WIDTH = 1280.0;
                    final int ORIG_WIDTH = options.outWidth;
                    final int ORIG_HEIGHT = options.outHeight;

                    final int TARGET_HEIGHT = (int)(ORIG_HEIGHT / (ORIG_WIDTH / TARGET_WIDTH));

                    options.inJustDecodeBounds = false;
                    Bitmap myBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options),
                            (int)TARGET_WIDTH,
                            TARGET_HEIGHT,
                            false);

                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    if (myBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outStream)) {
                        try {
                            File compressed = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/temp.jpg");
                            compressed.createNewFile();
                            FileOutputStream fo = new FileOutputStream(compressed);
                            fo.write(outStream.toByteArray());
                            fo.close();

                            myImage.setImageBitmap(BitmapFactory.decodeFile(compressed.getAbsolutePath()));
                        }
                        catch(IOException e) {

                        }


                    }
                }
                catch (FileNotFoundException e) {
                    // nt found
                }
            }
            else if(requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                ivImage.setImageURI(selectedImageUri);
                if (ivImage.getHeight()>ivImage.getWidth()+50) {
                    ivImage.setRotation(90);
                }

                BitmapDrawable drawable = (BitmapDrawable)ivImage.getDrawable();
                image = drawable.getBitmap();
                outputText.setText(GrainValue());
                showedges = true;
            }
        }


    }

   /* public class OpenCameraDemo extends Activity {

        private static final int CAMERA_PIC_REQUEST = 2500;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_image);

            Button b = (Button)findViewById(R.id.Button01);
            b.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                }
            });
        }

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == CAMERA_PIC_REQUEST) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                ImageView imageview = (ImageView) findViewById(R.id.ivImage);
                imageview.setImageBitmap(image);
            }
        }
    }*/
    public String GrainValue(){
        run=true;
        int width=image.getWidth();
        int height=image.getHeight();
        int factor=1;
        // 720p (mobile)
        while (width*height>921600){
            width/=2;
            height/=2;
            factor*=2;
        }
        System.out.println("Factor: "+factor);
        final double THRESHOLD=.25;
        double[][] pixelValues=new double[height][width];
        double sharpness=0;

        for (int y=0;y<height;y++){
            for (int x=0;x<width;x++){
                int val=image.getPixel(x * factor, y * factor);
                double r= ((val>>16) & 0xFF) / 255f;
                double g= ((val>>8) & 0xFF) / 255f;
                double b = (val & 0xFF) /255f;

                pixelValues[y][x]= (r * 0.2126 + g*.7152 + b*.0722);
                //pixelValues[pixel]=(r+g+b)/3.0;

            }
        }

        double mean=0.0;
        for (int y=0;y<height;y++){
            for (int x=0;x<width;x++){
                mean+=pixelValues[y][x];
            }
        }
        mean/=(height * width);
        double diff;
        double right;
        double down;
        double saturation=0;
        double stdDev=0.0;
        for (int y=0;y<height-1;y++){
            for (int x=0;x<width-1;x++){
                diff=pixelValues[y][x]-mean;
                diff *=diff;
                stdDev+=diff;
                int val=image.getPixel(x * factor, y * factor);
                double r= ((val>>16) & 0xFF) / 255f;
                double g= ((val>>8) & 0xFF) / 255f;
                double b = (val & 0xFF) /255f;
                double max=Math.max(r,Math.max(g,b));
                double min=Math.min(r, Math.min(g, b));
                double delta=max-min;
                if (delta==0){

                }
                else{
                    saturation+=delta/max;
                }
                val=image.getPixel((x+1) * factor, y * factor);
                double r1= ((val>>16) & 0xFF) / 255f;
                double g1= ((val>>8) & 0xFF) / 255f;
                double b1 = (val & 0xFF) /255f;
                val=image.getPixel(x * factor, (y+1) * factor);
                double r2= ((val>>16) & 0xFF) / 255f;
                double g2= ((val>>8) & 0xFF) / 255f;
                double b2 = (val & 0xFF) /255f;
                right=Math.abs(r1-r)+Math.abs(g1-g)+Math.abs(b1-b);
                down=Math.abs(r2-r)+Math.abs(g2-g)+Math.abs(b2-b);
                if (right>THRESHOLD || down>THRESHOLD){
                    sharpness+=right;
                    sharpness+=down;
                }

            }
        }
        stdDev/=(height * width)-1;
        stdDev=Math.sqrt(stdDev);
        sharpness/=(height * width);
        if (sharpness<0){
            sharpness=0;
        }

//        double sharpnessFinal;
//        sharpnessFinal=sharpness*10;
//        sharpnessFinal=Math.pow(sharpnessFinal,0.1)/1.259;
//        sharpnessFinal*=10;

        double snr=10.0 * Math.log10(mean/stdDev);
        saturation/=(height*width);
        saturation*=10;
        if (saturation>4.5){
            saturation=(10-saturation)*4.5/5.5;

        }
        saturation*=(10/4.5);
        double snrFinal=10-10/snr;
        if (snrFinal<0){
            snrFinal=0;
        }
        if (snrFinal>10){

            snrFinal=10;
        }

        CannyEdgeDetector detector = new CannyEdgeDetector();
        detector.setHighThreshold(8f);
        detector.setLowThreshold(1f);

        int eHeight=image.getHeight();
        int eWidth=image.getWidth();

        while (eHeight*eWidth>409920){
            eHeight /= 2;
            eWidth /= 2;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imgBytes = stream.toByteArray();

        Bitmap bmp = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bmp, eWidth, eHeight, false);
        resizedBitmap.setHasAlpha(true);
        //image.recycle();

        detector.setSourceImage(resizedBitmap);
        detector.process();
        edges = detector.getEdgesImage();

        int whiteCount=0;

        for (int y=0;y<eHeight;y++){
            for (int x=0;x<eWidth;x++){
                int val=edges.getPixel(x, y);
                double b= (val & 0xFF) / 255.0;
                if (b > 0.9){
                    whiteCount++;
                }

            }
        }

        double sharpnessFinal=172.0*whiteCount/(eHeight*eWidth);

        if (sharpnessFinal>10){
            sharpnessFinal=10;
        }
        double grain=Math.pow(snrFinal*sharpnessFinal*saturation,1.0/3);
        /*System.out.println("SNR: "+snrFinal);
        System.out.println("Mean: "+mean);
        System.out.println("Std Deviation: "+stdDev);
        System.out.println("Sharpness:"+sharpnessFinal);
        System.out.println("Grainy™ score: "+grain );*/

        snrFinal = Math.round(snrFinal * 100.0) / 100.0;
        sharpnessFinal = Math.round(sharpnessFinal * 100.0) / 100.0;
        saturation=Math.round(saturation*100.0)/100.0;
        grain = Math.round(grain * 100.0) / 100.0;

        String result="SNR: "+snrFinal+"\nSharpness: "+sharpnessFinal+"\nColor Score: "+saturation+"\nGrainy™ score: "+grain;
        return result;
    }
}
