package com.company.repository;
// PROJECT NAME -> You_Tube_Demo
// TIME -> 17:13
// MONTH -> 07
// DAY -> 16

import com.company.entity.SubscriptionEntity;

import com.company.entity.comment.CommentLikeEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SubscriptionRepository extends CrudRepository<SubscriptionEntity,Integer> {
    @Query("FROM SubscriptionEntity a where  a.channel.id=:channelId and a.profile.id =:profileId")
    Optional<SubscriptionEntity> findExists(String channelId, Integer profileId);

    @Transactional
    @Modifying
    @Query("DELETE FROM SubscriptionEntity a where  a.channel.id=:channelId and a.profile.id =:profileId")
    void delete(String channelId, Integer profileId);

    boolean existsById(String id);

}
