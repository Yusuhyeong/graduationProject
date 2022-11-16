package com.example.gp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_match_sub extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private MatchAPI MatchAPI;
    private Map<String, Object> back = new HashMap<>();

    TextView nicknameText;
    TextView ageText;
    TextView genreText;

    TextView titletext;
    TextView singertext;

    Button acceptbtn;
    Button refusebtn;


    String myuid;
    String mynickname = "INITIAL";
    String friendname;
    String friendage;
    String friendgenre;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_sub);
        Intent preintent = getIntent();
        nicknameText = (TextView) findViewById(R.id.test_text);

        singertext = (TextView) findViewById(R.id.friend_singer);
        titletext = (TextView) findViewById(R.id.friend_song);

        acceptbtn = (Button) findViewById(R.id.btn_accept);
        refusebtn = (Button) findViewById(R.id.btn_refuse);

        myuid = FirebaseAuth.getInstance().getUid();


        DocumentReference documentReference1 = db.collection(myuid).document("uinfo");
        documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Map<String, Object> test;
                    test = documentSnapshot.getData();

                    mynickname = test.get("nickname").toString();
                    System.out.println("see my uid--------------------" + myuid);
                    System.out.println("see my nick--------------------" + mynickname);

                    MatchResponse();
                    MatchUserResponse();
                }
            }
        });

        System.out.println("---------nick------------" + mynickname);

        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcceptResponse();
                MatchUserResponse();
            }
        });

        refusebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RefuseResponse();
                MatchUserResponse();
            }
        });
    }

    public void MatchResponse() {
        MatchRequest match_request = new MatchRequest(mynickname);
        retrofitClient = RetrofitClient.getInstance();
        MatchAPI = RetrofitClient.getRetrofitInterface();

        MatchAPI.getMatchResponse(match_request).enqueue(new Callback<MatchResponse>() {
            @Override
            public void onResponse(Call<MatchResponse> call, Response<MatchResponse> response) {
                Log.d("retrofit", "Data fetch success");
            }

            @Override
            public void onFailure(Call<MatchResponse> call, Throwable t) {
                Log.d("retrofit", "failure");
            }
        });
    }

    public void MatchUserResponse(){// 매치 큐에서 한명 fuid 받아오기

        MatchUserRequest match_user_request = new MatchUserRequest(mynickname);
        retrofitClient = RetrofitClient.getInstance();
        MatchAPI = RetrofitClient.getRetrofitInterface();

        System.out.println("In MatchUserResponse------------" + mynickname);

        MatchAPI.getMatchUserResponse(match_user_request).enqueue(new Callback<MatchUserResponse>(){
              @Override
              public void onResponse(Call<MatchUserResponse> call, Response<MatchUserResponse> response){
                  Log.d("retrofit", "Data fetch success");
                  MatchUserResponse result = response.body();
                  // 매칭된 사람 uid를 불러오기
                  String nickname2 = result.getfuid();
                  System.out.println("int get friend nick------------" + nickname2);

                  DocumentReference documentReference2 = db.collection(nickname2).document("uinfo");
                  documentReference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                      @Override
                      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                          if(task.isSuccessful()){
                              DocumentSnapshot documentSnapshot = task.getResult();
                              Map<String, Object> test;
                              test = documentSnapshot.getData();

                              friendname = test.get("nickname").toString();

                              DocumentReference documentReference3 = db.collection(nickname2).document("sinfo");
                              documentReference3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                  @Override
                                  public void onComplete(@NonNull Task<DocumentSnapshot> task2){
                                      if(task.isSuccessful()){
                                          DocumentSnapshot documentSnapshot1 = task2.getResult();
                                          ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);
                                          Map<String, Object> sinfos;
                                          ArrayList<String> songs, singers, images;
                                          Bitmap bitmap;

                                          String tsong, tsinger, key, url1, url2;

                                          sinfos = documentSnapshot1.getData();
                                          System.out.println("-----------------------nicktest_img---------" + nickname2);
                                          System.out.println("-----------------------nicktest_img---------" + sinfos);
                                          songs = (ArrayList<String>) sinfos.get("title");
                                          singers = (ArrayList<String>) sinfos.get("singer");

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

                                          System.out.println("-----------------------nicktest_img---------" + nickname2);
                                          System.out.println("-----------------------imgtest---------" + (String)images.get(0));

                                          Glide.with(Activity_match_sub.this).load(images.get(0)).into(imageView3);
                                      }
                                  }
                              });

                              nicknameText.setText(friendname);
                          }
                      }
                  });

                  ageText = (TextView) findViewById(R.id.fage_text);

                  DocumentReference documentReference3 = db.collection(nickname2).document("uinfo");
                  documentReference3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                      @Override
                      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                          if (task.isSuccessful()) {
                              DocumentSnapshot documentSnapshot = task.getResult();
                              Map<String, Object> test;
                              test = documentSnapshot.getData();

                              friendage = test.get("age").toString();
                              ageText.setText(test.get("age").toString());
                          }
                      }
                  });

                  genreText = (TextView) findViewById(R.id.fgenre_text);

                  DocumentReference documentReference4 = db.collection(nickname2).document("uinfo");
                  documentReference4.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                      @Override
                      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                          if (task.isSuccessful()) {
                              DocumentSnapshot documentSnapshot = task.getResult();
                              Map<String, Object> test;
                              test = documentSnapshot.getData();

                              friendgenre = test.get("genre").toString();
                              genreText.setText(test.get("genre").toString());
                          }
                      }
                  });

                  DocumentReference documentReference5 = db.collection(nickname2).document("sinfo");
                  documentReference5.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                      @Override

                      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                          if(task.isSuccessful()){
                              DocumentSnapshot documentSnapshot4 = task.getResult();
                              Map<String, Object> test;
                              ArrayList<String> songs, singers;

                              test = documentSnapshot4.getData();
                              songs = (ArrayList<String>) test.get("title");
                              singers = (ArrayList<String>) test.get("singer");

                              String str = "";
                              for(int i = 0; i<songs.size(); i++){
                                  str = str.concat(String.valueOf(songs.get(i))+ "\n\n");
                              }
                              System.out.println("-----------------------str_test------------" + str);
                              titletext.setText(str);


                              String str2 = "";
                              for(int i = 0; i<singers.size(); i++){
                                  str2 = str2.concat(String.valueOf(singers.get(i))+ "\n\n");
                              }
                              System.out.println("-----------------------str2_test------------" + str2);

                              singertext.setText(str2);
                          }
                      }
                  });

              }
              @Override
              public void onFailure(Call<MatchUserResponse> call, Throwable t){
                  Log.d("retrofit", "failure");
              }
        });
    }

    public void AcceptResponse() { // 누르면 채팅방 만들기
        System.out.println("see mnick fnick-------------" + mynickname + friendname);
        AcceptRequest accept_request = new AcceptRequest(mynickname, friendname);
        retrofitClient = RetrofitClient.getInstance();
        MatchAPI = RetrofitClient.getRetrofitInterface();

        MatchAPI.getAcceptResponse(accept_request).enqueue(new Callback<AcceptResponse>(){
            @Override
            public void onResponse(Call<AcceptResponse> call, Response<AcceptResponse> response){
                Log.d("retrofit", "Data fetch success");
            }
            @Override
            public void onFailure(Call<AcceptResponse> call, Throwable t){
                Log.d("retrofit", "failure");
            }
        });
    }

    public void RefuseResponse() { // 거절
        System.out.println("see mnick fnick-------------" + mynickname + friendname);
        RefuseRequest refuse_request = new RefuseRequest(mynickname, friendname);
        retrofitClient = RetrofitClient.getInstance();
        MatchAPI = RetrofitClient.getRetrofitInterface();
        MatchAPI.getRefuseResponse(refuse_request).enqueue(new Callback<RefuseResponse>(){
            @Override
            public void onResponse(Call<RefuseResponse> call, Response<RefuseResponse> response){
                Log.d("retrofit", "Data fetch success");
            }
            @Override
            public void onFailure(Call<RefuseResponse> call, Throwable t){
                Log.d("retrofit", "failure");

            }
        });
    }
}