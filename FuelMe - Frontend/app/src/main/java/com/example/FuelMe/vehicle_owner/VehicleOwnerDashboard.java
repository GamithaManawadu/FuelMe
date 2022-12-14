package com.example.FuelMe.vehicle_owner;

import java.util.Map;
import java.util.Objects;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.graphics.Color;
import android.content.Intent;
import android.widget.TextView;
import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.FuelMe.R;
import com.example.FuelMe.constants.VolleyCallback;
import com.example.FuelMe.models.User;
import com.example.FuelMe.models.Queue;
import com.example.FuelMe.constants.Constants;
import com.example.FuelMe.models.FuelStation;
import com.example.FuelMe.vehicle_owner.controllers.vehicleDashboardController;


public class VehicleOwnerDashboard extends AppCompatActivity implements View.OnClickListener {

    private User loggedUser;
    private Button btnJoinQueue;
    private FuelStation fuelStation;
    private TextView txtPetrolStatus;
    private TextView txtDieselStatus;
    private TextView txtTimeDuration;
    private String timeDuration = "0";
    private CardView cardPetrolStatus, cardDieselStatus;
    private TextView txtVanCountDiesel, txtVanCountPetrol;
    private TextView txtCarCountDiesel, txtCarCountPetrol;
    private TextView txtBusCountDiesel, txtBusCountPetrol;
    private TextView txt3WheelCountDiesel, txt3WheelCountPetrol;
    private FuelStation selectedFuelStation = new FuelStation();
    private TextView txtMotorBikeCountDiesel, txtMotorBikeCountPetrol;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_join:
                Intent i = new Intent(VehicleOwnerDashboard.this, JoinQueue.class);
                i.putExtra(Constants.LOGGED_USER, loggedUser);
                i.putExtra(Constants.STATION, fuelStation);
                i.putExtra(Constants.WAITING_TIME, timeDuration);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_owner_dashboard);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        fuelStation = (FuelStation) getIntent().getSerializableExtra(Constants.STATION);
        loggedUser = (User) getIntent().getSerializableExtra(Constants.LOGGED_USER);

        getSupportActionBar().setTitle(fuelStation.getStationName() + " - " + fuelStation.getLocation());

        btnJoinQueue = findViewById(R.id.btn_join);
        btnJoinQueue.setOnClickListener(this);

        getFuelStationById(fuelStation.getId());

        txtPetrolStatus = findViewById(R.id.txt_petrolStatus);
        txtDieselStatus = findViewById(R.id.txt_dieselStatus);

        cardPetrolStatus = findViewById(R.id.card_petrolStatus);
        cardDieselStatus = findViewById(R.id.card_dieselStatus);

        txtMotorBikeCountDiesel = findViewById(R.id.txt_bikeCountDiesel);
        txtMotorBikeCountPetrol = findViewById(R.id.txt_bikeCountPetrol);

        txtVanCountDiesel = findViewById(R.id.txt_vanCountDiesel);
        txtVanCountPetrol = findViewById(R.id.txt_vanCountPetrol);

        txt3WheelCountDiesel = findViewById(R.id.txt_3wheelCountDiesel);
        txt3WheelCountPetrol = findViewById(R.id.txt_3wheelCountPetrol);

        txtBusCountDiesel = findViewById(R.id.txt_bussLorryCountDiesel);
        txtBusCountPetrol = findViewById(R.id.txt_bussLorryCountPetrol);

        txtCarCountDiesel = findViewById(R.id.txt_carCountDiesel);
        txtCarCountPetrol = findViewById(R.id.txt_carCountPetrol);

        txtTimeDuration = findViewById(R.id.txt_timeDuration);


        setFuelStatsInView();


        getQueueTimeDuration(fuelStation.getId(), new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        if (result == "0") {
                            txtTimeDuration.setText("No Queue");
                        } else {
                            txtTimeDuration.setText("Waiting Time" + result);
                        }
                    }

                    @Override
                    public void onError(String result) {
                    }
                }
        );
    }


    private void setFuelStatsInView() {
        if (fuelStation.getTotalDiesel() != 0) {
            txtDieselStatus.setText("On Stock");
            cardDieselStatus.setCardBackgroundColor(Color.parseColor("#ff99cc00"));
        } else {
            txtDieselStatus.setText("Out of Stock");
            cardDieselStatus.setCardBackgroundColor(Color.parseColor("#ffff4444"));
        }

        if (fuelStation.getTotalPetrol() != 0) {
            txtPetrolStatus.setText("On Stock");
            cardPetrolStatus.setCardBackgroundColor(Color.parseColor("#ff99cc00"));
        } else {
            txtPetrolStatus.setText("Out of Stock");
            cardPetrolStatus.setCardBackgroundColor(Color.parseColor("#ffff4444"));
        }
    }


    private void getFuelStationById(String id) {

        JSONObject jsonObject = new JSONObject();
        ArrayList<Queue> joinedQueues = new ArrayList<>();
        String URL = Constants.BASE_URL + "/FuelStation/" + id;
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray queueArray = new JSONArray(response.getString("queue"));

                            for (int i = 0; i < queueArray.length(); i++) {
                                JSONObject singleObject = queueArray.getJSONObject(i);
                                Queue queue = new Queue(
                                        singleObject.getString("id"),
                                        singleObject.getString("vehicleType"),
                                        singleObject.getString("vehicleOwner"),
                                        singleObject.getString("fuelType"),
                                        singleObject.getString("stationsId")
                                );
                                joinedQueues.add(queue);
                                Log.e("api", "onResponse: " + joinedQueues.size());
                            }
                            selectedFuelStation.setId(response.getString("id"));
                            selectedFuelStation.setStationName(response.getString("name"));
                            selectedFuelStation.setLocation(response.getString("location"));
                            selectedFuelStation.setStationOwner(response.getString("stationOwner"));
                            selectedFuelStation.setLastModified(response.getString("lastModified"));
                            selectedFuelStation.setDieselStatus(response.getBoolean("dieselStatus"));
                            selectedFuelStation.setPetrolStatus(response.getBoolean("petrolStatus"));
                            selectedFuelStation.setQueues(joinedQueues);


                            Map<String, Integer> vehicleCounts = vehicleDashboardController.getVehicleCounts(joinedQueues);

                            setQueueCountsInTexts(vehicleCounts);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(jsonObjReq);
    }


    private void setQueueCountsInTexts(Map<String, Integer> vehicleCounts) {

        txtMotorBikeCountDiesel.setText(String.valueOf(vehicleCounts.get(Constants.MOTOR_BIKE + Constants.DIESEL)));
        txtMotorBikeCountPetrol.setText(String.valueOf(vehicleCounts.get(Constants.MOTOR_BIKE + Constants.PETROL)));

        txtVanCountDiesel.setText(String.valueOf(vehicleCounts.get(Constants.VAN + Constants.DIESEL)));
        txtVanCountPetrol.setText(String.valueOf(vehicleCounts.get(Constants.VAN + Constants.PETROL)));

        txt3WheelCountDiesel.setText(String.valueOf(vehicleCounts.get(Constants.THREE_WHEEL + Constants.DIESEL)));
        txt3WheelCountPetrol.setText(String.valueOf(vehicleCounts.get(Constants.THREE_WHEEL + Constants.PETROL)));

        txtCarCountDiesel.setText(String.valueOf(vehicleCounts.get(Constants.CAR + Constants.DIESEL)));
        txtCarCountPetrol.setText(String.valueOf(vehicleCounts.get(Constants.CAR + Constants.PETROL)));

        txtBusCountDiesel.setText(String.valueOf(vehicleCounts.get(Constants.BUS + Constants.DIESEL)));
        txtBusCountPetrol.setText(String.valueOf(vehicleCounts.get(Constants.BUS + Constants.PETROL)));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();


        if (id == android.R.id.home) {
            Intent intent = new Intent(VehicleOwnerDashboard.this, SelectionStation.class);
            startActivity(intent);
        }
        return true;
    }


    public void getQueueTimeDuration(String stationId, final VolleyCallback callback) {


        RequestQueue queue = Volley.newRequestQueue(this);
        String api = Constants.BASE_URL + "/Queue/GetQueueTime?stationId=" + stationId;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.length() == 0) {
                            callback.onSuccess("0");
                            timeDuration = "0";
                        } else {
                            callback.onSuccess(response);
                            timeDuration = response;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("That didn't work! +" + error.getLocalizedMessage());
                    }
                });

        queue.add(stringRequest);
    }

}