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
    private MaterialButton btnClose;
    private static final int CAMERA_PERMISSION_CODE = 100;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.barcode_scan_activity);
        MaterialButton btnEnterBarcodeMannually = findViewById(R.id.btnEnterBarcodeMannually);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
    }
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(ScanActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(ScanActivity.this,
                    new String[]{permission},
                    requestCode);
        } else {
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(me.dm7.barcodescanner.zbar.Result result) {
        Intent intent = new Intent(ScanActivity.this, ScanPreview.class);
        intent.putExtra(BARCODE, result.getContents());
        startActivity(intent);
    }
}