package com.example.mlkittest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    Button captureImage,DetectText;
    ImageView imageView1;
    TextView text1;
    static  final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initialization of Views
        captureImage = (Button)findViewById(R.id.captureImage);
        imageView1 = (ImageView)findViewById(R.id.imageview1);
        DetectText = (Button)findViewById(R.id.detectText);
        text1 = (TextView)findViewById(R.id.text1);

        //on button click open camera to capture image
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureIntent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        //on click read the text from captured image
        DetectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetectTextFromImage();
            }
        });
    }

    private void DetectTextFromImage() {
        //read bitmap image
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        //text detector from selected image
        FirebaseVisionTextRecognizer textDetector = FirebaseVision.getInstance().getCloudTextRecognizer();

        textDetector.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {

                List<FirebaseVisionText.TextBlock> blocks = firebaseVisionText.getTextBlocks();
                if(blocks.size()==0){
                    Toast.makeText(TestActivity.this,"No Text Found no images",Toast.LENGTH_LONG).show();
                }
                else{
                    for(FirebaseVisionText.Block block : firebaseVisionText.getTextBlocks())
                    {
                        String text = block.getText();
                        //set text to textview
                        // that is been read from imagebitmap
                        text1.setText(text);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TestActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle extras  = data.getExtras();
        imageBitmap = (Bitmap) extras.get("data");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        // camera captured image set to ImageView
        imageView1.setImageBitmap(imageBitmap);
    }
}