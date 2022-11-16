package com.example.gp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_signup extends AppCompatActivity {
    TextView back;
    EditText name,pw,pw2,email,nickname,age, genre;
    Button pwcheck, submit;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final String[] genre_select = {"Classic", "Jazz", "Pop", "Ballad", "R&B", "HipHop",
                "CountryMusic", "Reggae", "KPOP", "Trot", "Dance", "EDM", "RockNRoll"};

        final ArrayAdapter adapter = new ArrayAdapter(this,R.layout.simple_list_item_multiple_choice, genre_select);
        final ListView listview = (ListView)findViewById(R.id.choose_genre_list);
        listview.setAdapter(adapter);

        //뒤로 가기 버튼
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed() );

        //기입 항목
        email = (EditText) findViewById(R.id.email);
        nickname = (EditText) findViewById(R.id.nickname);
        pw= (EditText)findViewById(R.id.signPW);
        pw2= (EditText)findViewById(R.id.signPW2);
        age = (EditText) findViewById(R.id.age);

        email.setText("user@1.1");
        pw.setText("qwe123");
        pw2.setText("qwe123");
        age.setText("11");
        nickname.setText("user");

        firebaseAuth = FirebaseAuth.getInstance();

        //비밀번호 확인 버튼
        pwcheck = findViewById(R.id.pwcheckbutton);
        pwcheck.setOnClickListener(v -> {
            if(pw.getText().toString().equals(pw2.getText().toString())){
                pwcheck.setText("일치");
            }else{
                Toast.makeText(Activity_signup.this, "비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
            }
        });

        submit = (Button) findViewById(R.id.signupbutton);

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String email_r = email.getText().toString().trim();
                final String pwd_r = pw.getText().toString().trim();
                final String nickname_r = nickname.getText().toString().trim();
                final String age_r = age.getText().toString().trim();
                ArrayList<String> genre_r = new ArrayList<>();

                SparseBooleanArray checkedItems = listview.getCheckedItemPositions(); //체크박스로 체크한 셀의 정보를 담고 있는 희소 논리 배열 얻어오기
                int count = adapter.getCount(); //전체 몇개인지 세기

                if(checkedItems.size()!=0) {
                    for (int i = count - 1; i >= 0; i--) {
                        if (checkedItems.get(i)) { //희소 논리 배열의 해당 인덱스가 선택되어 있다면
                            genre_r.add(genre_select[i]); //arrayList에 추가하기
                        }
                    }
                }

                if(TextUtils.isEmpty(email_r) || TextUtils.isEmpty(pwd_r) || TextUtils.isEmpty(nickname_r)){
                    Toast toast = Toast.makeText(getApplicationContext(), "정보를 바르게 입력하세요", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                // 해당 부분은 firebaseauth의 기본 사용방법에 의거하여 회원가입을 진행한 내용입니다.

                firebaseAuth.createUserWithEmailAndPassword(email_r, pwd_r).addOnCompleteListener(Activity_signup.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            // Map형태로 사용자 정보를 추가 저장합니다.
                            // 회원가입할 때 입력한 이메일, 비밀번호, 닉네임, 나이 등을 저장합니다.
                            // 다른 정보를 추가로 기입하고 싶다면, xml에 입력칸을 더 추가하고 이 맵에 추가적인 정보를 push 하면 됩니다.

                            Map<String, Object> user = new HashMap<>();
                            user.put("email", email_r);
                            user.put("pw", pwd_r);
                            user.put("nickname", nickname_r);
                            user.put("age", age_r);
                            user.put("genre", genre_r);

                            // 해당 사용자의 db에 사용자 정보를 추가해야 하기 때문에, uid를 받아옵니다.

                            String uid = FirebaseAuth.getInstance().getUid().toString();

                            // uid를 받아와 db 컬렉션에 접근하고, uinfo document를 만든 뒤 해당 document에 map을 저장합니다.

                            firebaseFirestore
                                    .collection(uid)
                                    .document("uinfo")
                                    .set(user);

                            // 처음 데이터를 저장할 때 finfo(친구목록), cinfo(채팅방 목록), sinfo(노래 목록)을 추가로 만듭니다.
                            // finfo/cinfo 1차 db 구현 : 2022. 09. 05.
                            // finfo/cinfo 2차 db 구현 : 2022. 09. 12.
                            // finfo/cinfo db 마무리 : 2022. 09. 16.

                            // sinfo에 대한 구조 및 기능 정리 필요
                            // 2022. 09. 15. 회의로 기능 구체화
                            // 2022. 09. 19 ~ 22 작업 예정

                            Map<String, Object> finfo = new HashMap<>();
                            user.put("initial", "0");
                            Map<String, Object> cinfo = new HashMap<>();
                            user.put("initial", "0");
                            Map<String, Object> sinfo = new HashMap<>();
                            user.put("initial", "0");

                            // finfo를 실제로 db에 추가합니다.

                            firebaseFirestore
                                    .collection(uid)
                                    .document("finfo")
                                    .set(finfo);

                            // cinfo를 실제로 db에 추가합니다.

                            firebaseFirestore
                                    .collection(uid)
                                    .document("cinfo")
                                    .set(cinfo);

                            firebaseFirestore
                                    .collection(uid)
                                    .document("sinfo")
                                    .set(sinfo);

                            // 해당 과정이 마무리되었다면 다시 로그인 액티비티로 되돌아갑니다.

                            Map<String, Object> rootuid = new HashMap<>();
                            final String rootnick = nickname_r;
                            rootuid.put(rootnick, uid);

                            firebaseFirestore
                                    .collection("root")
                                    .document("UserList")
                                    .update(rootuid);


                            Intent intent = new Intent(Activity_signup.this, Activity_login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Activity_signup.this, "[ERROR] Registration Error", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });
    }
}