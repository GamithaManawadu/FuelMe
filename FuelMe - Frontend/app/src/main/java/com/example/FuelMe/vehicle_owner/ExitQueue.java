package com.example.FuelMe.vehicle_owner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.FuelMe.R;
import com.example.FuelMe.constants.Constants;
import com.example.FuelMe.models.FuelStation;
import com.example.FuelMe.models.Queue;
import com.example.FuelMe.models.User;


public class ExitQueue extends AppCompatActivity implements View.OnClickListener {
    private Button btnExitAfter, btnExitBefore;
    private FuelStation fuelStation;
    private TextView txtJoinedTime, txtFuelType, txtVehicleType;
    private String joinedTime, fuelType, vehicleType;
    private Queue joinedQueue;
    private User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_queue);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Exit Queue Process");

        joinedTime = getIntent().getStringExtra(Constants.JOINED_TIME);
        fuelStation = (FuelStation) getIntent().getSerializableExtra(Constants.STATION);
        joinedQueue = (Queue) getIntent().getSerializableExtra(Constants.JOINED_QUEUE);
        loggedUser = (User) getIntent().getSerializableExtra(Constants.LOGGED_USER);
        fuelType = joinedQueue.fuelType;
        vehicleType = joinedQueue.vehicleType;
        txtFuelType = findViewById(R.id.txt_fueltype);
        txtVehicleType = findViewById(R.id.txt_vehicletypeExit);

        txtFuelType.setText("Fuel Type: " + fuelType);
        txtVehicleType.setText("Vehicle Type: " + vehicleType);

        btnExitAfter = findViewById(R.id.btn_exitAfter);
        btnExitAfter.setOnClickListener(this);

        btnExitBefore = findViewById(R.id.btn_exitBefore);
        btnExitBefore.setOnClickListener(this);

        txtJoinedTime = findViewById(R.id.txt_joinedTime);
        txtJoinedTime.setText("joined  at " + joinedTime);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            Intent intent = new Intent(ExitQueue.this, JoinQueue.class);
            intent.putExtra(Constants.STATION, fuelStation);
            intent.putExtra(Constants.LOGGED_USER, loggedUser);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_exitAfter:
                exitQueueApi(true);
                Toast.makeText(this, " Exited from the Queue After the pump fuel", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_exitBefore:
                exitQueueApi(false);
                Toast.makeText(this, "Exited from the Queue Before the pump fuel", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        Intent i = new Intent(ExitQueue.this, VehicleOwnerDashboard.class);
        i.putExtra(Constants.STATION, fuelStation);
        i.putExtra(Constants.LOGGED_USER, loggedUser);
        startActivity(i);
    }


    private void exitQueueApi(Boolean aquired) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.BASE_URL+"/Queue?fuelType="+joinedQueue.getFuelType()+"&stationId="+joinedQueue.stationsId+"&queueId="+joinedQueue.getId()+"&aquired="+aquired;

        // Request a string response from the provided URL.
        StringRequest stringRequest =  new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response Status Code: "+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //textView.setText("That didn't work!");
                        System.out.println("That didn't work!");
                        System.out.println("That didn't work! +" + error.getLocalizedMessage());
                    }
                });


        queue.add(stringRequest);
    }

}