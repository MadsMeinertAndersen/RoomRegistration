package com.example.roomregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyReservations extends Activity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservations);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();


    }

    @Override
    protected void onStart() {
        super.onStart();

        getReservationsForSpecificRoom(firebaseAuth.getUid(), new Date().getTime() / 1000);

    }



    private void getReservationsForSpecificRoom(String userId, long fromTime)
    {
        String url = "http://anbo-roomreservationv3.azurewebsites.net/api/Reservations/user/" + userId + "/" + fromTime;
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
                                    Log.d("TAG", "Number of reservations: " + re.length);
                                    ListView myReservationsListView = findViewById(R.id.MyResList);
                                    ArrayAdapter<Reservation> reservationAdapter = new ArrayAdapter<Reservation>(getBaseContext(), android.R.layout.simple_list_item_1, re);
                                    myReservationsListView.setAdapter(reservationAdapter);
                                    myReservationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                            Intent RoomIntent = new Intent(getBaseContext(), SpecificReservationActivity.class);
                                            Reservation reservation = (Reservation) adapterView.getItemAtPosition(i);
                                            RoomIntent.putExtra(SpecificReservationActivity.RESERVATION, reservation);
                                            startActivity(RoomIntent);

                                        }
                                    });


                                }
                            });


                }
            }
        });
    }
}
