package ittepic.com.mx.tpdm_u5_practica2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button entrar;
    private EditText nombre, numeroTelefono;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        entrar = findViewById(R.id.entrar);

        nombre = findViewById(R.id.nombre);
        numeroTelefono = findViewById(R.id.numeroTelefono);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validar(nombre.getText().toString(), numeroTelefono.getText().toString())) {
                    finish();

                    metodoInsertarDatos(nombre.getText().toString(), numeroTelefono.getText().toString());
                    Intent ventana = new Intent(MainActivity.this, Main2Activity.class);
                    ventana.putExtra("bandera", "0");
                    startActivity(ventana);
                }

            }
        });

        consultarJuegoEnCurso();
    }// onCreate


    private void metodoInsertarDatos(String usuario, String telefono) {
        Game juego = new Game(usuario, "", telefono, "", "", "", "", "", "NO", "NO");

        mDatabase.child("lobby").child("juego1").setValue(juego).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("INSERTADO!");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("ERROR!");
            }
        });

    }// metodoInsertarDatos


    private boolean validar(String nombre, String telefono) {
        System.out.println("CLICK " + nombre + " " + telefono);

        if (!nombre.isEmpty() || !telefono.isEmpty()) {
            return true;
        }
        return false;
    }// validar


    private void consultarJuegoEnCurso() {

        mDatabase.child("lobby").child("juego1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Game game = dataSnapshot.getValue(Game.class);
                if (!game.finish.isEmpty()) {
                    if (game.sobre2.isEmpty() && game.ready2.isEmpty()) {
                        Intent ventana = new Intent(MainActivity.this, Main2Activity.class);
                        ventana.putExtra("bandera", "1");
                        startActivity(ventana);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }// consultarTodos

}// class
