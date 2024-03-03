package com.springblogapp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springblogapp.payloads.ApiResponse;
import com.springblogapp.payloads.CommentDto;
import com.springblogapp.services.CommentService;


@RestController
@RequestMapping("/apis/")
public class CommentController {
	
	@Autowired
	private CommentService commService;
	
	@PostMapping("/posts/{postId}/comments")
	public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto,@PathVariable Integer postId){
		CommentDto createdComment = this.commService.createComment(commentDto, postId);
		return new ResponseEntity<>(createdComment,HttpStatus.CREATED);
	}
	
	//get all comments
	@GetMapping("/comments")
	public ResponseEntity<CommentDto> getAllComments(){
		List<CommentDto> commentDtos = this.commService.getAllComments();
		return new ResponseEntity(commentDtos,HttpStatus.OK);
	}
	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId){
		this.commService.deleteComment(commentId);
		return new ResponseEntity<>(new ApiResponse("Comment deleted successfully!",true),HttpStatus.OK);
	}


}
