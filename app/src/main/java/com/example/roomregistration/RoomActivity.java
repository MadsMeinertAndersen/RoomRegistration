package com.example.roomregistration;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Comment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class RoomActivity extends Activity {

    public static final String ROOM_URL = "http://anbo-roomreservationv3.azurewebsites.net/api/rooms";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        final ReadJSONFeedTast task = new ReadJSONFeedTast();
        task.execute(ROOM_URL);

            Toolbar toolBar = findViewById(R.id.toolbar);
            setActionBar(toolBar);


        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Button logInButton = (Button) findViewById(R.id.SignInButton);
            logInButton.setVisibility(View.VISIBLE);
            getActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    public void SignInPageClick(View view) {

            finish();
            startActivity(new Intent(this, MainActivity.class));

    }

    private class ReadJSONFeedTast extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return readJSonFeed(urls[0]);
            } catch (IOException ex) {
                cancel(true);
                return ex.toString();
            }
        }


        @Override
        protected void onPostExecute(String jsonString) {


            Gson gson = new GsonBuilder().create();
            final Room[] rooms = gson.fromJson(jsonString.toString(), Room[].class);

            ListView roomListView = findViewById(R.id.RoomList);
            ArrayAdapter<Room> roomAdapter = new ArrayAdapter<Room>(getBaseContext(), android.R.layout.simple_list_item_1, rooms);
            roomListView.setAdapter(roomAdapter);

            roomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent RoomIntent = new Intent(getBaseContext(), SpecificRoomActivity.class);
                    Room room = (Room) adapterView.getItemAtPosition(i);
                    RoomIntent.putExtra(SpecificRoomActivity.ROOM, room);
                    startActivity(RoomIntent);
                }
            });
        }

        @Override
        protected void onCancelled(String message) {
            super.onCancelled(message);
            final TextView textView = findViewById(R.id.RoomErrorText);
            textView.setText(message);
        }
    }




    private String readJSonFeed(String urlString) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        final InputStream content = openHttpConnection(urlString);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        while (true) {
            final String line = reader.readLine();
            if (line == null)
                break;
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    private InputStream openHttpConnection(final String urlString) throws IOException {
        final URL url = new URL(urlString);
        final URLConnection conn = url.openConnection();
        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        final HttpURLConnection httpConn = (HttpURLConnection) conn;
        httpConn.setAllowUserInteraction(false);
        // No user interaction like dialog boxes, etc.
        httpConn.setInstanceFollowRedirects(true);
        // follow redirects, response code 3xx
        httpConn.setRequestMethod("GET");
        httpConn.connect();
        final int response = httpConn.getResponseCode();
        if (response == HttpURLConnection.HTTP_OK) {
            return httpConn.getInputStream();
        } else {
            throw new IOException("HTTP response not OK");
        }
    }
}
