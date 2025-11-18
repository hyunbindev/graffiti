package com.hyunbindev.graffiti.repository.jpa.feed;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class FeedBaseCustomRepsotory {
	@PersistenceContext
	private EntityManager em;
	public List<FeedBaseEntity> findRecentFeed(List<String> groupIds, int size, Long lastId){
		
		StringBuilder queryBuilder = new StringBuilder("SELECT f FROM FeedBaseEntity f JOIN FETCH f.author WHERE f.deleted = false AND f.group.id IN :groupIds ");
		
		if(lastId != null) queryBuilder.append("AND f.id < :lastId ");
		
		queryBuilder.append("ORDER BY f.id DESC");
		
		TypedQuery<FeedBaseEntity> query = em.createQuery(queryBuilder.toString(),FeedBaseEntity.class).setParameter("groupIds", groupIds).setMaxResults(size);
		
		if(lastId != null) query.setParameter("lastId",lastId);
		return query.getResultList();
	}
}
