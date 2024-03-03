package com.springblogapp.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springblogapp.entities.Category;
import com.springblogapp.entities.Post;
import com.springblogapp.entities.User;
import com.springblogapp.exceptions.ResourceNotFoundException;
import com.springblogapp.payloads.PostDto;
import com.springblogapp.payloads.PostResponse;
import com.springblogapp.repos.CategoryRepo;
import com.springblogapp.repos.PostRepo;
import com.springblogapp.repos.UserRepo;
import com.springblogapp.services.PostService;

@Service
public class PostServiceImpl implements PostService {
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
		User user = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("userId ","user ",userId));
		Category category = this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("categoryId ","category",categoryId));
		
		Post post = this.modelMapper.map(postDto,Post.class);
		post.setImage("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		
		
		Post newPost = this.postRepo.save(post);
		return this.modelMapper.map(newPost,PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("post ","postId",postId));
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImage(postDto.getImage());
		
		Post updatedPost = this.postRepo.save(post);
		return this.modelMapper.map(updatedPost,PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("post ","postId",postId));
		this.postRepo.delete(post);
		

	}

	@Override
	public PostResponse getAllPosts(Integer pageNumber, Integer pageSize,String sortBy,String sortDir) {
		// TODO Auto-generated method stub
		
		Sort sort = sortDir.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
		
		Pageable p = PageRequest.of(pageNumber, pageSize,sort);

        //Page<Post> pagePost = this.postRepo.findAll(p);
        org.springframework.data.domain.Page<Post> p1 = this.postRepo.findAll(p);
        List<Post> allPosts = p1.getContent();

        List<PostDto> postDtos = allPosts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNumber(p1.getNumber());
        postResponse.setPageSize(p1.getSize());
        postResponse.setTotalElements(p1.getTotalElements());
        postResponse.setTotalPages(p1.getTotalPages());
        postResponse.setLastPage(p1.isLast());
        
        return postResponse;
        
        //return postDtos;
	}

	@Override
	public PostDto getPostById(Integer postId) {
		// TODO Auto-generated method stub
		Post post = this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("postId ","post",postId));
		return this.modelMapper.map(post,PostDto.class);
	}

	@Override
	public List<PostDto> getPostByCategory(Integer categoryId) {
		
        Category cat = this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("categoryId ","category",categoryId));
		List<Post> posts = this.postRepo.findByCategory(cat);
		List<PostDto> postDtos = posts.stream().map((post)->(this.modelMapper.map(post,PostDto.class))).collect(Collectors.toList());
		return postDtos;
		
	}

	@Override
	public List<PostDto> getPostsByUser(Integer userId) {
		User user = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("userId ","user",userId));
		List<Post> posts = this.postRepo.findByUser(user);
		List<PostDto> postDtos = posts.stream().map(post->(this.modelMapper.map(post,PostDto.class))).collect(Collectors.toList());
		
		return postDtos;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		// TODO Auto-generated method stub
		List<Post> posts = this.postRepo.searchByTitle("%"+keyword+"%");
		List<PostDto> postDtos = posts.stream().map((post)->this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
		return postDtos;
	}

	
}
