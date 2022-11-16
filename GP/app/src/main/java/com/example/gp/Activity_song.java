package com.example.gp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_song extends AppCompatActivity {
    ImageView imageView;
    Button button;
    String TAG = "Activity_Ocr";

    RecyclerView recyclerView;
    Readapter readapter;

    private Button button2;
    private EditText et_send2;
    private String sword;

    private Map<String, Object> back = new HashMap<>();
    private ArrayList<String> titles;
    private ArrayList<String> singers;
    private ArrayList<String> images;

    public static Context context;

    ArrayList<String> storetitle = new ArrayList<>();
    ArrayList<String> storesinger = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__song);

        context = this;


        button2 = (Button) findViewById(R.id.button2);
        et_send2 = (EditText) findViewById(R.id.et_send2);

        recyclerView = (RecyclerView) findViewById(R.id.review);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));



        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sword = et_send2.getText().toString();
                String url1 = "http://www.maniadb.com/api/search/";
                String url2 = "/?sr=song&display=10&key=cbnu2017038040@gmail.com&v=.05";
                String key = sword;
                maniadbapi maniadbapi = new maniadbapi(url1, url2, key);
                try {
                    back = maniadbapi.execute().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // MAP 형태로 maniadb에서 검색한 목록을 받아옵니다.
                // image(앨범이미지), singer(가수이름), title(곡명)이고, 각각 ArrayList으로 되어 있습니다.
                // 이 목록을 그대로 가져오셔서 리사이클러뷰나 리스트뷰에 띄우시면 됩니다.
                // 이후, 선택된 노래를 그대로 Map<String, Object>(Object는 ArrayList입니다. maniadbapi.java처럼 선언하시고 사용하시면 됩니다.) 형태로 임시저장 후
                // 그대로 firestore sinfo 에 저장하면 됩니다.
                // 저장할 때는 Activity_match.java처럼 map형태로 sinfo에 저장하면 됩니다.

                // System.out.println("----------------------main" + back);

                titles = (ArrayList<String>) back.get("title");
                singers = (ArrayList<String>) back.get("singer");
                images = (ArrayList<String>) back.get("image");

                readapter = new Readapter(storetitle, storesinger);

                for(int i = 0; i < titles.size(); i++){
                    String str = titles.get(i);
                    String str2 = singers.get(i);
                    String str3 = images.get(i);
                    readapter.setArrayData(str, str2, str3);
                }

                recyclerView.setAdapter(readapter);



            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    imageView.setImageURI(uri);
                }
                break;
        }
    }

}