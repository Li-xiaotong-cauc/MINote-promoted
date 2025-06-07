package net.micode.notes.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.micode.notes.R;
import net.micode.notes.data.Notes;
import net.micode.notes.data.Notes.NoteColumns;

public class PasswordVerifyActivity extends Activity {
    private EditText mPasswordEdit;
    private Button mVerifyButton;
    private long mNoteId;
    private String mStoredPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_verify_layout);

        mNoteId = getIntent().getLongExtra("note_id", -1);
        if (mNoteId == -1) {
            finish();
            return;
        }

        // Get stored password
        Cursor cursor = getContentResolver().query(
            Notes.CONTENT_NOTE_URI,
            new String[] { NoteColumns.PASSWORD },
            NoteColumns.ID + "=?",
            new String[] { String.valueOf(mNoteId) },
            null
        );

        if (cursor != null && cursor.moveToFirst()) {
            mStoredPassword = cursor.getString(0);
            cursor.close();
        }

        if (mStoredPassword == null) {
            finish();
            return;
        }

        mPasswordEdit = findViewById(R.id.password_edit);
        mVerifyButton = findViewById(R.id.verify_button);

        mVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mPasswordEdit.getText().toString();

                if (password.isEmpty()) {
                    Toast.makeText(PasswordVerifyActivity.this, R.string.password_empty_hint, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.equals(mStoredPassword)) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(PasswordVerifyActivity.this, R.string.password_incorrect, Toast.LENGTH_SHORT).show();
                    mPasswordEdit.setText("");
                }
            }
        });
    }
} 