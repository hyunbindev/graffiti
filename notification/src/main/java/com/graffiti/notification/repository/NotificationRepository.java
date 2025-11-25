package com.graffiti.notification.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.graffiti.notification.entity.Notification;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String>{
	List<Notification> findByReceiverUuid(String receiverUuid, Pageable pageable);
}
