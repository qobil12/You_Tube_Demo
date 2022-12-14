package com.company.dto;
// PROJECT NAME -> You_Tube_Demo
// TIME -> 16:16
// MONTH -> 07
// DAY -> 16

import com.company.enums.LikeStatus;
import com.company.enums.VideoStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class VideoFullInfoDTO {
    private String id;
    private String name;
    private String description;
    private String previewId;
    private String previewUrl;
    private String attachId;
    private String attachUrl;
    private Integer categoryId;
    private String categoryName;
    private LocalDateTime createdDate;
    private String channelId;
    private String channelName;
    private String channelUrl;
    private Integer shareCount;
    private List<TagDTO> tagDTOS;
    private Integer viewCount;
    private Integer likeCount;
    private Integer dislikeCount;
    private LikeStatus status;

}
