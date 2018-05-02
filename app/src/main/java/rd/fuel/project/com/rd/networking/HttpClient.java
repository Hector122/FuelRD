package rd.fuel.project.com.rd.networking;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import rd.fuel.project.com.rd.R;

public class HttpClient {
    private static HttpClient mInstance;
    private static RequestQueue mRequestQueue;
    private static Context mContext;

    private HttpClient(Context context) {
        mContext = context;
    }

    public static synchronized HttpClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new HttpClient(context);
        }
        return mInstance;
    }

    /**
     * Check is the user have a active internet connectivity.
     *
     * @param context Activity context
     * @return true is have internet connection.
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


    public void getRssFromMic(StringRequest stringRequest ) {
        final String result;

        if(isConnected(mContext)){
            // Instance the cache
            Cache cache = new DiskBasedCache(mContext.getCacheDir(), MAX_CACHE_IN_BYTES);

            Network network = new BasicNetwork(new HurlStack());

            RequestQueue requestQueue = new RequestQueue(cache, network);
            requestQueue.start();

            // Making the request
            requestQueue.add(stringRequest);

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(R.string.not_internet_connection);
        }
    }


    /* Time in milliseconds */
    private static final int TIMEOUT_READ = 10000;
    private static final int TIMEOUT_CONNECT = 15000;
    private static final String DEBUG_TAG = "DEBUG_TAG";
    private static final int MAX_CACHE_IN_BYTES = 1024 * 4;


    /***
     * Make a Http GET request to url provider.
     *
     * @param myUrl String with the rss url.
     * @return String in XML format with the data.
     */

    public String getRSS(String myUrl) {
        InputStream inputStream = null;
        String result = "";

        int len = 1024 * 40;

        try {
            URL url = new URL(myUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(TIMEOUT_READ);
            connection.setConnectTimeout(TIMEOUT_CONNECT);
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            // Initializer the connection.
            connection.connect();

            int response = connection.getResponseCode();
            Log.d(DEBUG_TAG, "The response is " + response);

            if (response == HttpURLConnection.HTTP_OK) {
                // Convert the inputString to string
                inputStream = connection.getInputStream();
                result = convertInputStreamToString(inputStream);

            } else {
                inputStream = connection.getErrorStream();
                Log.d(DEBUG_TAG, "The error response is: " +
                        convertInputStreamToString(inputStream));
            }

            connection.disconnect();

            if (inputStream != null) {
                inputStream.close();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(DEBUG_TAG, "XML_String: " + result.trim());

        return result.trim();
    }

    /***
     * Convert InputStream to String in format the xml encode &gt, &lt
     *
     * @param inputStream InputStream receive form the request.
     * @return String XML.
     * @throws IOException
     */

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));

        String line = "";

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        inputStream.close();

        return stringBuilder.toString();
    }


    //TODO: Correct check best Practice for this.
    static private String instanceVariable;

    public static String getDataFromResources(final Context context, final String url) {
        // Request a string response for the provider URL.
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Put return code here.
                instanceVariable = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG_ERROR", error.getMessage());
                instanceVariable = null;
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Log.i("TAG_INFO", String.valueOf(response.statusCode));

                return super.parseNetworkResponse(response);
            }
        };

        // Get the singleton instance and add to the request queue.
        VolleyHttpClient client = VolleyHttpClient.getInstance(context);
        client.addRequestQueue(stringRequest);

        return instanceVariable;
    }
}
