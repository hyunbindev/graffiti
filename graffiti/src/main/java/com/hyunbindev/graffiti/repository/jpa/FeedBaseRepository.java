package com.hyunbindev.graffiti.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;

public interface FeedBaseRepository extends JpaRepository<FeedBaseEntity,Long> {
}