package my.edu.tarc.smartb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class ProfileActivity extends AppCompatActivity {

    private TextView nric,name,studID,programme,faculty;
    private EditText email,contactNo;
    private Button btn_logout;
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "my.edu.tarc.smartb";
    private static String URL_EDIT = "https://yapsm-wa16.000webhostapp.com/StudentEdit.php";
    private Menu action;
    String getID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mPreferences = getSharedPreferences(sharedPrefFile,MODE_PRIVATE);
        getID = mPreferences.getString("ID_KEY","");

        name =  findViewById(R.id.tvName);
        email =  findViewById(R.id.tvEmail);
        studID =  findViewById(R.id.tvID);
        nric =  findViewById(R.id.tvNric);
        faculty =  findViewById(R.id.tvFaculty);
        programme =  findViewById(R.id.tvCourse);
        contactNo = findViewById(R.id.tvContact);
        btn_logout = findViewById(R.id.btnSignout);

        // Retrieve data from shared preferences
        String extraName = mPreferences.getString("NAME_KEY","");
        String extraEmail = mPreferences.getString("EMAIL_KEY","");
        String extraContactNo = mPreferences.getString("CONTACT_KEY","");
        String extranric = mPreferences.getString("NRIC_KEY","");
        String extraStudID = mPreferences.getString("ID_KEY","");
        String extraProgramme = mPreferences.getString("PROGRAMME_KEY","");
        String extraFaculty = mPreferences.getString("FACULTY_KEY","");

        name.setText(extraName);
        email.setText(extraEmail);
        studID.setText(extraStudID);
        nric.setText(extranric);
        faculty.setText(extraFaculty);
        programme.setText(extraProgramme);
        contactNo.setText(extraContactNo);

        email.setFocusableInTouchMode(false);
        contactNo.setFocusableInTouchMode(false);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreferences = getSharedPreferences(sharedPrefFile,MODE_PRIVATE);
                SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                preferencesEditor.clear();
                preferencesEditor.commit();
                Intent intent = new Intent(ProfileActivity.this,LandingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_action,menu);

        action = menu;
        action.findItem(R.id.menu_save).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_edit:
                email.setFocusableInTouchMode(true);
                contactNo.setFocusableInTouchMode(true);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(email,InputMethodManager.SHOW_IMPLICIT);

                action.findItem(R.id.menu_edit).setVisible(false);
                action.findItem(R.id.menu_save).setVisible(true);

                return true;

            case R.id.menu_save:

                SaveEditDetail();

                action.findItem(R.id.menu_edit).setVisible(true);
                action.findItem(R.id.menu_save).setVisible(false);

                email.setFocusableInTouchMode(false);
                contactNo.setFocusableInTouchMode(false);
                email.setFocusable(false);
                contactNo.setFocusable(false);


                return true;

                default:return super.onOptionsItemSelected(item);
        }
    }

    private void SaveEditDetail(){
        final String email = this.email.getText().toString().trim();
        final String contactNo = this.contactNo.getText().toString().trim();
        final String studID = getID;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if(success.equals("1")){
                        Toast.makeText(ProfileActivity.this, "Success!",Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                        preferencesEditor.putString("ID_KEY",studID);
                        preferencesEditor.putString("EMAIL_KEY",email);
                        preferencesEditor.putString("CONTACT_KEY",contactNo);
                        preferencesEditor.apply();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivity.this,"JSONError" + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileActivity.this,"VolleyError" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",email);
                params.put("contactNo",contactNo);
                params.put("studID",studID) ;
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}