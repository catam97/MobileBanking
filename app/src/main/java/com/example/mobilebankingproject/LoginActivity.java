package com.example.mobilebankingproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private Button login;
    private EditText username, password;
    String url ="http://192.168.1.4/test_android/login1.php";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

         login.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                  String mUsername = username.getText().toString().trim();
                  String mPass = password.getText().toString().trim();

                 if(!mPass.isEmpty() && !mUsername.isEmpty()) {

                     Login(mUsername,mPass);

                 } else {
                     username.setError("Please enter username");
                     password.setError("Please enter password");
                 }
             }
         });


    }

    private void Login(final String username, final String password) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {

                            //converting response to json object

                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");


                            if(success.equals("true")) {

                                JSONObject jsonObject1 = jsonObject.getJSONObject("login");
                                String name = jsonObject1.getString("name").trim();
                                String id = jsonObject1.getString("id").trim();
                                System.out.println("raspuns1 " + response);
                                sessionManager.createSession(name,id);

                                String firstLogin = jsonObject1.getString("firstLogin").trim();

                                if(firstLogin.equals("1")){
                                    Intent intent = new Intent(LoginActivity.this, FirstLogin.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                }

                            } else {
                                //System.out.println("raspuns1 " + response);
                                Toast.makeText(LoginActivity.this, "Invalid username or password.",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("raspuns1 " + response);
                            Toast.makeText(LoginActivity.this,
                                    "Error1234 " + e.toString(),Toast.LENGTH_SHORT).show();
                            Log.e("TAG","Error"+e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "Error4321 " +error.toString(),Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "Error " + error.getMessage());
                    //Log.d(TAG, "Error StackTrace: \t" + error.getStackTrace());
                }
        })
        {
          @Override
          protected Map<String, String> getParams() throws AuthFailureError{
              Map<String, String> params = new HashMap<>();
              params.put("username", username);
              params.put("password", password);
              return params;
          }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }


}