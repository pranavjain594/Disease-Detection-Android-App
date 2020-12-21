package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class MainActivity2 extends AppCompatActivity {
    ImageView iv;
    FirebaseCustomRemoteModel remoteModel;
    Interpreter interpreter;
    ByteBuffer input;
    ByteBuffer modelOutput;
    String dis;
    int res;
    String mCurrentPhotoUri;

    private static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 3;
    static final int REQUEST_IMAGE_GALLERY = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle("Upload the Image");
        iv=findViewById(R.id.iv2);

        Intent i=getIntent();
        res=i.getIntExtra("disease",0);
        if(res==1) dis="pneumonia";
        else if(res==2) dis="malaria";
        else if(res==3) dis="breast_cancer";
        else if(res==4) dis="skin_cancer";
        else dis="covid";
        //Toast.makeText(this, dis+res, Toast.LENGTH_SHORT).show();

        remoteModel =
                new FirebaseCustomRemoteModel.Builder(dis).build();
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        FirebaseModelManager.getInstance().download(remoteModel, conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Download complete. Depending on your app, you could enable
                        // the ML feature, or switch from the local model to the remote
                        // model, etc.
                        //Toast.makeText(MainActivity2.this, dis+"Succsessful", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void detect(View v){
        loadModel();
    }

    public void loadModel(){
        //Toast.makeText(this, "inside load", Toast.LENGTH_SHORT).show();
        remoteModel = new FirebaseCustomRemoteModel.Builder(dis).build();
        FirebaseModelManager.getInstance().getLatestModelFile(remoteModel)
                .addOnCompleteListener(new OnCompleteListener<File>() {
                    @Override
                    public void onComplete(@NonNull Task<File> task) {
                        File modelFile = task.getResult();
                        if (modelFile != null) {
                            interpreter = new Interpreter(modelFile);
                           // Toast.makeText(MainActivity2.this, "Hosted Model loaded.. Running interpreter..", Toast.LENGTH_SHORT).show();
                            runningInterpreter();
                        }
                        else{
                            try {
                                InputStream inputStream = getAssets().open(dis+".tflite");
                                byte[] model = new byte[inputStream.available()];
                                inputStream.read(model);
                                ByteBuffer buffer = ByteBuffer.allocateDirect(model.length)
                                        .order(ByteOrder.nativeOrder());
                                buffer.put(model);
                                //Toast.makeText(MainActivity2.this, dis+"Bundled Model loaded.. Running interpreter..", Toast.LENGTH_SHORT).show();
                                interpreter = new Interpreter(buffer);
                                runningInterpreter();
                            } catch (IOException e) {
                                // File not found?
                                Toast.makeText(MainActivity2.this, "No hosted or bundled model", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void gettingImageInput(){
        //Toast.makeText(this, "getting image input..", Toast.LENGTH_SHORT).show();
        Bitmap bm = getYourInputImage();
        int z;
        if(res==2) { z=50; }
        else { z=224; }
        Bitmap bitmap = Bitmap.createScaledBitmap(bm, z, z, true);
        input = ByteBuffer.allocateDirect(z * z * 3 * 4).order(ByteOrder.nativeOrder());
        for (int y = 0; y < z; y++) {
            for (int x = 0; x < z; x++) {
                int px = bitmap.getPixel(x, y);

                // Get channel values from the pixel value.
                int r = Color.red(px);
                int g = Color.green(px);
                int b = Color.blue(px);

                // Normalize channel values to [-1.0, 1.0]. This requirement depends
                // on the model. For example, some models might require values to be
                // normalized to the range [0.0, 1.0] instead.
                float rf = (r) / 255.0f;
                float gf = (g) / 255.0f;
                float bf = (b) / 255.0f;

                input.putFloat(rf);
                input.putFloat(gf);
                input.putFloat(bf);
            }
        }
    }

    public void runningInterpreter(){
        Toast.makeText(this, "Detecting..", Toast.LENGTH_SHORT).show();
        gettingImageInput();
        int x;
//        if(res==1) x=1;
//        else x=2;
        int bufferSize = 2 * java.lang.Float.SIZE / java.lang.Byte.SIZE;
        modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder());
        interpreter.run(input, modelOutput);

        predictOutput();
    }

    public void predictOutput(){
        modelOutput.rewind();

        try {
            FloatBuffer probabilities = modelOutput.asFloatBuffer();
            String file;
            switch (res){
                case 1:file="pneu"; break;
                case 2:file="malaria"; break;
                case 3:file="bc"; break;
                case 4:file="sc"; break;
                default:file="covid"; break;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(file+".txt")));
            String output="[";
            String label="[";
            for (int i = 0; i < probabilities.capacity(); i++) {
                label+= " "+ reader.readLine();
                //float probability = probabilities.get(i);
                output+= " "+ String.format("%1.4f", probabilities.get(i)*100);
                //Log.i("Ans=", String.format("%s: %1.4f", label, probability));
               // Toast.makeText(this, String.format("%s: %1.4f", label, probability), Toast.LENGTH_SHORT).show();
            }
//            if(res==1) {
//                output+=" "+(1-probabilities.get(0));
//                label+=" "+"Pneumonia";
//            }
            Toast.makeText(this, label+" ] = "+output+" ]", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            // File not found?
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap getYourInputImage(){
        Bitmap bm = ((BitmapDrawable)iv.getDrawable()).getBitmap();
        //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.u1);
        return bm;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            mCurrentPhotoUri = uri.toString();
            if(iv==null) Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
            else iv.setImageURI(Uri.parse(mCurrentPhotoUri));
             iv.setScaleType(ImageView.ScaleType.FIT_XY);

        }
    }

    public void onImageFromGalleryClick(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
        }
    }
    }