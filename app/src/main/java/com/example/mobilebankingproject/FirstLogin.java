package com.example.mobilebankingproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FirstLogin extends AppCompatActivity {

    private Button change;
    private EditText password1, password2;
    TextView firstTime;
    String url ="http://192.168.1.4/test_android/home.php";
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        change = findViewById(R.id.change);
        password1 =  findViewById(R.id.password);
        password2 = findViewById(R.id.checkPassword);
        firstTime = findViewById(R.id.firstLoginText);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        final String mId = user.get(sessionManager.ID);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = password1.getText().toString().trim();
                String checkPassword = password2.getText().toString().trim();

                if(password.isEmpty() && !checkPassword.isEmpty()) {
                    password1.setError("Please enter the password");
                    password2.setError("Please enter the password");

                } else if (password.equals(checkPassword)){
                    ChangePassword(password,mId);

                } else {
                    password1.setError("Please insert the same password");
                    password2.setError("Please insert the same password");
                }
            }
        });

    }

   private void ChangePassword(final String password, final String mId){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {

                            //converting response to json object

                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            System.out.println("raspuns:  " + success);

                            if(success.equals("true")) {

                                Toast.makeText(FirstLogin.this,"You have successfully changed your password",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(FirstLogin.this,HomeActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(FirstLogin.this,
                                        "Something went wrong.",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("raspuns:  " + response);
                            Toast.makeText(FirstLogin.this,
                                    "Error1234 " + e.toString(),Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FirstLogin.this, "Error4321 " +error.toString(),Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "Error " + error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("password", password);
                params.put("id",mId);
                System.out.println("mID  = " + mId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}