package com.kakoon.kakoom;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
//import org.apache.commons.codec.*;
//import org.apache.commons.codec.binary.Base64;

public class SignIn extends AppCompatActivity {
    public String resp = "";
    EditText et, et1,val;
    PrintWriter out;
    private static final int port = 50000;
    private static final String ip = "192.168.0.248";
    TextView tet;


    private Socket socket;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button btn = (Button)findViewById(R.id.refresh) ;


        assert btn != null;
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Recive_Data();
                tet.setText(resp);
            }
        });



        setContentView(R.layout.activity_sign_in);
        val = (EditText) findViewById(R.id.type);
        et = (EditText) findViewById(R.id.EditText01);
        et1 = (EditText) findViewById(R.id.editText);
       tet = (TextView) findViewById(R.id.txt);
        tet.setText("hello");
        tet = (TextView) findViewById(R.id.txt);
        tet.setText("connecting on ip: "+ip+" port: "+String.valueOf(port));
        connect(ip,port);

    }




    public void onClick(View view) {

        String str = et.getText().toString();
        String str1 = et1.getText().toString();
        String vvl = val.getText().toString();
        String os = new String(Base64.encodeBase64(str.getBytes()));
        String os1 = new String(Base64.encodeBase64(str1.getBytes()));
        String l = new String(Base64.encodeBase64("32.0959783".getBytes()));
        String l1 = new String(Base64.encodeBase64("34.8126455".getBytes()));
        String mes= vvl+"|"+os+"|"+os1;
        Log.i("echo:",mes);

        Send_Data(mes);
        try {
            Thread.sleep(500);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        String t = Recive_Data();

        Log.i("echo:",resp);
        tet.setText(mes+resp);
        resp = "";





    }


    public void connect(String Server_ip, int Server_port) {

        Task_connect task = new Task_connect();
        task.execute();
    }


    private class Task_connect extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "nothing";

            try {
                InetAddress serverAddr = InetAddress.getByName(ip);

                socket = new Socket(serverAddr, port);

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
    public String Recive_Data()
    {
        Task_Recive_Data task = new Task_Recive_Data();
        task.execute();
    return "hi";}


    private class  Task_Recive_Data extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... size) {
            StringBuilder outter = new StringBuilder();
            String response = "nothing";

                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    int c;
                    outter = new StringBuilder();
                    char[] buffer = new char[5000];
                    c = in.read(buffer);
                    outter.append(buffer, 0, c);

                } catch (IOException e) {
                }
            Log.i("echo",outter.toString());
                response = outter.toString();
            resp = response;
            return response;
            }


            @Override
            protected void onPostExecute (String result){

            }
        }




    public void Send_Data(String data)
    {
        Task_Send_Data task = new Task_Send_Data();
        task.execute(new String[]{data});
    }


    private class Task_Send_Data extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... mess) {
            String response = "nothing";
            for (String mes : mess) {
                try {
                    out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())), true);
                } catch (IOException e) {
                }
                out.println(mes);
            }return "hi";
        }

        @Override
        protected void onPostExecute(String result) {}
    }

}


