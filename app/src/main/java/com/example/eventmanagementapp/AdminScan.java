package com.example.eventmanagementapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventmanagementapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class AdminScan extends AppCompatActivity {

    TextView txtView1;
    Button qrScanBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_scan);

        txtView1 = findViewById(R.id.txtView1);

        qrScanBtn = findViewById(R.id.scan_qr);
        qrScanBtn.setOnClickListener(v -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(AdminScan.this);
            intentIntegrator.setPrompt("Scan Booking QR");
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.initiateScan();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult!=null){
            if(intentResult.getContents()!=null){

                txtView1.setText(intentResult.getContents());
                String bookingID = intentResult.getContents();
                updateScannedStatus(bookingID);



            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateScannedStatus(String bookingID){
        DocumentReference bookingref = db.collection("bookings").document(bookingID);
        bookingref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String scannedStatus = documentSnapshot.getString("scannedStatus");

                    if("true".equals(scannedStatus)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminScan.this);
                        builder.setTitle("Already Scanned");
                        builder.setMessage("This booking has already been scanned.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();
                    } else {
                        bookingref.update("scannedStatus","true");
                        AlertDialog.Builder dialogBox = new AlertDialog.Builder(AdminScan.this);
                        dialogBox.setMessage("Ticket Verified ✅");
                        dialogBox.setTitle("EventSpot");
                        dialogBox.setCancelable(false);

                        dialogBox.setPositiveButton("Done", (DialogInterface.OnClickListener) (dialog, which) -> {
                            finish();
                        });

                        AlertDialog alertDialog = dialogBox.create();
                        alertDialog.show();
                    }

                }
            }
        });

    }
}