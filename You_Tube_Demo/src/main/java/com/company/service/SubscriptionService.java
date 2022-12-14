package com.company.service;
// PROJECT NAME -> You_Tube_Demo
// TIME -> 17:13
// MONTH -> 07
// DAY -> 16


import com.company.entity.ChannelEntity;
import com.company.entity.ProfileEntity;
import com.company.entity.SubscriptionEntity;
import com.company.enums.NotificationType;
import com.company.enums.SubscriptionStatus;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    ProfileService profileService;


    public void subscribe(String commentId) {
        subscribeUnsubscribe(commentId, profileService.getProfile().getId(), SubscriptionStatus.ACTIVE, NotificationType.PERSONALIZED);
    }

    public void unsubscribe(String commentId) {
        subscribeUnsubscribe(commentId, profileService.getProfile().getId(), SubscriptionStatus.DELETED ,  NotificationType.PERSONALIZED);
    }

    private void subscribeUnsubscribe(String channelId, Integer pId, SubscriptionStatus status , NotificationType type) {
        Optional<SubscriptionEntity> optional = subscriptionRepository.findExists(channelId, pId);
        if (optional.isPresent()) {
            SubscriptionEntity like = optional.get();
            like.setStatus(status);
            subscriptionRepository.save(like);
            return;
        }
        boolean commentExists = subscriptionRepository.existsById(channelId);
        if (!commentExists) {
            throw new ItemNotFoundException("Article NotFound");
        }

        SubscriptionEntity like = new SubscriptionEntity();
        like.setChannel( new ChannelEntity(channelId));
        like.setProfile(new ProfileEntity(pId));
        like.setStatus(status);
        like.setType(type);
        subscriptionRepository.save(like);
    }

    public void removeSubscription(String channelId) {
       /* Optional<ArticleLikeEntity> optional = commentlikeRepository.findExists(articleId, pId);
        optional.ifPresent(articleLikeEntity -> {
            commentlikeRepository.delete(articleLikeEntity);
        });*/
        subscriptionRepository.delete(channelId, profileService.getProfile().getId());
    }



}
