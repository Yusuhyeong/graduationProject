package com.example.gp;

import com.google.gson.annotations.SerializedName;

public class MatchUserRequest {

    @SerializedName("nickname")
    public String nickname;

    public String getnickname() {
        return nickname;
    }

    public void setnickname(String nickname) {
        this.nickname = nickname;
    }


    public MatchUserRequest(String nickname) {
        this.nickname = nickname;
    }

}
