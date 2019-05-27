package ittepic.com.mx.tpdm_u5_practica2;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main3Activity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    TextView nombre1, listo1, nombre2, listo2, ganador, perdedor, iniciar;
    ImageView objeto1, objeto2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        nombre1 = findViewById(R.id.nombre1);
        listo1 = findViewById(R.id.listo1);

        nombre2 = findViewById(R.id.nombre2);
        listo2 = findViewById(R.id.listo2);

        ganador = findViewById(R.id.ganador);
        perdedor = findViewById(R.id.perdedor);

        iniciar = findViewById(R.id.iniciar);

        objeto1 = findViewById(R.id.objeto1);
        objeto2 = findViewById(R.id.objeto2);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        consultarJuegoEnCurso();
        super.onStart();
    }

    private void consultarJuegoEnCurso() {
        ganador.setVisibility(View.INVISIBLE);
        perdedor.setVisibility(View.INVISIBLE);

        mDatabase.child("lobby").child("juego1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Game game = dataSnapshot.getValue(Game.class);

                System.out.println("ENTRO1");

                if (game.ready2.equals("")) {
                    nombre2.setVisibility(View.INVISIBLE);
                    iniciar.setVisibility(View.INVISIBLE);
                    listo2.setText("Esperando ...");
                    listo2.setTextColor(Color.rgb(0, 0, 0));
                } else {
                    listo2.setText("¡Listo!");
                    nombre2.setVisibility(View.VISIBLE);
                    nombre2.setText("" + game.sobre2);
                    listo2.setTextColor(Color.rgb(76, 175, 80));
                    iniciar.setVisibility(View.VISIBLE);
                }
                nombre1.setText("" + game.sobre1);
                if (game.finish.equals("")) {
                    // finish();
                }// if


            }// onDataChange

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }// consultarTodos

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
