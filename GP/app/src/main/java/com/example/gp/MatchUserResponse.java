package com.example.gp;

import com.google.gson.annotations.SerializedName;

public class MatchUserResponse {

    @SerializedName("cname")
    public String cname;

    @SerializedName("fuid")
    public String fuid;

    @SerializedName("uid")
    public String uid;

    public void setcname(String cname) {
        this.cname = cname;
    }

    public String getcname() {
        return cname;
    }

    public void setfuid(String fuid) {
        this.fuid = fuid;
    }

    public String getfuid() {
        return fuid;
    }
}
