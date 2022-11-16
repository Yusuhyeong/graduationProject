package com.example.gp;

import com.google.gson.annotations.SerializedName;

public class MatchRequest {

    @SerializedName("nickname")
    public String nickname;

    public String getnickname() {
        return nickname;
    }

    public void setnickname(String nickname) {
        this.nickname = nickname;
    }


    public MatchRequest(String nickname) {
        this.nickname = nickname;
    }

}
