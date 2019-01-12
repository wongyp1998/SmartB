package my.edu.tarc.smartb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private TextView contactNo,nric,name,email,studID,programme,faculty;
    private Button btn_logout;
    SharedPreferences sharedPreferences = getSharedPreferences("my.edu.tarc.smartb",MODE_PRIVATE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name =  findViewById(R.id.tvName);
        email =  findViewById(R.id.tvEmail);
        studID =  findViewById(R.id.tvID);
        nric =  findViewById(R.id.tvNric);
        faculty =  findViewById(R.id.tvFaculty);
        programme =  findViewById(R.id.tvCourse);
        contactNo = findViewById(R.id.tvContact);
        btn_logout = findViewById(R.id.btnSignout);

//        Intent intent = getIntent();
//        String extraName = intent.getStringExtra("name");
//        String extraEmail = intent.getStringExtra("email");
//        String extraContactNo = intent.getStringExtra("contactNo");
//        String extranric = intent.getStringExtra("nric");
//        String extraStudID = intent.getStringExtra("studID");
//        String extraProgramme = intent.getStringExtra("programme");
//        String extraFaculty = intent.getStringExtra("faculty");

        String extraName = sharedPreferences.getString("NAME_KEY","");
        String extraEmail = sharedPreferences.getString("EMAIL_KEY","");
        String extraContactNo = sharedPreferences.getString("CONTACT_KEY","");
        String extranric = sharedPreferences.getString("NRIC_KEY","");
        String extraStudID = sharedPreferences.getString("ID_KEY","");
        String extraProgramme = sharedPreferences.getString("PROGRAMME_KEY","");
        String extraFaculty = sharedPreferences.getString("FACULTY_KEY","");

        name.setText(extraName);
        email.setText(extraEmail);
        studID.setText(extraStudID);
        nric.setText(extranric);
        faculty.setText(extranric);
        programme.setText(extraProgramme);
        contactNo.setText(extraContactNo);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





    }
}
