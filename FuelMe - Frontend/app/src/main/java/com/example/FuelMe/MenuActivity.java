package com.example.FuelMe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.FuelMe.constants.Constants;
import com.example.FuelMe.models.User;
import com.example.FuelMe.vehicle_owner.SelectionStation;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView UserCard, BookCard, HisCard, MapCard;
    private User user;

    TextView Email;
    Button btnLogout,account;
   // SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        UserCard = (CardView) findViewById(R.id.usercardid);
        BookCard = (CardView) findViewById(R.id.bookingcardid);
        HisCard = (CardView) findViewById(R.id.addparkingcardview);
        MapCard = (CardView) findViewById(R.id.mapcardid);

        UserCard.setOnClickListener(this);
        BookCard.setOnClickListener(this);
        HisCard.setOnClickListener(this);
        MapCard.setOnClickListener(this);


        btnLogout = findViewById(R.id.btnLogout);
        //Email = findViewById(R.id.tvEmailMain);
        //sessionManager = new SessionManager(getApplicationContext());

       // String EmailAddress = sessionManager.getEmail();
       // Email.setText(EmailAddress);


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Log out");
                builder.setMessage("Are you sure to log out?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       // sessionManager.setLogin(false);
                      //  sessionManager.setEmail("");

                        startActivity(new Intent(getApplicationContext(), LoginScreen.class));
                        finish();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()){

         //   case R.id.usercardid:
          //      i = new Intent(this, AccountControl.class);
          //      startActivity(i);
          //      break;

            case R.id.bookingcardid:
                i = new Intent(this, SelectionStation.class);
                i.putExtra(Constants.LOGGED_USER, user);
                startActivity(i);
                break;

           // case R.id.addparkingcardview:
           //     i = new Intent(this, Addparking.class);
            //    startActivity(i);
             //   break;

            case R.id.mapcardid:
                i = new Intent(this, MapNavigate.class);
                startActivity(i);
                break;

            default:
                break;
        }
    }
}