// Firebase 데이터 받아오기 예시


package com.example.gp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class Activity_demo extends AppCompatActivity {

    TextView textView;
    TextView textView2;

    String test1;

    ArrayList<String> test2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        textView = (TextView) findViewById(R.id.textview9);
        textView2 = (TextView) findViewById(R.id.textview123);

        String uid = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        DocumentReference documentReference = db.collection(uid).document("sinfo");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Map<String, Object> sinfos;
                    ArrayList<String> songs, singers, images;

                    sinfos = documentSnapshot.getData();
                    songs = (ArrayList<String>) sinfos.get("title");
                    singers = (ArrayList<String>) sinfos.get("singer");

                    String str = "";
                    for(int i = 0; i<songs.size(); i++){
                        str = str.concat(songs.get(i)+ "\n");
                    }
                    textView.setText(str);

                    String str2 = "";
                    for(int i = 0; i<singers.size(); i++){
                        str2 = str2.concat(String.valueOf(singers.get(i))+ "\n");
                    }
                    textView2.setText(str2);
                }
            }
        });
    }
}