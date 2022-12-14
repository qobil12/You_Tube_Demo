package com.company.repository;
// PROJECT NAME -> You_Tube_Demo
// TIME -> 16:19
// MONTH -> 07
// DAY -> 16

import com.company.entity.TagEntity;
import com.company.entity.video.VideoEntity;
import com.company.entity.video.VideoTagEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VideoTagRepository extends CrudRepository<VideoTagEntity , Integer> {


    List<VideoTagEntity> findAllByVideo(VideoEntity entity);
}
