package my.edu.tarc.smartb;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class DateActivity extends AppCompatActivity {


    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;


    private TextView starttime;
    private TextView endtime;
    private TextView duration;
    private Button confirmButton;

    private RadioGroup radiogroupValue;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    String URL= "https://yapsm-wa16.000webhostapp.com/insertBadmintonBook.php";

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "my.edu.tarc.smartb";
    String getID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        //Calendar
        dateView = (TextView) findViewById(R.id.editText4);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        starttime = (TextView) findViewById(R.id.editText3);
        endtime = (TextView) findViewById(R.id.editText2);
        duration = (TextView) findViewById(R.id.editText);
        confirmButton = findViewById(R.id.button);
        radiogroupValue = findViewById(R.id.radioGroupBooking);

        requestQueue = Volley.newRequestQueue(DateActivity.this);
        progressDialog = new ProgressDialog(DateActivity.this);

        mPreferences = getSharedPreferences(sharedPrefFile,MODE_PRIVATE);
        getID = mPreferences.getString("ID_KEY","");


        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(DateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        starttime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(DateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endtime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        endtime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                Date d1 = null;
                Date d2 = null;

                try {
                    d1 = format.parse(starttime.getText().toString());
                    d2 = format.parse(endtime.getText().toString());

                    long diff = d2.getTime() - d1.getTime();

                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000) % 24;

                    duration.setText(diffHours + " Hrs " + diffMinutes + " Min");

                } catch (Exception ex) {
                    Toast.makeText(DateActivity.this, "Something Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Please Wait, We are added your booking!");
                progressDialog.show();

                String bookingDate = dateView.getText().toString();
                String bookingStartTime = starttime.getText().toString();
                String bookingEndTime = endtime.getText().toString();
                String bookingDuration = duration.getText().toString();
                String courtNumber = " ";

                int courtid = radiogroupValue.getCheckedRadioButtonId();

                if (courtid == R.id.rbcourt4) {
                    courtNumber = " Court 4";
                } else if (courtid == R.id.rbcourt3) {
                    courtNumber = " Court 3";
                } else if (courtid == R.id.rbcourt2) {
                    courtNumber = " Court 2";
                } else if (courtid == R.id.rbcourt1) {
                    courtNumber = " Court 1";
                }

                valueGetFrom(bookingDate, bookingStartTime, bookingEndTime, bookingDuration, courtNumber);

            }
        });

    }

    public void valueGetFrom(final String bookingDate,final String bookingStartTime,final String bookingEndTime,final String bookingDuration , final String courtNumber)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                Toast.makeText(DateActivity.this, response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(DateActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }

        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("StudID", getID);
                params.put("Date", bookingDate);
                params.put("StartTime", bookingStartTime);
                params.put("EndTime", bookingEndTime);
                params.put("HoursBook", bookingDuration);
                params.put("FacID", getIntent().getStringExtra("FacID"));
                params.put("Court", courtNumber);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(DateActivity.this);

        requestQueue.add(stringRequest);
    }



    @SuppressWarnings("deprecation")
    public void setDate(View view)
    {
        showDialog(999);
        Toast.makeText(getApplicationContext(),"ca", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        // TODO Auto-generated method stub
        if (id == 999)
        {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3)
                {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day)
    {
        dateView.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
    }


}
