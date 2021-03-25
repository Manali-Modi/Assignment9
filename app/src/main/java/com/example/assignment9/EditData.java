package com.example.assignment9;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditData extends AppCompatActivity {

    String id;
    String name, email, contact, dob, address, course, bloodGroup, gender;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    int _id;

    EditText etName, etEmail, etContact, etDob, etAddress;
    Spinner spnCourse, spnBloodGroup;
    RadioGroup rdgGender;
    RadioButton rdoMale, rdoFemale;
    ImageButton ibDob;
    Button btnSave;

    MyDatabase database;
    Context ctx = this;
    String[] courses;
    String[] bloodGroups;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_form);

        memoryAllocation();
        loadSpinnerandDatePicker();

        database = new MyDatabase(this);

        btnSave.setText(R.string.update);

        fetchOldData();
        setNewData();
    }

    private void setNewData() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidate()){//(name,email,course,gender,contact,birthdate,address,bloodgroup)
                    database.toDeleteOrUpdateData("update " + MyDatabase.TABLE_NAME + " set name = '"+name+"'" +
                            ", email = '"+email+"', course = '"+course+"', gender = '"+gender+"', contact = '"+contact+"'" +
                            ", birthdate = '"+dob+"', address = '"+address+"', bloodgroup = '"+bloodGroup+"';");
                    Toast.makeText(ctx,"Data updated successfully",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(EditData.this,MainActivity.class));
                }
            }
        });
    }

    private boolean isValidate() {
        boolean isValid = true;
        name = etName.getText().toString();
        email = etEmail.getText().toString();
        contact = etContact.getText().toString();
        dob = etDob.getText().toString();
        address = etAddress.getText().toString();
        if(name.length()==0){
            Toast.makeText(ctx,"Please enter the name",Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if(email.length()==0 || !email.matches(emailPattern)){
            Toast.makeText(ctx,"Please enter valid email address",Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if(contact.length()!=10){
            Toast.makeText(ctx,"Please enter 10 digit contact number",Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if(dob.length()==0){
            Toast.makeText(ctx,"Please enter your date of birth",Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if(address.length()==0){
            Toast.makeText(ctx,"Please enter residence address",Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        switch (spnCourse.getSelectedItemPosition()){
            case 0:
                Toast.makeText(ctx,"Please choose your course",Toast.LENGTH_SHORT).show();
                isValid = false;
                break;
            case 1:
                course = (String) spnCourse.getItemAtPosition(1);
                break;
            case 2:
                course = (String) spnCourse.getItemAtPosition(2);
                break;
            case 3:
                course = (String) spnCourse.getItemAtPosition(3);
                break;

        }

        if(spnBloodGroup.getSelectedItemPosition()==0){
            Toast.makeText(ctx,"Please choose your blood group",Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        else {
            for(int i=1; i<bloodGroups.length; i++){
                bloodGroup = (String) spnBloodGroup.getItemAtPosition(i);
            }
        }

        rdgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rdo_male:
                        gender = "Male";
                        break;
                    case R.id.rdo_female:
                        gender = "Female";
                        break;
                }
            }
        });
        return isValid;
    }

    private void fetchOldData() {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("Position");
        _id = Integer.parseInt(id);

        Cursor c = database.toGetData("select * from " + MyDatabase.TABLE_NAME + " where _id = " + _id);
        if(c!=null){
            if(c.moveToFirst()){

                etName.setText(c.getString(c.getColumnIndex("name")));
                etEmail.setText(c.getString(c.getColumnIndex("email")));
                etContact.setText(c.getString(c.getColumnIndex("contact")));
                etDob.setText(c.getString(c.getColumnIndex("birthdate")));
                etAddress.setText(c.getString(c.getColumnIndex("address")));

                if(c.getString(c.getColumnIndex("gender")).equals("Male"))
                    rdoMale.setChecked(true);
                else
                    rdoFemale.setChecked(true);

                if(c.getString(c.getColumnIndex("course")).equals("Computer Science Engineering"))
                    spnCourse.setSelection(1);
                else if(c.getString(c.getColumnIndex("course")).equals("Information Technology Engineering"))
                    spnCourse.setSelection(2);
                else
                    spnCourse.setSelection(3);

                switch (c.getString(c.getColumnIndex("bloodgroup"))) {
                    case "O+":
                        spnBloodGroup.setSelection(1);
                        break;
                    case "A+":
                        spnBloodGroup.setSelection(2);
                        break;
                    case "B+":
                        spnBloodGroup.setSelection(3);
                        break;
                    case "AB+":
                        spnBloodGroup.setSelection(4);
                        break;
                    case "O-":
                        spnBloodGroup.setSelection(5);
                        break;
                    case "A-":
                        spnBloodGroup.setSelection(6);
                        break;
                    case "B-":
                        spnBloodGroup.setSelection(7);
                        break;
                    case "AB-":
                        spnBloodGroup.setSelection(8);
                        break;
                }

            }
        }
    }

    private void loadSpinnerandDatePicker() {
        courses = new String[]{"Choose Your Course", "Computer Science Engineering","Information Technology Engineering","Electronics and Communication Engineering"};
        bloodGroups = new String[]{"Choose Your Blood Group","O+","A+","B+","AB+","O-","A-","B-","AB-"};

        spnCourse.setAdapter(new ArrayAdapter<>(ctx, android.R.layout.simple_spinner_dropdown_item,courses));
        spnBloodGroup.setAdapter(new ArrayAdapter<>(ctx, android.R.layout.simple_spinner_dropdown_item,bloodGroups));

        ibDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GregorianCalendar gc = new GregorianCalendar();
                DatePickerDialog dpd = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        etDob.setText(d + "/" + (m+1) + "/" + y);
                    }
                }, gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
    }
    private void memoryAllocation() {
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etContact = findViewById(R.id.et_contact);
        etDob = findViewById(R.id.et_dob);
        etAddress = findViewById(R.id.et_address);
        spnCourse = findViewById(R.id.spn_course);
        spnBloodGroup = findViewById(R.id.spn_blood_grp);
        rdgGender = findViewById(R.id.rdg_gender);
        rdoMale = findViewById(R.id.rdo_male);
        rdoFemale = findViewById(R.id.rdo_female);
        ibDob = findViewById(R.id.ib_dob);
        btnSave = findViewById(R.id.btn_save);
    }
}
