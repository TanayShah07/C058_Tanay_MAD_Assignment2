package com.shah.firebasetodoapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddEditTaskActivity extends AppCompatActivity {

    private EditText descriptionEditText;
    private RadioGroup priorityRadioGroup;
    private Button saveButton;
    private DatabaseReference databaseReference;
    private String taskId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        descriptionEditText = findViewById(R.id.edit_text_description);
        priorityRadioGroup = findViewById(R.id.radio_group_priority);
        saveButton = findViewById(R.id.button_save);

        databaseReference = FirebaseDatabase.getInstance().getReference("tasks");

        if (getIntent().hasExtra("TASK_ID")) {
            taskId = getIntent().getStringExtra("TASK_ID");
            String description = getIntent().getStringExtra("TASK_DESCRIPTION");
            String priority = getIntent().getStringExtra("TASK_PRIORITY");

            descriptionEditText.setText(description);

            if (priority.equals("High")) {
                priorityRadioGroup.check(R.id.radio_high);
            } else if (priority.equals("Medium")) {
                priorityRadioGroup.check(R.id.radio_medium);
            } else {
                priorityRadioGroup.check(R.id.radio_low);
            }
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
    }

    private void saveTask() {
        String description = descriptionEditText.getText().toString().trim();
        if (description.isEmpty()) {
            Toast.makeText(this, "Please enter a task description.", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = priorityRadioGroup.getCheckedRadioButtonId();
        String priority;
        if (selectedId == R.id.radio_high) {
            priority = "High";
        } else if (selectedId == R.id.radio_medium) {
            priority = "Medium";
        } else {
            priority = "Low";
        }

        if (taskId == null) {
            taskId = databaseReference.push().getKey();
            Task task = new Task(taskId, description, priority);
            databaseReference.child(taskId).setValue(task);
            Toast.makeText(this, "Task saved successfully!", Toast.LENGTH_SHORT).show();
        } else {
            databaseReference.child(taskId).child("description").setValue(description);
            databaseReference.child(taskId).child("priority").setValue(priority);
            Toast.makeText(this, "Task updated successfully!", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}