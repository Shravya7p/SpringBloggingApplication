package com.springblogapp.services;

import java.util.List;

import com.springblogapp.payloads.PostDto;
import com.springblogapp.payloads.PostResponse;

public interface PostService {
	
	//create
		PostDto createPost(PostDto postDto,Integer userId,Integer categoryId);
		
		//update
		PostDto updatePost(PostDto postDto, Integer postId);
		
		//delete
		void deletePost(Integer postId);
		
		//getAllPosts
		PostResponse getAllPosts(Integer pageNumber, Integer pageSize,String sortBy,String sortDir);
		
		//get Single Post
		PostDto getPostById(Integer postId);
		
		//getAllPosts By category
		List<PostDto> getPostByCategory(Integer categoryId);
		
		
		//get All Posts by userUser(
		List<PostDto> getPostsByUser(Integer userId);
		
		//search Post
		List<PostDto> searchPosts(String keyword);

}
