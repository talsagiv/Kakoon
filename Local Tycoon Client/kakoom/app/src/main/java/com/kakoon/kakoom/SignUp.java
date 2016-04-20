package com.kakoon.kakoom;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class SignUp extends AppCompatActivity {
    private TextView tet;
    private Socket socket;
    private String str;
    private String str1;
    EditText et, et1;
    PrintWriter out;
    BufferedReader s_in = null;

    private static final int SERVERPORT = 50000;
    private static final String SERVER_IP = "10.208.10.7";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        tet = (TextView) findViewById(R.id.txt);

        et = (EditText) findViewById(R.id.EditText01);
        et1 = (EditText) findViewById(R.id.editText);
        this.tet = (TextView) findViewById(R.id.txt);

    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "nothing";

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

                socket = new Socket(serverAddr, SERVERPORT);

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }


            try {
                out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);
            } catch (java.io.IOException e) {
            }
            String mes = "";


            String os = new String(Base64.encodeBase64(str.getBytes()));
            String os1 = new String(Base64.encodeBase64(str1.getBytes()));
            mes = "reg|" + os + "|" + os1;
            Log.i("echo:", mes);
            out.println(mes);

            Log.i("echo:", "finsend");
            try {
                Log.i("echo:", "try1");
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.i("echo:", "finbuffer");
                    int c;
                    StringBuilder outter = new StringBuilder();
                    char[] buffer = new char[1024];
                    c = in.read(buffer);
                    Log.i("echo:", "afterfirst read");
                    Log.i("echo", String.valueOf(c));
                    outter.append(buffer, 0, c);
                    Log.i("echo", outter.toString());
                }catch (java.io.IOException e){

                    Log.e("err", e.toString());
                }
            return response;
        }


        @Override
        protected void onPostExecute(String result) {
            tet.setText(result);
        }
    }


    public void onClick(View view) {
        try {
            str = et.getText().toString();
            str1 = et1.getText().toString();
            DownloadWebPageTask task = new DownloadWebPageTask();
            task.execute(new String[]{"http://www.vogella.com"});
            /*
            EditText et = (EditText) findViewById(R.id.EditText01);
            EditText et1 = (EditText) findViewById(R.id.editText);
            this.tet = (TextView) findViewById(R.id.txt);
            String str = et.getText().toString();
            String str1 = et1.getText().toString();
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);

            String mes = "";
            */
            /*
            String os = new String(Base64.encodeBase64(str.getBytes()));
            String os1 = new String(Base64.encodeBase64(str1.getBytes()));
            mes= "reg|"+os+"|"+os1;
            Log.i("echo:",mes);
            out.println(mes);
            */
            /*

            Thread thread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn =
                    new BufferedReader(
                            new InputStreamReader(System.in));
            String userInput;
            String t = "";
            int byter;
            Log.i("echo:","before");

            while ((byter = in.read()) != -1) {
                Log.i("echo:","inside");

                byter = in.read();

            }
            //Log.i("echo:","t+"+userInput.toString());
            Log.i("echo:","out");


                        //Your code goes here
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });*/
            /*
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.i("echo:","i");
        } catch (IOException e) {
            Log.i("echo:","ii");
            e.printStackTrace();
            */
        } catch (Exception e) {
            Log.i("echo:","iiji");
            e.printStackTrace();
        }





    }
}