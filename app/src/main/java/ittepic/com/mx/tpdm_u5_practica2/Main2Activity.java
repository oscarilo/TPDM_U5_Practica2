package ittepic.com.mx.tpdm_u5_practica2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    EditText celularReto;
    Button enviarReto;
    private static final int MY_PERMISSIONS_REQUEST_RECIEVER_SMS = 0;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        celularReto = findViewById(R.id.celularReto);
        enviarReto = findViewById(R.id.enviarReto);

        permisos();

        enviarReto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!celularReto.getText().toString().isEmpty()) {

                    enviarMensaje(celularReto.getText().toString());
                }

            }
        });

    }// onCreate


    private void enviarMensaje(String celular) {

        SmsManager sms = SmsManager.getDefault();

        ArrayList<String> parts = sms.divideMessage("Te reto a un juego de piedra, papel o tijera!\nEntra al lobby para jugar.");
        sms.sendMultipartTextMessage(celular, null, parts, null, null);
        System.out.println("NUMERO " + celular);

        Toast.makeText(this, "Rival retado!", Toast.LENGTH_LONG).show();
        celularReto.setText("");

    }// enviarMensaje



    private void permisos() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST_RECIEVER_SMS);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_RECIEVER_SMS);
            }
        }// if 1
    }

}// class
