package rd.fuel.project.com.rd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import rd.fuel.project.com.rd.R;
import rd.fuel.project.com.rd.networking.HttpClient;
import rd.fuel.project.com.rd.utilitys.ActivityConstants;

/**
 * @author hector castillo
 */
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDataSource();
    }

    /***
     * Get the data feed for the app.
     */
    private void getDataSource() {
        final String url = getResources().getString(R.string.rss_url);
        String response = HttpClient.getDataFromResources(this, url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        startDashboardActivity(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG_ERROR", error.getMessage());
                    }
                }
        );
        
    }

    private void startDashboardActivity(String response) {
        Intent intent = new Intent(this, DashboardContainerActivity.class);
        intent.putExtra(ActivityConstants.EXTRA_DATA_SOURCE, response);
        startActivity(intent);

        finish();
    }
}
