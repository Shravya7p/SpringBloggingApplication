package com.springblogapp.services;

import java.util.List;

import com.springblogapp.payloads.CommentDto;


public interface CommentService {
	
	CommentDto createComment(CommentDto commentDto,Integer postId);
	List<CommentDto> getAllComments();
	void deleteComment(Integer commentId);

}
