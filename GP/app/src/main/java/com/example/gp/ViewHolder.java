package com.example.gp;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView singer;
    public ImageView imageView;

    String uid = FirebaseAuth.getInstance().getUid();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    ArrayList<String> temptitle;
    ArrayList<String> tempsinger;

    public ViewHolder(Context context, View view, ArrayList<String> storetitle, ArrayList<String> storesinger){
        super(view);

        title = view.findViewById(R.id.title);
        singer = view.findViewById(R.id.singer);
        imageView = view.findViewById(R.id.image123);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 위치받아오기
                int position = getAdapterPosition();
                System.out.println("-------------------------" + position);
                // 노래 가수 받아오고 ArrayList에 저장
                storetitle.add(title.getText().toString());
                storesinger.add(singer.getText().toString());
                //System.out.println("-------------------------" + titles + singers);

                for(int i = 0; i < storetitle.size(); i++) System.out.println(storetitle.get(i) + storesinger.get(i));

                // 목록이 4개가 되면 자동으로 firestore에 저장
                if(storetitle.size() == 4) {
                    Map<String, Object> storing = new HashMap<>();
                    storing.put("title", storetitle);
                    storing.put("singer", storesinger);
                    firebaseFirestore.collection(uid)
                            .document("sinfo")
                            .set(storing);
                    storetitle.clear();
                    storesinger.clear();
                }

                // 1곡 선택후 다른곡 검색시 ArrayList 초기화되는 문제 발생

                temptitle = (ArrayList<String>) storetitle.clone();
                tempsinger = (ArrayList<String>) storesinger.clone();

            }
        });
    }

    public ArrayList<String> ReturnArray(ArrayList<String> temp){
        return temp;
    }

}
