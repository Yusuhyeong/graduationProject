package com.example.gp;

import com.google.gson.annotations.SerializedName;

public class RefuseRequest {

    @SerializedName("nickname1")
    public String nickname1;

    @SerializedName("nickname2")
    public String nickname2;

    public String getnickname1() {
        return nickname1;
    }

    public String getnickname2() {
        return nickname2;
    }

    public void setnickname1(String nickname1) {
        this.nickname1 = nickname1;
    }

    public void setnickname2(String nickname2) {
        this.nickname2 = nickname2;
    }

    public RefuseRequest(String nickname1, String nickname2) {
        this.nickname1 = nickname1;
        this.nickname2 = nickname2;
    }
}
