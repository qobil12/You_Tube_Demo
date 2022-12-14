package com.company.repository.mapper;


// PROJECT NAME -> You_Tube_Demo
// TIME -> 16:31
// MONTH -> 07
// DAY -> 16

public interface VideoViewLikeDislikeCountAndStatusByProfile {

    Integer getViewCount();
    Integer getLikeCount();
    Integer getDislikeCount();
    String getStatus();
}
