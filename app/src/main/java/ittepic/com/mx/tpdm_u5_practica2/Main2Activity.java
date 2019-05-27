package ittepic.com.mx.tpdm_u5_practica2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Main2Activity extends AppCompatActivity {
    EditText celularReto, nombre2;
    Button enviarReto, entrar;
    String bandera;

    private static final int MY_PERMISSIONS_REQUEST_RECIEVER_SMS = 0;
    private static final int PERMISSION_REQUEST_CODE = 1;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        celularReto = findViewById(R.id.celularReto);
        enviarReto = findViewById(R.id.enviarReto);

        nombre2 = findViewById(R.id.nombre2);
        entrar = findViewById(R.id.join);

        Intent intent = getIntent();

        bandera = intent.getStringExtra("bandera");

        validar(bandera);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        permisos();
        //consultarJuegoEnCurso();
        enviarReto.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                if (!celularReto.getText().toString().isEmpty()) {
                    metodoInsertarDatos("Jugador1", celularReto.getText().toString());
                    enviarMensaje(celularReto.getText().toString());
                    Intent ventana = new Intent(Main2Activity.this, Main3Activity.class);
                    startActivity(ventana);
                    finishAffinity();
                }

            }
        });

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                metodoInsertarDatosP2(nombre2.getText().toString());

                Intent ventana = new Intent(Main2Activity.this, Main3Activity.class);
                startActivity(ventana);
                finish();
            }
        });

    }// onCreate

    private void validar(String bandera) {
        System.out.println("BANDERA: " + bandera);

        if (bandera.equals("0")) {
            enviarReto.setVisibility(View.VISIBLE);
            celularReto.setVisibility(View.VISIBLE);
        } else {
            entrar.setVisibility(View.VISIBLE);
            nombre2.setVisibility(View.VISIBLE);
        }
    }


    private void consultarJuegoEnCurso() {

        mDatabase.child("lobby").child("juego1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Game game = dataSnapshot.getValue(Game.class);

                System.out.println("ENTRO1");

                if (game.ready1.equals("")) {

                    enviarReto.setVisibility(View.VISIBLE);
                    celularReto.setVisibility(View.VISIBLE);

                }


            }// onDataChange

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }// consultarTodos

    private void metodoInsertarDatos(String usuario, String telefono) {
        // Game juego = new Game("", usuario, "", telefono, "", "", "", "1", "NO", "NO");
        System.out.println("TELEFONO " + telefono + " " + usuario);
        mDatabase.child("lobby").child("juego1").child("num2").setValue(telefono);
        mDatabase.child("lobby").child("juego1").child("ready1").setValue("1");
        finish();
    }// metodoInsertarDatos

    private void metodoInsertarDatosP2(String usuario) {
        // Game juego = new Game("", usuario, "", telefono, "", "", "", "1", "NO", "NO");
//        System.out.println("TELEFONO " + telefono + " " + usuario);
        mDatabase.child("lobby").child("juego1").child("sobre2").setValue(usuario);
        mDatabase.child("lobby").child("juego1").child("ready2").setValue("1");
        finish();
    }// metodoInsertarDatos

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
