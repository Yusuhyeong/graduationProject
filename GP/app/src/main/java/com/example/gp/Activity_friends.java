package com.example.gp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_friends extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String uid = firebaseAuth.getUid();

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        ListView listView = (ListView) findViewById(R.id.list);

        // ArrayList에 있는 데이터를 ListView에 보여주기 위해 arrayadapter를 사용합니다.
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.friends_type, arrayList);
        listView.setAdapter(arrayAdapter);

        DocumentReference docref = db.collection(uid).document("finfo");
        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    //if(document.exists()) Log.d(TAG, document.getData().toString());
                    //else Log.d(TAG, "No such document");

                    // finfo를 추가할 때 map으로 추가했기 때문에 map형태로 받아옵니다.
                    Map<String, Object> test = new HashMap<>();
                    test = document.getData();

                    // Map<String, Object>에서, key는 String, value는 Object이기 때문에 key는 String으로 바로 받아 사용 가능합니다.
                    // 따라서, arrayList에 바로 추가할 수 있습니다.

                    arrayList.addAll(test.keySet());

                    // 친구 목록 정보 제대로 받아와지는지 확인
                    // 확인완료 . 2022.09.15 23:24
                    for(int i = 0; i < arrayList.size(); i++){
                        System.out.println("----------------------------" + arrayList.get(i));
                    }

                    // 친구 목록을 표시
                    // 친구 uid를 표시합니다.
                    // 친구 uid를 알게 되면, 해당 uid로 firestore의 닉네임 등 회원정보 접근이 가능합니다.
                    // 2022. 09. 15. 23.34
                    // 해당 arrayList, arrayAdapter에 button을 추가해 새 창을 띄우거나 하는 방식으로 상대 프로필 보기 등 추가하면 될 것으로 예상합니다.
                    arrayAdapter.notifyDataSetChanged();

               }
            }
        });

    }
}


// 친구 목록 액티비티 DB작업 1차 완료. 2022. 09. 15
