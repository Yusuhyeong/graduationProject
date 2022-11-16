package com.example.gp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Activity_uinfo extends AppCompatActivity {
    EditText enick;
    EditText eemail;
    EditText eage;
    EditText epw;
    EditText egenre;

    Button bnick;
    Button bemail;
    Button bage;
    Button bpw;
    Button bgenre;

    String snick;
    String semail;
    String sage;
    String spw;
    String sgenre;

    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;

    String storenick;
    String storeemail;
    String storeyear;
    String storegenre;


    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    String TAG = "Activity_test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uinfo);

        enick = (EditText) findViewById(R.id.editTextTextPersonName);
        eemail = (EditText) findViewById(R.id.editTextTextPersonName3);
        eage = (EditText) findViewById(R.id.editTextTextPersonName4);
        epw = (EditText) findViewById(R.id.editTextTextPersonName5);
        egenre = (EditText) findViewById(R.id.editTextTextPersonName6);

        bnick = (Button) findViewById(R.id.button3);
        bemail = (Button) findViewById(R.id.button8);
        bage = (Button) findViewById(R.id.button9);
        bpw = (Button) findViewById(R.id.button10);
        bgenre = (Button) findViewById(R.id.button11);

        textView1 = (TextView) findViewById(R.id.store_nick);
        textView2 = (TextView) findViewById(R.id.store_email);
        textView3 = (TextView) findViewById(R.id.store_year);
        textView4 = (TextView) findViewById(R.id.store_genre);


        String uid = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection(uid).document("uinfo");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Map<String, Object> test;
                    test = documentSnapshot.getData();

                    storenick = test.get("nickname").toString();
                    textView1.setText(test.get("nickname").toString());

                    storeemail = test.get("email").toString();
                    textView2.setText(test.get("email").toString());

                    storeyear = test.get("age").toString();
                    textView3.setText(test.get("age").toString());

                    storegenre = test.get("genre").toString();
                    textView4.setText(test.get("genre").toString());
                }
            }
        });



        // 이하 회원정보수정메뉴
        bnick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snick = enick.getText().toString();
                System.out.println(snick);

                Map<String,Object> cnick = new HashMap<>();
                cnick.put("nickname", snick);

                firebaseFirestore.collection(uid)
                        .document("uinfo")
                        .update(cnick);
                Toast.makeText(Activity_uinfo.this, "닉네임이 변경되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        bemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semail = eemail.getText().toString();
                System.out.println(semail);

                Map<String,Object> cemail = new HashMap<>();
                cemail.put("email", semail);

                firebaseFirestore.collection(uid)
                        .document("uinfo")
                        .update(cemail);
                Toast.makeText(Activity_uinfo.this, "이메일이 변경되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        bage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sage = eage.getText().toString();
                System.out.println(sage);

                Map<String,Object> cage = new HashMap<>();
                cage.put("age", sage);

                firebaseFirestore.collection(uid)
                        .document("uinfo")
                        .update(cage);
                Toast.makeText(Activity_uinfo.this, "나이가 변경되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        bpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spw = epw.getText().toString();
                System.out.println(spw);

                Map<String,Object> cpw = new HashMap<>();
                cpw.put("pw", spw);

                firebaseFirestore.collection(uid)
                        .document("uinfo")
                        .update(cpw);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.updatePassword(spw)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) Log.d(TAG, "PASSWORD CHANGED");
                            }
                        });
                Toast.makeText(Activity_uinfo.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        bgenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sgenre = egenre.getText().toString();
                System.out.println(sgenre);

                Map<String,Object> cgenre = new HashMap<>();
                cgenre.put("genre", sgenre);

                firebaseFirestore.collection(uid)
                        .document("uinfo")
                        .update(cgenre);
                Toast.makeText(Activity_uinfo.this, "장르가 변경되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}