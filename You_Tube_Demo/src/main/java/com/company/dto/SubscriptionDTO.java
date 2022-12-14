package com.company.dto;
// PROJECT NAME -> You_Tube_Demo
// TIME -> 17:17
// MONTH -> 07
// DAY -> 16

import com.company.entity.ChannelEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.NotificationType;
import com.company.enums.SubscriptionStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class SubscriptionDTO {

    private Integer id;
    private Integer profileId;
    private ProfileEntity profile;
    private String channelId;
    private ChannelEntity channel;
    private SubscriptionStatus status;
    private NotificationType type;
    private LocalDateTime createdDate;
}
