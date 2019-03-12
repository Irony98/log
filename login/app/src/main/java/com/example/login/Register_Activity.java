package com.example.login;

        import android.content.ContentValues;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.AsyncTask;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.InputFilter;
        import android.text.Spanned;
        import android.view.KeyEvent;
        import android.view.View;
        import android.view.inputmethod.EditorInfo;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

public class Register_Activity extends AppCompatActivity implements View.OnClickListener{
    private EditText edit_register, edit_setpassword, edit_resetpassword;
    private Button btn_yes, btn_cancel;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        init();
        dbHelper = new DBHelper(this, "Data.db", null, 1);
    }


    protected void init() {
        edit_register = (EditText) findViewById(R.id.edit_register);
        edit_register.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        for (int i = start; i < end; i++) {
                            if (!Character.isLetterOrDigit(source.charAt(i)) &&
                                    !Character.toString(source.charAt(i)).equals("_")) {
                                Toast.makeText(Register_Activity.this, "wrong format!", Toast.LENGTH_SHORT).show();
                                return "";
                            }
                        }
                        return null;
                    }
                }
        });
        edit_register.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_register.clearFocus();
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_register.getWindowToken(), 0);
                }
                return false;
            }
        });
        edit_setpassword = (EditText) findViewById(R.id.edit_setpassword);
        edit_setpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String s = v.getText().toString();
                    System.out.println(" v: ****** v :"+ s.length());
                    if (s.length() >= 6) {
                        System.out.println(" ****** s :"+ s.length());
                        edit_setpassword.clearFocus();
                        InputMethodManager imm =
                                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edit_setpassword.getWindowToken(), 0);
                    } else {
                        Toast.makeText(Register_Activity.this, "password should be at least 6 bits!", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
        edit_resetpassword = (EditText) findViewById(R.id.edit_resetpassword);
        edit_resetpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_resetpassword.clearFocus();
                    InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(edit_resetpassword.getWindowToken(), 0);
                }
                return false;
            }
        });
        btn_yes = (Button) findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(this);
        btn_cancel = (Button) findViewById(R.id.btn_cancle);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String reg_username=edit_register.getText().toString();
        String set_password=edit_setpassword.getText().toString();
        String reset_password=edit_resetpassword.getText().toString();
        switch (v.getId()) {
            case R.id.btn_yes:
                if (CheckIsDataAlreadyInDBorNot(reg_username)) {
                    Toast.makeText(this, "this name has been used!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (set_password.trim().equals(reset_password)) {
                        new rigisterUserInfo().execute(reg_username, set_password);

                        Toast.makeText(this, "success！", Toast.LENGTH_SHORT).show();
                        Intent register_intent = new Intent(Register_Activity.this, MainActivity.class);
                        startActivity(register_intent);
                    } else {
                        Toast.makeText(this, "different password！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_cancle:
                Intent login_intent = new Intent(Register_Activity.this, MainActivity.class);
                startActivity(login_intent);
                break;
            default:
                break;
        }




    }
    public boolean CheckIsDataAlreadyInDBorNot(String value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String Query = "Select * from usertable where username =?";
        Cursor cursor = db.rawQuery(Query, new String[]{value});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    class rigisterUserInfo extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            final String username="reg_username";
            final String userpassword="set_password";
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("password", userpassword);
            db.insert("usertable", null, values);
            db.close();

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }
    }

}
