package com.effone.pdlconnprovider.volley;

import android.util.Log;

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
 * Created by sarith.vasu on 10-06-2016.
 */
public class PdlFileRequest  extends Request<byte[]> {
    private final Response.Listener<byte[]> mListener;
    private final int MY_SOCKET_TIMEOUT_MS=20000;


    public Map<String, String> responseHeaders;

    public PdlFileRequest(int method, String url, Response.Listener<byte[]> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        setShouldCache(false);
        mListener = listener;
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        byte[] json = null;

        responseHeaders = response.headers;

        //Pass the response data here
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));

    }

    @Override
    protected void deliverResponse(byte[] response) {
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
