package com.example.FuelMe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MapNavigate extends AppCompatActivity {

    EditText Source, Destination;
    Button display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_navigate);

        Source = findViewById(R.id.source);
        Destination = findViewById(R.id.location);
        display = findViewById(R.id.btnDisplay);

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sSource = Source.getText().toString().trim();
                String sDestination = Destination.getText().toString().trim();

                if (sSource.isEmpty()){
                    Source.setError("Enter your location");

                }else if(sDestination.isEmpty()) {
                    Destination.setError("Enter your destination");

                }else if(sSource.equals(sDestination)){
                    Toast.makeText(getApplicationContext()
                            , "Enter different locations", Toast.LENGTH_LONG).show();

                }else{
                    DisplayTrack(sSource, sDestination);
                }
            }
        });
    }

    private void DisplayTrack(String sSource, String sDestination){

        try{
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + sSource + "/"
                    + sDestination);

            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            Toast.makeText(getApplicationContext()
                    , "Opening google maps", Toast.LENGTH_LONG).show();
            clearControls();

        }catch (ActivityNotFoundException e){
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void clearControls(){
        Source.setText("");
        Destination.setText("");
    }
}