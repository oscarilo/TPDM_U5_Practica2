package ittepic.com.mx.tpdm_u5_practica2;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
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
    String[] nom = {"", "piedra", "papel", "tijera"};
    TextView nombre1, listo1, nombre2, listo2, ganador, perdedor, iniciar;
    ImageView objeto1, objeto2;

    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;
    int contador;


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

        contador = 0;
        // Sensor manager es quien se encarga de saber que sensor vamos a administrar.
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Validar si el dispositivo cuenta con el sensor
        if (sensor == null) {
            finish();
        }// if

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];

                if (iniciar.getVisibility() == View.VISIBLE) {
                    if (x < -5 && contador == 0) {
                        contador++;
                        // Colorear el fondo de color Gris
                        //getWindow().getDecorView().setBackgroundColor(Color.GRAY);
                    } else if (x > 5 && contador == 1) {
                        contador++;
                        // Colorear el fondo de color Rojo
                        //getWindow().getDecorView().setBackgroundColor(Color.RED);
                    }

                    if (contador == 2) {
                        // Reproducir un sonido
                        //sonido();
                        int num = (int) ((Math.random() * 3) + 1);
                        System.out.println("RANDOM: " + num);

                        establecer_img(num);
                        contador = 0;

                    }
                }else{
                    int id = getResources().getIdentifier("blanco", "drawable", getPackageName());
                    objeto1.setImageResource(id);
                    System.out.println("INVISIBLE");
                }


            }// onSensorChanged

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

    }// onCreate

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


    public void sonido() {
        // Acceder al recurso de tipo sonido

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.winner);
        mediaPlayer.start();

    }// sonido


    public void iniciar() {
        sensorManager.registerListener(sensorEventListener, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }// iniciar


    public void detener() {
        sensorManager.unregisterListener(sensorEventListener);
    }// detener


    @Override
    protected void onPause() {
        super.onPause();
        detener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iniciar();
    }

    public void establecer_img(int numero) {
        int id = getResources().getIdentifier(nom[numero], "drawable", getPackageName());
        objeto1.setImageResource(id);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
