package com.csi4999.snapnstore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.csi4999.snapnstore.Helper.GraphicOverlay;
import com.csi4999.snapnstore.Helper.RectOverlay;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

import dmax.dialog.SpotsDialog;

import  com.csi4999.snapnstore.Helper.MLKitVisionImage;

public class BarcodeActivity extends AppCompatActivity {

    CameraView cameraView;
    AlertDialog waitingDialog;
    GraphicOverlay graphicOverlay;
    Button btnCapture, btnPass;

    String str2;

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
        graphicOverlay = (GraphicOverlay) findViewById(R.id.grahpic_overlay2);
        btnCapture = (Button) findViewById(R.id.btn_capture2);
        btnPass = (Button) findViewById(R.id.btn_pass2);

        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
                // get the value which input by user in EditText
                // and convert it to string

                // Create the Intent object of this class Context() to Second_activity class
               Intent intent = new Intent(); //(getApplicationContext(), VerifyDataActivity.class);

                // now by putExtra method put the value in key, value pair
                // key is message_key by this key we will receive the value, and put the string

                intent.putExtra("message_key2", str2);
                setResult(RESULT_OK, intent);
                finish();
                // start the Intent
                //finish();
                //startActivity(intent);
            }
        });

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.start();
                cameraView.captureImage();
                graphicOverlay.clear();
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



    private void recognizeBarcode(Bitmap bitmap) {

        //InputImage image = InputImage.fromBitmap(bitmap, rotationDegree); - default input for scanner process
        //FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap); - deprecated code, keep just in case
        int rotationDegree = 0;
        // [START image_from_bitmap]
        InputImage image = InputImage.fromBitmap(bitmap, rotationDegree);
        BarcodeScannerOptions options =
                new BarcodeScannerOptions .Builder()
                        .setBarcodeFormats(Barcode.FORMAT_UPC_A)
                        .setBarcodeFormats(Barcode.FORMAT_UPC_E)
                        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                        .build();
        BarcodeScanner scanner = BarcodeScanning.getClient(options);

        Task<List<Barcode>> result = scanner.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        processResult(barcodes);
                        // Task completed successfully
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                    }
                });
    }

    public void processResult(List<Barcode> barcodes) {
        for (Barcode barcode: barcodes) {

            //Identify in image, draw graphic

            Rect rectBounds = barcode.getBoundingBox();
            RectOverlay rectOverlay = new RectOverlay(graphicOverlay, rectBounds);
            graphicOverlay.add(rectOverlay);

            Rect bounds = barcode.getBoundingBox();
            Point[] corners = barcode.getCornerPoints();

            String rawValue = barcode.getRawValue();

            //int valueType = barcode.getValueType();

            int scannedFormat = barcode.getFormat();

            switch (scannedFormat) {
                case Barcode.FORMAT_UPC_A: {
                        String str2 = barcode.getRawValue();
                        int type = barcode.getFormat();
                        //break;

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(barcode.getRawValue());
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {

                        }
                    });
                    //String str2 = rawValue;
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
//Note URL may be removed, there may not be a need for this, each supported Barcode type in our app should have its own case
                case Barcode.TYPE_URL: {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(barcode.getRawValue()));
                    startActivity(intent);
                }
                break;
                case Barcode.TYPE_CONTACT_INFO: {
                    String message = "QR Contact info detected, not a Product Code!";
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(message);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                default:

                    break;

            }
        }
        Toast toast = Toast.makeText(getApplicationContext(), str2 , Toast.LENGTH_SHORT);
        toast.show();
            waitingDialog.dismiss();

        }
    }
