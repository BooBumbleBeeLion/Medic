package com.example.medic.SignUpScreen;

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
import com.example.medic.SignIn.SignIn;

import org.json.JSONObject;

import java.io.*;
import java.net.*;

public class SignUp extends AppCompatActivity {

    TextView tv, tvReg;
    EditText name, secondName, email, pass, secondPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.editText);
        secondName = findViewById(R.id.editText1);
        email = findViewById(R.id.editText2);
        pass = findViewById(R.id.editText3);
        secondPass = findViewById(R.id.editText4);

        tv = findViewById(R.id.textView3);
        tvReg = findViewById(R.id.textView2);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
            }
        });

        tvReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty() | secondName.getText().toString().isEmpty() | email.getText().toString().isEmpty() | pass.getText().toString().isEmpty() | secondPass.getText().toString().isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    builder.setTitle("Ошибка!")
                            .setMessage("Не все поля заполнены!");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }
                if (!pass.getText().toString().equals(secondPass.getText().toString())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    builder.setTitle("Ошибка!")
                            .setMessage("Введённые пароли не одинаковы!");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }

                if (email.getText().toString().indexOf('@') != -1 && email.getText().toString().indexOf('.') != -1 &&  (email.getText().toString().indexOf('.') - email.getText().toString().indexOf('@')) > 1 &&  email.length() - (email.getText().toString().indexOf('.') + 1) <= 3 && email.length() - (email.getText().toString().indexOf('.') + 1) != 0) {
                    String s = email.getText().toString();
                    char[] c = s.toCharArray();
                    int i = 0;
                    while (c[i] != '@') {
                        if (!Character.isDigit(c[i])) {
                            if (!(Character.isLowerCase(c[i]))) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                builder.setTitle("Ошибка!")
                                        .setMessage("Неверный Email!1");
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                builder.setMessage("Ошибка")
                                        .setMessage("Неверный Email!2");
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                return;
                            }
                        }
                        i++;
                    }


                    i = s.indexOf('.') + 1;
                    while (i != s.length()) {
                        if (Character.isDigit(c[i]) | !Character.isLowerCase(c[i])) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                            builder.setTitle("Ошибка")
                                    .setMessage("Неверный Email!3");
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            return;
                        }
                        i++;
                    }


                    new mytask().execute("http://cinema.areas.su/auth/register", email.getText().toString(), pass.getText().toString(), name.getText().toString(), secondName.getText().toString());

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    builder.setTitle("Ошибка")
                            .setMessage("Неверный Email!4");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }




            }
        });

    }

    public class mytask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String answer = "";
            try{
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                connection.setReadTimeout(10000);
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                //http://cinema.areas.su

                JSONObject obj = new JSONObject();
                obj.put("email", strings[1]);
                obj.put("password", strings[2]);
                obj.put("firstName", strings[3]);
                obj.put("lastName", strings[4]);



                DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                os.writeBytes(obj.toString());
                os.flush();
                os.close();

                InputStream is = connection.getInputStream();
                ByteArrayOutputStream baos  = new ByteArrayOutputStream();
                byte[] b = new byte[5120];
                int readBytes;
                while((readBytes = is.read(b)) != -1)
                {
                    baos.write(b,0,readBytes);
                }

                answer = new String(baos.toByteArray(), "UTF-8");


            }catch (Exception e){answer = e.getMessage();}
            return answer;
        }

        @Override
        protected void onPostExecute(String str) {
            if(str.contains("Успешная регистрация"))
            {
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                builder.setTitle("Произошла ошибка!")
                        .setMessage(str);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            super.onPostExecute(str);
        }
    }


}