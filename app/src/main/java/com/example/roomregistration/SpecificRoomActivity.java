package com.example.roomregistration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;
import okhttp3.*;


import com.google.android.gms.common.api.Api;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SpecificRoomActivity extends Activity {

    public static final String ROOM = "ROOM";
    private Room room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_room);

        Toolbar toolBar = findViewById(R.id.toolbar);
        setActionBar(toolBar);

        Intent intent = getIntent();
        room = (Room) intent.getSerializableExtra(ROOM);

        TextView roomName = (TextView) findViewById(R.id.RoomName);
        roomName.setText("Room name: "+room.getName());

        TextView roomDesc = (TextView) findViewById(R.id.RoomDesc);
        roomDesc.setText("Room description: "+room.getDescription());

        Integer cap = room.getCapacity();
        TextView roomCap = (TextView) findViewById(R.id.RoomCap);
        roomCap.setText("Room capacity: "+cap.toString());


        TextView roomRemarks = (TextView) findViewById(R.id.RoomRemarks);
        if (room.getRemarks() == null) {
            roomRemarks.setText("Room remarks: No remarks");
        } else
        {
            roomRemarks.setText("Room remarks: "+room.getRemarks().toString());
        }

        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            Button showButton = (Button) findViewById(R.id.reserveButton);
            showButton.setVisibility(View.VISIBLE);
        }


    }
    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        room = (Room) intent.getSerializableExtra(ROOM);

        getReservationsForSpecificRoom(room.getId());
    }




    private void getReservationsForSpecificRoom(int roomId)
    {
        String url = "http://anbo-roomreservationv3.azurewebsites.net/api/Reservations/room/" + roomId;
        OkHttpClient client = new OkHttpClient();
        Request.Builder request = new Request.Builder();
        Request build = request.url(url).build();

        client.newCall(build).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {


            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {

                if (!response.isSuccessful())
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView failure = findViewById(R.id.forFailure);
                            failure.setText("Der opstod en fejl " + response.message());
                        }
                    });

                } else {
                    final String jsonString = response.body().string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new GsonBuilder().create();


                            final Reservation[] re = gson.fromJson(jsonString, Reservation[].class);
                            ListView reservationListView = findViewById(R.id.ReservationsList);
                            ArrayAdapter<Reservation> reservationAdapter = new ArrayAdapter<Reservation>(getBaseContext(), android.R.layout.simple_list_item_1, re);
                            reservationListView.setAdapter(reservationAdapter);
                        }
                    });

                }


            }
                

        });
    }


    public void MakeReservationClick(View view) {
        Intent RoomIntent = new Intent(this, MakeReservationActivity.class);
        RoomIntent.putExtra("roomId", room.getId());
        startActivity(RoomIntent);

    }
}
