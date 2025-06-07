package net.micode.notes.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.micode.notes.R;
import net.micode.notes.data.Notes;
import net.micode.notes.data.Notes.NoteColumns;

public class PasswordActivity extends Activity {
    private EditText mPasswordEdit;
    private EditText mConfirmPasswordEdit;
    private Button mSubmitButton;
    private long mNoteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_layout);

        mNoteId = getIntent().getLongExtra("note_id", -1);
        if (mNoteId == -1) {
            finish();
            return;
        }

        mPasswordEdit = findViewById(R.id.password_edit);
        mConfirmPasswordEdit = findViewById(R.id.confirm_password_edit);
        mSubmitButton = findViewById(R.id.submit_button);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mPasswordEdit.getText().toString();
                String confirmPassword = mConfirmPasswordEdit.getText().toString();

                if (password.isEmpty()) {
                    Toast.makeText(PasswordActivity.this, R.string.password_empty_hint, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(PasswordActivity.this, R.string.password_not_match, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save password to database
                ContentValues values = new ContentValues();
                values.put(NoteColumns.PASSWORD, password);
                values.put(NoteColumns.IS_ENCRYPTED, 1);
                getContentResolver().update(
                    Notes.CONTENT_NOTE_URI,
                    values,
                    NoteColumns.ID + "=?",
                    new String[] { String.valueOf(mNoteId) }
                );

                setResult(RESULT_OK);
                finish();
            }
        });
    }
} 