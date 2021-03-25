package com.example.assignment9;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    Context ctx;
    ArrayList<Data> studentList;

    public DataAdapter(Context ctx, ArrayList<Data> studentList) {
        this.ctx = ctx;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(ctx).inflate(R.layout.data_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Data data = studentList.get(position);
        holder.txtName.setText(data.getName());
        holder.txtCourse.setText(data.getCourse());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtCourse;
        ImageView imgMenu;
        MyDatabase database;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtCourse = itemView.findViewById(R.id.txt_course);
            imgMenu = itemView.findViewById(R.id.img_menu);
            database = new MyDatabase(ctx);

            imgMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int dataId = Integer.parseInt(studentList.get(getAdapterPosition()).getId());
                    PopupMenu popupMenu = new PopupMenu(ctx,view);
                    popupMenu.getMenuInflater().inflate(R.menu.my_menu,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @SuppressLint("NonConstantResourceId")
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.mnu_edit:
                                    Toast.makeText(ctx,"Edit",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ctx,EditData.class);
                                    intent.putExtra("Position",studentList.get(getAdapterPosition()).getId());
                                    ctx.startActivity(intent);
                                    break;
                                case R.id.mnu_delete:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
                                            .setTitle("Are you sure to delete this record?")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    database.toDeleteOrUpdateData("delete from " + MyDatabase.TABLE_NAME + " where _id = " + dataId);
                                                    studentList.remove(getAdapterPosition());
                                                    notifyDataSetChanged();
                                                    Toast.makeText(ctx,"Deleted Successfully",Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            });
                                    builder.create().show();
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }
    }
}