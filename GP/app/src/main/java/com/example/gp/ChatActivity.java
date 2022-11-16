package com.example.gp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private ListView chating;
    private EditText et_send;
    private Button btn_send;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> arr_room = new ArrayList<>();

    private String str_room_name;
    private String str_user_name;

    private DatabaseReference reference;
    private FirebaseFirestore firebaseFirestore;
    private String key;
    private String chat_user;
    private String chat_message;

    private String uid;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        str_user_name = intent.getStringExtra("nickname");

        et_send = (EditText) findViewById(R.id.et_send);
        chating = (ListView) findViewById(R.id.chat);
        btn_send = (Button) findViewById(R.id.btn_send);

        str_room_name = intent.getStringExtra("room_name");


        reference = FirebaseDatabase.getInstance().getReference().child(str_room_name);

        setTitle(str_room_name + " 채팅방");

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.memolist_type, arr_room);
        chating.setAdapter(arrayAdapter);
        chating.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 버튼 클릭시 채팅이 realtime database에 추가됩니다.

        uid = FirebaseAuth.getInstance().getUid();
        DocumentReference documentReference3 = db.collection(uid).document("uinfo");
        documentReference3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Map<String, Object> test;
                    test = documentSnapshot.getData();

                    str_user_name = test.get("nickname").toString();
                    System.out.println("--------------------chat_uidtest-----------" + str_user_name);
                }
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View view) {

                // 채팅마다 고유한 키가 생기도록(채팅을 구별할 수 있게) leaf를 push한 뒤 키를 받습니다.

                Map<String, Object> map = new HashMap<String, Object>();
                key = reference.push().getKey();
                reference.updateChildren(map);

                // 이후 키를 경로로 설정합니다.

                DatabaseReference root = reference.child(key);

                // 사용자 닉네임과 메세지가 함께 뜨도록 Map형태로 db에 입력합니다.
                // 현재 defalut user name은 uid입니다. 변경 필요시 firestore에 접근하여 수정하면 됩니다.

                System.out.println("--------------------chat_test-----------" + str_user_name);
                Map<String, Object> objectMap = new HashMap<String, Object>();
                objectMap.put("name", str_user_name);
                objectMap.put("message", et_send.getText().toString());

                root.updateChildren(objectMap);

                et_send.setText("");
            }
        });

        // 아래는 realtime database에 변화를 감지하는 함수들입니다.
        // 현재 reference pointer는 root->채팅방이름 에 위치해있기 때문에, 현재 접속한 채팅방에 있는 데이터 변화만 감지합니다.

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Child는 메세지입니다. 메세지 추가시 아래 함수를 수행합니다.
                chatConversation(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // 메세지 변경시 아래 함수를 수행합니다.
                chatConversation(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // 변화 감지시 chatConversation함수를 수행합니다.
    // 해당 변화가 dataSnapshot으로 찍히고, Iterator로 child들을 구분합니다.
    // while문을 통해 iterator가 끝날 때가지 채팅 메세지 등을 이어붙여 출력합니다.

    private void chatConversation(DataSnapshot dataSnapshot){
        Iterator i = dataSnapshot.getChildren().iterator();
        while(i.hasNext()){
            chat_message = (String) ((DataSnapshot) i.next()).getValue();
            chat_user = (String) ((DataSnapshot) i.next()).getValue();
            arrayAdapter.add(chat_user + " : " + chat_message);
        }
    }
}
