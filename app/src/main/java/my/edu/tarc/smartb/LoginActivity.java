package my.edu.tarc.smartb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    private EditText txtStudID, txtPassword;
    private Button btnLogin;
    private static String URL_LOGIN = "https://yapsm-wa16.000webhostapp.com/StudentLogin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtStudID = findViewById(R.id.txtID);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mStudID = txtStudID.getText().toString().trim();
                String mPass = txtPassword.getText().toString().trim();

                if(!mStudID.isEmpty() || !mPass.isEmpty()){
                    Login(mStudID,mPass);
                }else{
                    txtStudID.setError("Please enter student ID");
                    txtPassword.setError("Please enter password");
                }

            }
        });
    }

    private void Login(final String studID, final String password){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        testing to check error
//                        txtStudID.setText(response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if(success.equals("1")){

                                for(int i = 0; i < jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    // To display the toast which says the status of login
                                    String studID = object.getString("studID").trim();
                                    String name = object.getString("name").trim();

                                    Toast.makeText(LoginActivity.this,
                                            "Success Login. \nYour ID: "
                                            +studID+"\nYour Name: "+name, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this,
                                        "Failed to login", Toast.LENGTH_SHORT).show();
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this,
                                    "JSON Error " +e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,
                                "Volley Error " +error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("studID",studID);
                params.put("password",password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
