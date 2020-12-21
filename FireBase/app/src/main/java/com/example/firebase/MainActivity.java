package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.custom.FirebaseCustomLocalModel;
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelInterpreterOptions;
import com.google.firebase.ml.custom.FirebaseModelOutputs;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class MainActivity extends AppCompatActivity {

//    FirebaseCustomLocalModel localModel;
//    FirebaseCustomRemoteModel remoteModel;
//    Button b1,b2;
//    ImageView iv1,iv2;
//    Interpreter interpreter;
//    ByteBuffer input;
//    ByteBuffer modelOutput;
    public void pneu(View v){
        Intent i=new Intent(this,MainActivity2.class);
        i.putExtra("disease",1);
        startActivity(i);
    }
    public void malaria(View v){
        Intent i=new Intent(this,MainActivity2.class);
        i.putExtra("disease",2);
        startActivity(i);
    }
    public void bc(View v){
        Intent i=new Intent(this,MainActivity2.class);
        i.putExtra("disease",3);
        startActivity(i);
    }
    public void sc(View v){
        Intent i=new Intent(this,MainActivity2.class);
        i.putExtra("disease",4);
        startActivity(i);
    }

    public void covid(View v){
        Intent i=new Intent(this,MainActivity2.class);
        i.putExtra("disease",5);
        startActivity(i);
    }

//    public void loadModel(){
//        remoteModel = new FirebaseCustomRemoteModel.Builder("custombinary").build();
//        FirebaseModelManager.getInstance().getLatestModelFile(remoteModel)
//                .addOnCompleteListener(new OnCompleteListener<File>() {
//                    @Override
//                    public void onComplete(@NonNull Task<File> task) {
//                        File modelFile = task.getResult();
//                        if (modelFile != null) {
//                            interpreter = new Interpreter(modelFile);
//                            Toast.makeText(MainActivity.this, "Hosted Model loaded.. Running interpreter..", Toast.LENGTH_SHORT).show();
//                            runningInterpreter();
//                        }
//                        else{
//                            try {
//                                InputStream inputStream = getAssets().open("pneumonia.tflite");
//                                byte[] model = new byte[inputStream.available()];
//                                inputStream.read(model);
//                                ByteBuffer buffer = ByteBuffer.allocateDirect(model.length)
//                                        .order(ByteOrder.nativeOrder());
//                                buffer.put(model);
//                                interpreter = new Interpreter(buffer);
//                            } catch (IOException e) {
//                                // File not found?
//                                Toast.makeText(MainActivity.this, "No hosted or bundled model", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
//    }
//
//    public void gettingImageInput(){
//        Bitmap bm = getYourInputImage();
//        Bitmap bitmap = Bitmap.createScaledBitmap(bm, 224, 224, true);
//        input = ByteBuffer.allocateDirect(224 * 224 * 3 * 4).order(ByteOrder.nativeOrder());
//        for (int y = 0; y < 224; y++) {
//            for (int x = 0; x < 224; x++) {
//                int px = bitmap.getPixel(x, y);
//
//                // Get channel values from the pixel value.
//                int r = Color.red(px);
//                int g = Color.green(px);
//                int b = Color.blue(px);
//
//                // Normalize channel values to [-1.0, 1.0]. This requirement depends
//                // on the model. For example, some models might require values to be
//                // normalized to the range [0.0, 1.0] instead.
//                float rf = (r - 127) / 255.0f;
//                float gf = (g - 127) / 255.0f;
//                float bf = (b - 127) / 255.0f;
//
//                input.putFloat(rf);
//                input.putFloat(gf);
//                input.putFloat(bf);
//            }
//        }
//    }
////
//    public void runningInterpreter(){
//        gettingImageInput();
//        int bufferSize = 1 * java.lang.Float.SIZE / java.lang.Byte.SIZE;
//        modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder());
//        if(interpreter==null){
//            Toast.makeText(this, "Interpreter is null", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            Toast.makeText(this, "interpreting..", Toast.LENGTH_SHORT).show();
//            interpreter.run(input, modelOutput);
//        }
//        Toast.makeText(this, "Predicting Output..", Toast.LENGTH_SHORT).show();
//        predictOutput();
//    }
//
//    public void predictOutput(){
//        modelOutput.rewind();
//        FloatBuffer probabilities = modelOutput.asFloatBuffer();
//        try {
//            //BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("custom_labels.txt")));
//            for (int i = 0; i < probabilities.capacity(); i++) {
//                //String label = reader.readLine();
//                float probability = probabilities.get(i);
//                //Log.i("Ans=", String.format("%s: %1.4f", label, probability));
//                //Toast.makeText(this, String.valueOf(probability), Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, String.format("%1.4f", probability), Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//            // File not found?
//            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
//        }
//    }
////
//    public Bitmap getYourInputImage(){
////        Bitmap bitmap = ((BitmapDrawable)iv1.getDrawable()).getBitmap();
//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.nor4);
//        return bm;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
         setTitle("Disease Detection");
//        b1=findViewById(R.id.button3);
////        b2=findViewById(R.id.button2);
////        iv1=findViewById(R.id.imageView8);
////        iv2=findViewById(R.id.imageView9);
//
//         remoteModel =
//                new FirebaseCustomRemoteModel.Builder("custombinary").build();
//        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
//                .requireWifi()
//                .build();
//        FirebaseModelManager.getInstance().download(remoteModel, conditions)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void v) {
//                        // Download complete. Depending on your app, you could enable
//                        // the ML feature, or switch from the local model to the remote
//                        // model, etc.
//                        Toast.makeText(MainActivity.this, "Download Complete.. Loading Model..", Toast.LENGTH_SHORT).show();
//                        loadModel();
//                    }
//                });
      }
}