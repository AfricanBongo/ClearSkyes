package com.africanbongo.clearskyes.controller.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.util.FeedbackUtil;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class FeedbackActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private MaterialButton submitButton;
    private EditText fromEditText;
    private EditText subjectEditText;
    private EditText bodyEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        MaterialToolbar toolbar = findViewById(R.id.feedback_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        actionBar.setDisplayHomeAsUpEnabled(true);

        submitButton = findViewById(R.id.feedback_submit);
        fromEditText = findViewById(R.id.feedback_from);
        subjectEditText = findViewById(R.id.feedback_title);
        bodyEditText = findViewById(R.id.feedback_content);

        submitButton.setOnClickListener(this);
        subjectEditText.addTextChangedListener(this);
        bodyEditText.addTextChangedListener(this);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Do nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        canSubmit();
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Do nothing
    }

    /*
    Called by EditTexts to check if they have valid text within them or not
    if required EditTexts are filled then allow feedback submission
     */
    public void canSubmit() {
        if (!bodyEditText.getText().toString().trim().isEmpty()) {
            if (!subjectEditText.getText().toString().trim().isEmpty()) {
                submitButton.setEnabled(true);
                return;
            }
        }

        submitButton.setEnabled(false);
    }

    // Send feedback if the submit button is enabled
    @Override
    public void onClick(View v) {
        String from = fromEditText.getText().toString();
        String subject = subjectEditText.getText().toString();
        String bodyText = bodyEditText.getText().toString();

        if (from.isEmpty() || from.trim().isEmpty()) {
            from = null;
        }

        FeedbackUtil.sendFeedback(this, from, subject, bodyText);
        finish();
    }
}