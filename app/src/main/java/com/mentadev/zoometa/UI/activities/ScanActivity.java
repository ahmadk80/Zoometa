package com.mentadev.zoometa.UI.activities;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.mentadev.zoometa.R;

import me.dm7.barcodescanner.zbar.ZBarScannerView;

import static com.mentadev.zoometa.UI.activities.ScanPreview.BARCODE;

public class ScanActivity  extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    private MaterialButton btnEnterBarcodeMannually;
    private MaterialButton btnClose;
    private static final int CAMERA_PERMISSION_CODE = 100;

    //camera permission is needed.
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.barcode_scan_activity);
//
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//        getWindow().setLayout((int) (width * .9), (int) (height * .7));


        btnEnterBarcodeMannually = findViewById(R.id.btnEnterBarcodeMannually);
        btnClose = findViewById(R.id.btnClose);

        btnClose.setOnClickListener(view -> {
            Intent intent = new Intent(ScanActivity.this, MainActivity.class);
            getIntent().removeExtra(BARCODE);
            startActivity(intent);
        });
        btnEnterBarcodeMannually.setOnClickListener(view -> {
            Intent intent = new Intent(ScanActivity.this, ScanPreview.class);
            intent.putExtra(BARCODE, "");
            startActivity(intent);
        });
        mScannerView = findViewById(R.id.cameraScanner);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();


//        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
//        setContentView(mScannerView);                // Set the scanner view as the content view

    }

    @Override
    public void onResume() {
        super.onResume();
        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
        // Start camera on resume
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(ScanActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(ScanActivity.this,
                    new String[]{permission},
                    requestCode);
        } else {
//            Toast.makeText(ScanActivity.this,
//                    "Permission already granted",
//                    Toast.LENGTH_SHORT)
//                    .show();

            mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
            mScannerView.startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(ScanActivity.this,
//                        "Camera Permission Granted",
//                        Toast.LENGTH_SHORT)
//                        .show();
            } else {
                Toast.makeText(ScanActivity.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
                onBackPressed();
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(me.dm7.barcodescanner.zbar.Result result) {

        Intent intent = new Intent(ScanActivity.this, ScanPreview.class);
        intent.putExtra(BARCODE, result.getContents());
        startActivity(intent);
//        // Do something with the result here
//        Log.v("kkkk", result.getContents()); // Prints scan results
//        Log.v("uuuu", result.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)
//        ScanPreview.txtResultBody.setText(result.getContents());
//        onBackPressed();
//        // If you would like to resume scanning, call this method below:
//        //mScannerView.resumeCameraPreview(this);
    }
}