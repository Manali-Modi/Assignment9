package com.example.assignment9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recData;
    FloatingActionButton fabAdd;
    ArrayList<Data> studentList;
    MyDatabase database;
    String id, name, email, contact, dob, address, course, bloodGroup, gender;

    DataAdapter dataAdapter;
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recData = findViewById(R.id.rec_data);
        fabAdd = findViewById(R.id.fab_add);
        studentList = new ArrayList<>();
        database = new MyDatabase(this);

        getStudentData();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,StudentForm.class));
            }
        });
    }

    private void getStudentData() {
        Cursor c = database.toGetData("select * from " + MyDatabase.TABLE_NAME + " order by _id desc");
        if(c!=null){
            int count = c.getCount();
            if(count == 0){
                Toast.makeText(ctx,"No data inserted",Toast.LENGTH_SHORT).show();
            }
            else {
                while (c.moveToNext()){
                    id = String.valueOf(c.getInt(c.getColumnIndex("_id")));
                    name = c.getString(c.getColumnIndex("name"));
                    email = c.getString(c.getColumnIndex("email"));
                    course = c.getString(c.getColumnIndex("course"));
                    gender = c.getString(c.getColumnIndex("gender"));
                    contact = c.getString(c.getColumnIndex("contact"));
                    dob = c.getString(c.getColumnIndex("birthdate"));
                    address = c.getString(c.getColumnIndex("address"));
                    bloodGroup = c.getString(c.getColumnIndex("bloodgroup"));

                    Data data = new Data(id,name,email,contact,dob,address,course,bloodGroup,gender);
                    studentList.add(data);
                }
                dataAdapter = new DataAdapter(ctx,studentList);
                recData.setLayoutManager(new LinearLayoutManager(ctx));
                recData.setAdapter(dataAdapter);
            }
        }
    }

}