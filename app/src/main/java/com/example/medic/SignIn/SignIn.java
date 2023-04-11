package com.example.medic.SignIn;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medic.Main.MainActivity;
import com.example.medic.R;
import com.example.medic.SignUpScreen.SignUp;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignIn extends AppCompatActivity {

    TextView tv, enter;
    EditText email, pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        tv = findViewById(R.id.textView3);
        enter = findViewById(R.id.enter);

        email = findViewById(R.id.editText2);
        pass = findViewById(R.id.editText3);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty() | pass.getText().toString().isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
                    builder.setTitle("Ошибка!")
                            .setMessage("Не все поля заполнены!");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }

                if (email.getText().toString().indexOf('@') != -1 && email.getText().toString().indexOf('.') != -1 && (email.getText().toString().indexOf('.') - email.getText().toString().indexOf('@')) > 1 && email.length() - (email.getText().toString().indexOf('.') + 1) <= 3 && email.length() - (email.getText().toString().indexOf('.') + 1) != 0) {
                    String s = email.getText().toString();
                    char[] c = s.toCharArray();
                    int i = 0;
                    while (c[i] != '@') {
                        if (!Character.isDigit(c[i])) {
                            if (!(Character.isLowerCase(c[i]))) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
                                builder.setTitle("Ошибка!")
                                        .setMessage("Неверный Email!");
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                return;
                            }
                        }
                        i++;
                    }
                    i = s.indexOf('@') + 1;
                    while (i != s.indexOf('.')) {
                        if (!Character.isDigit(c[i])) {
                            if (!Character.isLowerCase(c[i])) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
                                builder.setMessage("Ошибка")
                                        .setMessage("Неверный Email!");
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                        i++;
                    }


                    i = s.indexOf('.') + 1;
                    while (i != s.length()) {
                        if (Character.isDigit(c[i]) | !Character.isLowerCase(c[i])) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
                            builder.setTitle("Ошибка")
                                    .setMessage("Неверный Email!");
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            return;
                        }
                        i++;
                    }


                    new mytask().execute("http://cinema.areas.su/auth/login", email.getText().toString(), pass.getText().toString());

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
                    builder.setTitle("Ошибка")
                            .setMessage("Неверный Email!");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }


            }
        });


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    public class mytask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String answer = "";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                connection.setReadTimeout(10000);
                connection.setRequestProperty("Content-Type", "application/json; utf-8");

                JSONObject obj = new JSONObject();
                obj.put("email", strings[1]);
                obj.put("password", strings[2]);

                DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                os.writeBytes(obj.toString());
                os.flush();
                os.close();

                InputStream is = connection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] b = new byte[5120];
                int readBytes;
                while ((readBytes = is.read(b)) != -1) {
                    baos.write(b, 0, readBytes);
                }

                answer = new String(baos.toByteArray(), "UTF-8");


            } catch (Exception e) {
                answer = e.getMessage();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String str) {
            if (str.contains("token")) {
                Intent intent = new Intent(SignIn.this, MainActivity.class);
                startActivity(intent);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
                builder.setTitle("Произошла ошибка!")
                        .setMessage(str);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            super.onPostExecute(str);
        }
    }

}