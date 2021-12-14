package com.vishalchavda.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vishalchavda.myapplication.ActivityHome;
import com.vishalchavda.myapplication.Models.Taskmodel;
import com.vishalchavda.myapplication.R;

import java.util.ArrayList;

public class Taskshow_adapter extends RecyclerView.Adapter<Taskshow_adapter.ViewHolder>{
    ArrayList<Taskmodel> list;
    Context context;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    String userId;
    String key;





    public Taskshow_adapter(ArrayList<Taskmodel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sampletask_show,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Taskmodel model= list.get(position);
        holder.checktask.setText(model.getTask());


        holder.checktask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                String task = model.getTask();
                updateTask();
                return true;
            }
        });
    }

    private void updateTask() {

        database = FirebaseDatabase.getInstance();
        auth =FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("task").child(userId);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.sample_updatetask,null,false);
        dialog.setView(view);

        AlertDialog myDialog = dialog.create();
        myDialog.setCancelable(false);
        myDialog.show();

        final EditText task = view.findViewById(R.id.task);
        Button delete = view.findViewById(R.id.delete);
        Button save = view.findViewById(R.id.save);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String udatetask = task.getText().toString();
                key = reference.getKey();
                Taskmodel model = new Taskmodel(udatetask,key);
                reference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "task update Successfully", Toast.LENGTH_SHORT).show();
                        }    else {
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                myDialog.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    reference.child(key).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(context, " Task deleted ", Toast.LENGTH_SHORT).show();
                            }    else {
                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                myDialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button checktask;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checktask = itemView.findViewById(R.id.checktask);
        }
    }
}

