package com.example.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.creditcard.R;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private EditText CdCredt, Amt;
    private Button Prdt;
    private TextView Rst;
    private String url = "https://creditcardmobileapp12-f844c7fb4134.herokuapp.com/predict";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        CdCredt = view.findViewById(R.id.CcNum);
        Amt = view.findViewById(R.id.amt);
        Prdt = view.findViewById(R.id.predict);
        Rst = view.findViewById(R.id.result);

        Prdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("cc_num", CdCredt.getText().toString());
                    jsonBody.put("amt", Amt.getText().toString());

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST, url, jsonBody,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String data = response.getString("result");
                                        if (data.equals("1")) {
                                            Rst.setText("Fraud");
                                        } else {
                                            Rst.setText("Not Fraud");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    queue.add(jsonObjectRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
}
