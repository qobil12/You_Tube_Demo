package com.company.controller;
// PROJECT NAME -> You_Tube_Demo
// TIME -> 17:14
// MONTH -> 07
// DAY -> 16

import com.company.dto.SubscriptionDTO;
import com.company.dto.comment.CommentLikeDTO;
import com.company.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;


    @PostMapping("/subscribe")
    public ResponseEntity<Void> subscribe(@RequestBody SubscriptionDTO dto
    ) {

        subscriptionService.subscribe(dto.getChannelId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@RequestBody SubscriptionDTO dto) {
        subscriptionService.unsubscribe(dto.getChannelId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/remove")
    public ResponseEntity<Void> remove(@RequestBody SubscriptionDTO dto) {
        subscriptionService.removeSubscription(dto.getChannelId());
        return ResponseEntity.ok().build();
    }

}
