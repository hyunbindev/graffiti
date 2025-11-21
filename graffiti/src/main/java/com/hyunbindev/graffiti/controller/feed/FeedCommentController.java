package com.hyunbindev.graffiti.controller.feed;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyunbindev.graffiti.data.feed.FeedCommentDTO;
import com.hyunbindev.graffiti.data.whisper.CreateCommentDTO;
import com.hyunbindev.graffiti.service.feed.comment.FeedCommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feed")
@Slf4j
public class FeedCommentController {
	private final FeedCommentService feedCommentService;
	/**
	 * Feed 덧글 생성
	 * @param auth
	 * @param feedId
	 * @param createDto
	 * @return status 200
	 */
	@PostMapping("/{feedId}/comment")
	public ResponseEntity<Void> createFeedComment(Authentication auth, @PathVariable("feedId")Long feedId,@RequestBody @Valid CreateCommentDTO createDto){
		feedCommentService.createWhisperComment(auth.getName(), feedId, createDto);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Feed 덧글 삭제
	 * @param auth
	 * @param commentId
	 * @return status 200
	 */
	@DeleteMapping("/comment/{commentId}")
	public ResponseEntity<Void> deleteFeedComment(Authentication auth, @PathVariable("commentId")Long commentId){
		feedCommentService.deleteWhisperComment(auth.getName(), commentId);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Feed 덧글 조회
	 * @param auth
	 * @param feedId
	 * @return
	 */
	@GetMapping("/{feedId}/comment")
	public ResponseEntity<List<FeedCommentDTO>> getFeedComment(Authentication auth, @PathVariable("feedId")Long feedId, @RequestParam(name="lastId",required=false)Long lastId, @RequestParam("size")Integer size){
		if(lastId==null) lastId = Long.MAX_VALUE;
		return ResponseEntity.ok(feedCommentService.getWhisperComments(auth.getName(), feedId, lastId,size));
	}
}
