package com.springblogapp.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springblogapp.entities.Comment;
import com.springblogapp.entities.Post;
import com.springblogapp.exceptions.ResourceNotFoundException;
import com.springblogapp.payloads.CommentDto;
import com.springblogapp.repos.CommentRepo;
import com.springblogapp.repos.PostRepo;
import com.springblogapp.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepo commentRepo;
	
	@Autowired
	private PostRepo postRepo;
    
	@Autowired
	private ModelMapper modelMapper;
	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId) {
		// TODO Auto-generated method stub
		Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("post ","postId",postId));
		Comment comment = this.modelMapper.map(commentDto,Comment.class);
		comment.setPost(post);
		Comment savedComment = this.commentRepo.save(comment);
		return this.modelMapper.map(savedComment,CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentId) {
		// TODO Auto-generated method stub
		Comment comment = this.commentRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("comment ","commentId",commentId));
        this.commentRepo.delete(comment);
	}

	@Override
	public List<CommentDto> getAllComments() {
		// TODO Auto-generated method stub
		List<Comment> comments = this.commentRepo.findAll();
		List<CommentDto> commentDtos = comments.stream().map((comment)->this.modelMapper.map(comment,CommentDto.class)).collect(Collectors.toList());
		return commentDtos;
	}

}
