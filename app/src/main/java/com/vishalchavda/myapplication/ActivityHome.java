package com.vishalchavda.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vishalchavda.myapplication.Adapters.Taskshow_adapter;
import com.vishalchavda.myapplication.Models.ModelSign_up;
import com.vishalchavda.myapplication.Models.Taskmodel;
import com.vishalchavda.myapplication.databinding.ActivityHomeBinding;

import java.util.ArrayList;

public class ActivityHome extends AppCompatActivity {
ActivityHomeBinding binding;
FirebaseDatabase database;
DatabaseReference reference;
FirebaseAuth auth;
String userId;
ArrayList<Taskmodel> list;
Taskshow_adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth =FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        list = new ArrayList<>();
        adapter = new Taskshow_adapter(list,ActivityHome.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivityHome.this);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setHasFixedSize(true);



        database.getReference().child("task").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Taskmodel taskmodel = snapshot1.getValue(Taskmodel.class);
                    list.add(taskmodel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("task").child(userId);

        database.getReference().child("users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelSign_up model = snapshot.getValue(ModelSign_up.class);

                binding.tvUsername.setText(model.getUserName());
                binding.tvemail.setText(model.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });

    }

    private void addTask() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        View view = LayoutInflater.from(this).inflate(R.layout.input_task,null);
        dialog.setView(view);

        AlertDialog mydialog = dialog.create();
        mydialog.setCancelable(false);
        mydialog.show();

        final EditText task = view.findViewById(R.id.task);
        Button cancel = view.findViewById(R.id.delete);
        Button save = view.findViewById(R.id.save);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mydialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(ActivityHome.this);
                progressDialog.setTitle("adding");
                progressDialog.show();
                String addtask = task.getText().toString();
                String id = reference.push().getKey();

                if (addtask.isEmpty()){
                    task.setError("Please Enter task");
                    return;
                }

                Taskmodel taskmodel = new Taskmodel(task.getText().toString(),id);
                reference.child(id).setValue(taskmodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "task add sucsseccfuly ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                mydialog.dismiss();
            }
        });

    }
}