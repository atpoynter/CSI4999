package com.csi4999.snapnstore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.csi4999.snapnstore.Helper.GraphicOverlay;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

import dmax.dialog.SpotsDialog;

public class BarcodeActivity extends AppCompatActivity {

        CameraView cameraView;
        AlertDialog waitingDialog;
        GraphicOverlay graphicOverlay;
        Button btnCapture,btnPass;
        String str;

        @Override
        protected void onResume() {
            super.onResume();
            cameraView.start();
        }

        @Override
        protected void onPause() {
            super.onPause();
            cameraView.stop();
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_barcode);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            waitingDialog = new SpotsDialog.Builder()
                    .setCancelable(false)
                    .setMessage("Please wait")
                    .setContext(this)
                    .build();

            cameraView = (CameraView) findViewById(R.id.camera_view2);
            graphicOverlay = (GraphicOverlay) findViewById(R.id.graphic_overlay);
            btnCapture = (Button) findViewById(R.id.btn_capture2);
            btnPass = (Button) findViewById(R.id.btn_pass2);

            btnCapture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cameraView.start();
                    cameraView.captureImage();
                    graphicOverlay.clear();
                }
            });

            btnCapture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {

                    // get the value which input by user in EditText
                    // and convert it to string

                    // Create the Intent object of this class Context() to Second_activity class
                    Intent intent = new Intent(getApplicationContext(), VerifyDataActivity.class);

                    // now by putExtra method put the value in key, value pair
                    // key is message_key by this key we will receive the value, and put the string

                    intent.putExtra("message_key", str);

                    // start the Intent
                    startActivity(intent);
                }
            });
            //Event Camera View
            cameraView.addCameraKitListener(new CameraKitEventListener() {
                @Override
                public void onEvent(CameraKitEvent cameraKitEvent) {

                }

                @Override
                public void onError(CameraKitError cameraKitError) {

                }

                @Override
                public void onImage(CameraKitImage cameraKitImage) {
                    waitingDialog.show();

                    Bitmap bitmap = cameraKitImage.getBitmap();
                    bitmap = Bitmap.createScaledBitmap(bitmap, cameraView.getWidth(), cameraView.getHeight(), false);
                    cameraView.stop();

                    recognizeBarcode(bitmap);

                }

                @Override
                public void onVideo(CameraKitVideo cameraKitVideo) {

                }
            });
        }
    }

    private void recognizeBarcode(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionBarcodeDetectorOptions options =
                new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS)
                .build();
        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);

        detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
                        processResult(firebaseVisionBarcodes);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BarcodeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void processResult(List<FirebaseVisionBarcode> firebaseVisionBarcodes)
    {for(FirebaseVisionBarcode item : firebaseVisionBarcodes)
    {
        int value_type = item.getValueType();
        switch (value_type)
        {
            case FirebaseVisionBarcode.TYPE_TEXT:
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(item.getRawValue())
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            break;
//Note URL may be removed, there may not be a need for this, each supported Barcode type in our app should have its own case
            case FirebaseVisionBarcode.TYPE_URL:
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getRawValue()));
                startActivity(intent);

            }
            break;
            default:

                break;

    }

}
}