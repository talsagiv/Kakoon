/**
 * Created by Iddo on 4/20/2016.
 */

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class WorkingSocket {
    public int ip;
    public int port;


    public WorkingSocket(){}






    public Recive_Data(int size)
    {
        Task_Recive_Data task = new Task_Recive_Data();
        return task.execute();
    }




    private class  Task_Recive_Data extends AsyncTask<Int, Void, Int> {
        @Override
        protected String doInBackground(Int... size) {
            String response = "nothing";
            for (String siz : size) {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                int c;
                StringBuilder outter = new StringBuilder();
                char[] buffer = new char[size];
                c = in.read(buffer);
                outter.append(buffer, 0, c);

            }catch (IOException e){
            }
            return outter;
        }


        @Override
        protected void onPostExecute(String result) {

        }
    }

        public Send_Data(string data)
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
            }
        }

        @Override
        protected void onPostExecute(String result) {}
    }

        public connect(string Server_ip, int Server_port) {
            self.ip = Server_ip;
            self.port = Server_port;
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

        }

        @Override
        protected void onPostExecute(String result) {
            );
        }
    }











}
