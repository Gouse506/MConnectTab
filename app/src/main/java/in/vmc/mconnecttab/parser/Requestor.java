package in.vmc.mconnecttab.parser;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import in.vmc.mconnecttab.utils.TAG;

/**
 * Created by mukesh on 11/3/16.
 */
public class Requestor implements TAG {
    public static JSONObject requestAllSites(RequestQueue requestQueue, String url, final String bid, final String offset, final String limit) {
        JSONObject response = null;
        String resp;
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.POST, url, requestFuture, requestFuture) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(BID, bid);
                params.put(OFFSET, offset);
                params.put(LIMIT, limit);
                return params;
            }
        };

        requestQueue.add(request);
        try {
            resp = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            response = new JSONObject(resp);
        } catch (InterruptedException e) {
            //L.m(e + "");
        } catch (ExecutionException e) {
            // L.m(e + "");
        } catch (TimeoutException e) {
            //L.m(e + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
}
