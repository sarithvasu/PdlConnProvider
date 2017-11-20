package com.effone.pdlconnprovider.volley;

import android.util.Base64;

import com.android.volley.AuthFailureError;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sarith.vasu on 16-05-2016.
 */
public class PdlRequest extends Request<String> {

    private final Response.Listener<String> mListener;
    private final int MY_SOCKET_TIMEOUT_MS=20000;


    public Map<String, String> responseHeaders;

    public PdlRequest(int method, String mUrl, Response.Listener<String> listener,
                      Response.ErrorListener errorListener) {
        // TODO Auto-generated constructor stub

        super(method, mUrl, errorListener);
        // this request would never use cache.
        setShouldCache(false);
        mListener = listener;

    };

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String json = null;
        try {
            json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(json,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;

        }

    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("AuthToken", "12345");
        return headers;
    }

    @Override
    public void setRetryPolicy(RetryPolicy retryPolicy) {
        super.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
