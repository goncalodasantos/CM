package com.example.gonca.smtucky;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.JsonReader;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class ConnectAPI extends Service {
    static final int CONNECTION_TIMEOUT = 1000;
    static final int DATARETRIEVAL_TIMEOUT = 1000;

    public ConnectAPI() {
    }

    /*this method handles a single incoming request */
    @Override
    public int onStartCommand(Intent intent, int flags, int
            id){
        String decision = intent.getStringExtra("decision");


        Thread thread;
        if(decision.equals("getRoute")){
            int value1 = Integer.parseInt(intent.getStringExtra("routenumber"));
            thread =new Thread(new GetRoute(value1));
            thread.start();
        }
        else if(decision.equals("getRouteInformation")){
            int value1 = Integer.parseInt(intent.getStringExtra("routenumber"));
            thread =new Thread(new GetRouteInformation(value1));
            thread.start();
        }
        else if(decision.equals("getRoutes")){
            thread =new Thread(new GetRoutes());
            thread.start();
        }

        return START_STICKY;//stay running
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null; //7disable binding
    }






    private static String getResponseText(InputStream inStream) {
        // very nice trick from
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        return new Scanner(inStream).useDelimiter("\\A").next();
    }




    class GetRoute implements Runnable {
        int route;

        GetRoute(int route) {
            this.route = route;
        }

        public void run() {
            JSONObject response = null;
            HttpURLConnection urlConnection = null;
            try {
                // create connection
                Log.v("stuffs:",Integer.toString(route));

                URL urlToRequest = new URL("http://192.168.137.1:5000/route/" + Integer.toString(route));
                urlConnection = (HttpURLConnection)
                        urlToRequest.openConnection();
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

                // handle issues
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    // handle unauthorized (if service requires user login)
                } else if (statusCode != HttpURLConnection.HTTP_OK) {
                    // handle any other errors, like 404, 500,..
                }

                // create JSON object from content
                InputStream in = new BufferedInputStream(
                        urlConnection.getInputStream());
                response = new JSONObject(getResponseText(in));
                Log.d("stuff", response.toString());
            } catch (MalformedURLException e) {
                Log.d("stuff", "URL Exception");
            } catch (SocketTimeoutException e) {
                Log.d("stuff", "Socket Exception");
            } catch (IOException e) {
                Log.d("stuff", "IOException");
            } catch (JSONException e) {
                Log.d("stuff", "Json Exception");
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            Intent done = new Intent();
            done.setAction("action");
            done.putExtra("routenumber", response.toString());
            sendBroadcast(done);
        }
    }

    class GetRouteInformation implements Runnable {
        int route;

        GetRouteInformation(int route) {
            this.route = route;
        }

        public void run() {
            JSONObject response = null;
            HttpURLConnection urlConnection = null;
            try {
                // create connection
                URL urlToRequest = new URL("http://192.168.137.1:5000/route/information/" + Integer.toString(route));
                urlConnection = (HttpURLConnection)
                        urlToRequest.openConnection();
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

                // handle issues
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    // handle unauthorized (if service requires user login)
                } else if (statusCode != HttpURLConnection.HTTP_OK) {
                    // handle any other errors, like 404, 500,..
                }

                // create JSON object from content
                InputStream in = new BufferedInputStream(
                        urlConnection.getInputStream());
                response = new JSONObject(getResponseText(in));
                Log.d("stuff", response.toString());
            } catch (MalformedURLException e) {
                Log.d("stuff", "URL Exception");
            } catch (SocketTimeoutException e) {
                Log.d("stuff", "Socket Exception");
            } catch (IOException e) {
                Log.d("stuff", "IOException");
            } catch (JSONException e) {
                Log.d("stuff", "Json Exception");
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            Intent done = new Intent();
            done.setAction("action");
            done.putExtra("routenumber", response.toString());
            sendBroadcast(done);
        }
    }

    class GetRoutes implements Runnable {
        int route;

        GetRoutes() {

        }

        public void run() {
            JSONObject response = null;
            HttpURLConnection urlConnection = null;
            try {
                // create connection
                URL urlToRequest = new URL("http://192.168.137.1:5000/routes");
                urlConnection = (HttpURLConnection)
                        urlToRequest.openConnection();
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

                // handle issues
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    // handle unauthorized (if service requires user login)
                } else if (statusCode != HttpURLConnection.HTTP_OK) {
                    // handle any other errors, like 404, 500,..
                }

                // create JSON object from content
                InputStream in = new BufferedInputStream(
                        urlConnection.getInputStream());
                response = new JSONObject(getResponseText(in));

                Log.d("stuff", response.toString());
            } catch (MalformedURLException e) {
                Log.d("stuff", "URL Exception");
            } catch (SocketTimeoutException e) {
                Log.d("stuff", "Socket Exception");
            } catch (IOException e) {
                Log.d("stuff", "IOException");
            } catch (JSONException e) {
                Log.d("stuff", "Json Exception");
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            Intent done = new Intent();
            done.setAction("action");
            done.putExtra("routenumber", response.toString());

            sendBroadcast(done);
        }
    }




}
