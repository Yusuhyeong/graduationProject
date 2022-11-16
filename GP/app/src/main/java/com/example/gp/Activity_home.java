package com.example.gp;

import static androidx.constraintlayout.widget.StateSet.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Activity_home extends AppCompatActivity {
    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView titletext;
    TextView singertext;

    ImageView imageView;

    String test1;
    String test2;
    String test3;
    String test4;

    String nick1;

    private DrawerLayout drawerLayout; // 상단 메뉴 레이아웃
    private View drawerView; // 상단 메뉴 뷰
    private Button match; // 매칭 화면으로 이동 버튼
    private Button chat1; // 채팅 화면으로 이동 버튼
    private Button check; // 사용자 확인 버튼
    private FirebaseAuth firebaseAuth;

    private Button menu1;
    private Button menu2;
    private Button menu4;
    private Button menu5;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();;

    private String key;
    private String nickname;
    public String value;

    private Map<String, Object> back = new HashMap<>();


    @Override
    // 뒤로가기 x 설정 (로그인을 마친 상황이기 때문에)
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        textView = (TextView) findViewById(R.id.textView12);
        textView2 = (TextView) findViewById(R.id.textView13);
        textView3 = (TextView) findViewById(R.id.textView14);
        textView4 = (TextView) findViewById(R.id.textView15);

        singertext = (TextView) findViewById(R.id.textView18);
        titletext = (TextView) findViewById(R.id.textView19);

        imageView = (ImageView) findViewById(R.id.img_part_user);

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

                    test1 = test.get("genre").toString();
                    String genre_str = test1.replaceAll("[,]", "");
                    textView.setText(genre_str);
                    System.out.println(genre_str);
                    System.out.println("이게 몰까용"+test1.getClass().getName());
                }
            }
        });

        DocumentReference documentReference1 = db.collection(uid).document("uinfo");
        documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Map<String, Object> test;
                    test = documentSnapshot.getData();

                    test2 = test.get("nickname").toString();
                    textView2.setText(test.get("nickname").toString());
                }
            }
        });

        DocumentReference documentReference2 = db.collection(uid).document("uinfo");
        documentReference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Map<String, Object> test;
                    test = documentSnapshot.getData();

                    test3 = test.get("age").toString();
                    textView3.setText(test.get("age").toString());
                }
            }
        });

        DocumentReference documentReference3 = db.collection(uid).document("uinfo");
        documentReference3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Map<String, Object> test;
                    test = documentSnapshot.getData();

                    test4 = test.get("genre").toString();
                    textView4.setText(test.get("genre").toString());
                }
            }
        });

        DocumentReference documentReference4 = db.collection(uid).document("sinfo");
        documentReference4.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override

            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot4 = task.getResult();
                    Map<String, Object> sinfos;
                    ArrayList<String> songs, singers, images;
                    Uri uri;
                    Bitmap bitmap;

                    String tsong, tsinger, key, url1, url2;

                    final String murl;

                    sinfos = documentSnapshot4.getData();
                    songs = (ArrayList<String>) sinfos.get("title");
                    singers = (ArrayList<String>) sinfos.get("singer");

                    if (songs != null || singers != null){
                        String str = "";
                        for(int i = 0; i<songs.size(); i++){
                            str = str.concat(songs.get(i)+ "\n\n");
                        }

                        System.out.println("-----------------------str__home_test------------" + str);

                        titletext.setText(str);

                        String str2 = "";
                        for(int i = 0; i<singers.size(); i++){
                            str2 = str2.concat(String.valueOf(singers.get(i))+ "\n\n");
                        }
                        singertext.setText(str2);

                        tsong = songs.get(0);


                        tsinger = singers.get(0);
                        key = tsong + " " + tsinger;
                        url1 = "http://www.maniadb.com/api/search/";
                        url2 = "/?sr=song&display=10&key=cbnu2017038040@gmail.com&v=.05";

                        maniadbapi maniadbapi = new maniadbapi(url1, url2, key);
                        try {
                            back = maniadbapi.execute().get();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        images = (ArrayList<String>) back.get("image");
                        Glide.with(Activity_home.this).load(images.get(0)).into(imageView);
                    }
                }
            }
        });


        check = (Button) findViewById(R.id.check);


        // 매칭 버튼 터치와 동시에 uid를 받아오고, 해당 정보를 이용해 db에 접근, realtime database wqueue대기열에 합류합니다.

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        db.collection(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + document.getData());
                                value = document.get("nickname").toString();
                                System.out.println(value);  // 원하는 데이터 뽑아오기
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView = (View)findViewById(R.id.drawer);

        Button btn_open = (Button)findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        Button btn_close = (Button)findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });

        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        check.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View view) {
                System.out.println(value);  // 원하는 데이터 뽑아오기
            }
        });

        // ------------------------ 채팅방 액티비티로 이동

        chat1 = (Button) findViewById(R.id.chatbutton);

        chat1.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View view){
                Intent intent = new Intent(Activity_home.this, ChatroomActivity.class);
                intent.putExtra("nickname", test2);
                startActivity(intent);
            }
        });

        // ------------------------ 매칭 액티비티로 이동(클라이언트가 매칭큐로)

        match = findViewById(R.id.matchbutton);

        match.setOnClickListener(v -> {
            /*
            // 매칭 버튼 터치와 동시에 uid를 받아오고, 해당 정보를 이용해 db에 접근, realtime database wqueue대기열에 합류합니다.
            DatabaseReference ref = database.getReference("Wqueue/");
            // 이때, 데이터를 한 단계 집어넣고 매칭큐 키를 생성, 받아옵니다.(다른 매칭 대기열들과 구별하기 위해서)
            // 이후 해당 매칭 대기열을 생성합니다.
            // 트리 형식으로 간단하게 생각하면, root -> wqueue -> key1(user1) -> information1...
            //                                               key2(user2) -> information2...
            //                                               key3(user3) -> information3...
            // 이런 식으로 매칭 대기열이 구성된다고 보면 간단합니다.
            // realtime database의 유저 정보에 접근하려면 key값들을 알아야 합니다.
            // 이러한 키 값을 가져오기 위해서는, wqueue의 child값들을 가져오면 됩니다.
            // 해당 방법은 Activity_match, ChatroomActivity에 있으니 해당 방법을 참고하시면 됩니다.
            // 2022. 09. 16. 12:05
            Map<String, Object> map = new HashMap<String, Object>();
            key = ref.push().getKey();
            ref.updateChildren(map);

            // realtime DB pointer를 자신의 대기열 key 하위로 옮깁니다.
            DatabaseReference root = ref.child(key);

            // 매칭을 위해 필요한 정보는 자신의 uid, 친구가 될 사람의 uid, 채팅방 순서입니다.
            // 클라이언트는 자신의 uid를 realtime database에 기록하여 서버에서 접근할 수 있도록 합니다.
            // 이때, fuid와 cname도 uid로 세팅하도록 두었으니(initial) 서버 운영시 편한 대로 수정하면 됩니다.
            Map<String, Object> objectMap = new HashMap<String, Object>();
            objectMap.put("uid", uid);
            objectMap.put("fuid", uid);
            objectMap.put("cname", uid);

            //root.updateChildren(objectMap);

            // key하위에 해당 정보 세 가지를 세팅합니다.
            root.setValue(objectMap);



            // Firestore에서 데이터 가져오는 법 - Map형태

            // 모든 작업이 완료되었다면 db 대기열 키 값을 넘기고 인텐트를 실행합니다.
             */

            DocumentReference documentReference5 = db.collection(uid).document("uinfo");
            documentReference5.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        Map<String, Object> test;
                        test = documentSnapshot.getData();

                        nick1 = test.get("nickname").toString();
                    }
                }
            });

            Intent intent2 = new Intent(this, Activity_match_sub.class);
            intent2.putExtra("nick1", nick1);
            System.out.println("---------nick------------" + nick1);
            startActivity(intent2);

        });

        menu1 = findViewById(R.id.btn_menu1);

        menu1.setOnClickListener(v -> {
            Intent intent2 = new Intent(this, Activity_uinfo.class);
            startActivity(intent2);
        });

        menu2 = findViewById(R.id.btn_menu2);

        menu2.setOnClickListener(v -> {
            Intent intent2 = new Intent(this, Activity_song.class);
            startActivity(intent2);
        });


        menu4 = findViewById(R.id.btn_menu4);

        menu4.setOnClickListener(v -> {
            Intent intent2 = new Intent(this, Activity_login.class);
            FirebaseAuth.getInstance().signOut();
            startActivity(intent2);
        });

        menu5 = findViewById(R.id.btn_menu5);

        menu5.setOnClickListener(v -> {
            Intent intent3 = new Intent(this, Activity_friends.class);
            startActivity(intent3);
        });

    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };


}