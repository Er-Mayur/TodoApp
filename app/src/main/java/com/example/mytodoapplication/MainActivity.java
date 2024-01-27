package com.example.mytodoapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button btAdd,btReset;
    RecyclerView recyclerView;

    List<MainData> dataList=new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RoomDB database;

    MainAdapter mainAdapter;

    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.edit_text);
        btAdd=findViewById(R.id.bt_add);
        btReset=findViewById(R.id.bt_reset);
        recyclerView=findViewById(R.id.recycler_view);

        //init database

        database=RoomDB.getInstance(this);
        //store db value in datalist

        dataList=database.mainDao().getAll();

        //init linear layout manager

        linearLayoutManager =new LinearLayoutManager(this);

        //set layout manager

        recyclerView.setLayoutManager(linearLayoutManager);
        //init adapter

        mainAdapter=new MainAdapter(dataList,MainActivity.this);
        //set adapter

        recyclerView.setAdapter(mainAdapter);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get string from edit text

                String sText=editText.getText().toString().trim();
                if(!sText.equals("")){
                    //when text is null
                    //init main data
                    MainData data=new MainData();
                    //set text on main data
                    data.setText(sText);
                    //insert text in databse
                    database.mainDao().insert(data);

                    // clear

                    editText.setText("");
                    //notify when data is inserted

                    dataList.clear();
                    Toast.makeText(MainActivity.this,"Successfully added!",Toast.LENGTH_LONG).show();

                    dataList.addAll(database.mainDao().getAll());
                    mainAdapter.notifyDataSetChanged();

                }else{
                    builder= new AlertDialog.Builder(MainActivity.this);
                    //Setting message manually and performing action on button click
                    builder.setMessage("The text field must not be empty!!")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("InvalidActionAlert");
                    alert.show();
                }
            }
        });
        btReset.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               builder= new AlertDialog.Builder(v.getContext());
               //Setting message manually and performing action on button click
               builder.setMessage("Are you sure you want to delete all your todos?")
                       .setCancelable(false)
                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {

                               //delete all data from database
                               database.mainDao().reset(dataList);
                               //notify when all data deteled
                               dataList.clear();
                               dataList.addAll(database.mainDao().getAll());
                               mainAdapter.notifyDataSetChanged();
                           }
                       })
                       .setNegativeButton("No", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                               //  Action for 'NO' Button
                               dialog.cancel();
                           }
                       });
               //Creating dialog box
               AlertDialog alert = builder.create();
               //Setting the title manually
               alert.setTitle("ResetConfirmation");
               alert.show();

           }
        });
    }
}