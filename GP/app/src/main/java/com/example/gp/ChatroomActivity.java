package com.example.gp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.HashMap;
import java.util.Map;

public class ChatroomActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String uid = firebaseAuth.getUid();

    private ArrayAdapter<Object> arrayAdapter;
    private ArrayList<Object> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        ListView listView = (ListView) findViewById(R.id.list);

        arrayAdapter = new ArrayAdapter<Object>(this, R.layout.memolist_type_room, arrayList);
        listView.setAdapter(arrayAdapter);

        DocumentReference docref = db.collection(uid).document("cinfo");
        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    //if(document.exists()) Log.d(TAG, document.getData().toString());
                    //else Log.d(TAG, "No such document");
                    Map<String, Object> test = new HashMap<>();
                    test = document.getData();

                    arrayList.addAll(test.values());

                    // 채팅방 정보 제대로 출력되는지 확인
                    // 2022. 09. 15. 23:48
                    for(int i = 0; i < arrayList.size(); i++){
                        System.out.println("----------------------------" + arrayList.get(i).toString());
                    }

                    // 채팅방 목록을 표시
                    // 채팅방 이름을 표시합니다.
                    // 2022. 09. 15. 23:48
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });

        // 채팅방 이동
        // 현재 채팅방 개설 버튼이 사라진 관계로, 채팅방을 realtime database에서 직접 데이터 추가해주셔야 합니다.
        // 즉, 매칭에서 fuid를 넣고 임의로 cname을 넣으셔서 채팅방을 개설하셨다면
        // realtime database에서 cname과 같은 데이터를 입력하시면 완료됩니다.
        // 또는, ChatroomActivity_backup에서 버튼을 다시 살리시면 채팅방 개설을 클라이언트에서 수행할 수 있습니다.
        // 2022. 09. 15. 23:51 재확인
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), com.example.gp.ChatActivity.class);
                intent.putExtra("room_name", ((TextView) view).getText().toString());
                startActivity(intent);
            }
        });

    }
}
