package com.example.gp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MatchAPI {

    @POST("/match")
    Call<MatchResponse> getMatchResponse(@Body MatchRequest match_request);

    @POST("/match/matched-user")
    Call<MatchUserResponse> getMatchUserResponse(@Body MatchUserRequest matched_user_request);

    @POST("/match/accept")
    Call<AcceptResponse> getAcceptResponse(@Body AcceptRequest accept_request);

    @POST("/match/refuse")
    Call<RefuseResponse> getRefuseResponse(@Body RefuseRequest refuse_request);
}
