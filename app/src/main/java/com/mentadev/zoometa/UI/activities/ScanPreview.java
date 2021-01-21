package com.mentadev.zoometa.UI.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.mentadev.zoometa.R;
import com.mentadev.zoometa.datamodel.DeliveryNoteScanRequest;
import com.mentadev.zoometa.exceptionhandling.ExceptionHandling;
import com.mentadev.zoometa.restconnector.FinderREST;
import com.mentadev.zoometa.restconnector.MyProfileInterface;

public class ScanPreview extends AppCompatActivity {
    public static final String BARCODE = "BARCODE";
    MaterialButton btnOpenCamera;
    MaterialButton btnSubmitScanned;
    MaterialTextView textinput_error;
    public static TextInputEditText txtResultBody;
    public static ProgressBar simpleProgressBar;

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ScanPreview.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_preview_activity);
        initViews();
        btnOpenCamera.setOnClickListener(view -> {
            Intent intent = new Intent(ScanPreview.this, ScanActivity.class);
            startActivity(intent);
        });
        if (getIntent().getExtras() != null) {
            txtResultBody.setText(getIntent().getExtras().getString(BARCODE));
            getIntent().removeExtra(BARCODE);
            btnSubmitScanned.setEnabled(true);
        } else {
            txtResultBody.setText("");
            btnSubmitScanned.setEnabled(false);
        }
        btnSubmitScanned.setVisibility(View.VISIBLE);
        btnSubmitScanned.setOnClickListener(view -> submitScanned());
        txtResultBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnSubmitScanned.setEnabled(charSequence.length() != 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        submitScanned();
    }

    private void submitScanned() {
        simpleProgressBar.setVisibility(View.VISIBLE);
        if (txtResultBody.getText().toString().isEmpty()) {
            textinput_error.setVisibility(View.VISIBLE);
            textinput_error.setText(getResources().getString(R.string.errorText_noBarcodeScanned));
            simpleProgressBar.setVisibility(View.GONE);
            return;
        }
        FinderREST controller = new FinderREST(getApplicationContext(), LandingActivity.MyProfile.getToken());
        DeliveryNoteScanRequest deliveryNoteScanRequest = new DeliveryNoteScanRequest();
        deliveryNoteScanRequest.setCode(String.valueOf(txtResultBody.getText()));
        controller.sendScannedValue(deliveryNoteScanRequest, new MyProfileInterface.SendScannedValue() {
            @Override
            public void SendScannedValue(String scanningResponseMessage) {
                textinput_error.setVisibility(View.VISIBLE);
                textinput_error.setText(scanningResponseMessage);
                simpleProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void OnError(Throwable exception) {
                ExceptionHandling.HandleUIDataEntryValidation(getApplicationContext(),
                        new Exception(getApplicationContext().getResources().getString(R.string.message_error_submit_scanning) + ".\n " + exception.getMessage()),
                        textinput_error);
                simpleProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void OnUnauthorized() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }


            @Override
            public void OnResourceNotFound() {
                OnError(new Throwable(getApplicationContext().getResources().getString(R.string.message_error_resource_not_found)));
            }

            @Override
            public void OnInternalServerError() {
                OnError(new Throwable(getApplicationContext().getResources().getString(R.string.message_error_internal_server_error)));

            }

            @Override
            public void OnGenericError() {
                OnError(new Throwable(getApplicationContext().getResources().getString(R.string.message_error_unknown_error)));
            }
        });
    }

    private void initViews() {
        simpleProgressBar = findViewById(R.id.simpleProgressBar);
        txtResultBody = findViewById(R.id.txtResultsBody);
        btnOpenCamera = findViewById(R.id.btnOpenCamera);
        btnSubmitScanned = findViewById(R.id.btnSubmitScanned);
        textinput_error = findViewById(R.id.textinput_error);

        txtResultBody.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                submitScanned();
                return true;
            }
            return true;

        });
    }
}
