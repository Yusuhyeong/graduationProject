package com.example.gp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Activity_login extends AppCompatActivity {
    Button sign; // 회원가입하기

    private Button sign_in; // 로그인
    private EditText email; // 이메일

    // private EditText id; // 아이디
    private EditText pw; // 비밀번호
    FirebaseAuth firebaseAuth; // 파이어베이스 목록 불러오기 데이터

    @Override
    // 뒤로가기 x 설정 (로그아웃을 마친 상황이기 때문에)
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Activity_login 액티비티에서는 단순 firebaseauth를 이용한 로그인을 그대로 실행합니다.
        // 2022. 09. 16. 12:08

        sign_in = (Button) findViewById(R.id.loginbutton);
        email = (EditText) findViewById(R.id.signmail);
        pw = (EditText) findViewById(R.id.ediPassword);
        firebaseAuth = firebaseAuth.getInstance();

        sign_in.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email_s = email.getText().toString().trim();
                String pw_s = pw.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(email_s, pw_s).addOnCompleteListener(Activity_login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(Activity_login.this, Activity_home.class);
                            intent.putExtra("email", email_s);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Activity_login.this, "[ERROR] Login Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        email.setText("asdf@1.1");
        pw.setText("qwe123");


        //회원가입 버튼
        sign = findViewById(R.id.signin);

        //회원가입 버튼 클릭시, 회원가입 페이지로 이동
        sign.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_signup.class);
            startActivity(intent);
        });
    }
}