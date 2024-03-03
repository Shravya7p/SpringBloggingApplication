package com.springblogapp.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springblogapp.config.AppConstants;
import com.springblogapp.payloads.ApiResponse;
import com.springblogapp.payloads.PostDto;
import com.springblogapp.payloads.PostResponse;
import com.springblogapp.services.FileService;
import com.springblogapp.services.PostService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${project.image}")
	private String path;
	
	@GetMapping("/hello")
	public ResponseEntity<String> helloM(){
		return new ResponseEntity<>("Hello World!!",HttpStatus.OK);
	}
	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable Integer userId, @PathVariable Integer categoryId){
		PostDto createPost = this.postService.createPost(postDto,userId, categoryId);
		
		return new ResponseEntity<PostDto>(createPost,HttpStatus.CREATED);
		
	}
	//get Post By User
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable Integer userId){
		List<PostDto> posts = this.postService.getPostsByUser(userId);
		return new ResponseEntity<List<PostDto>>(posts,HttpStatus.OK);
	}
	
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable Integer categoryId){
		List<PostDto> posts = this.postService.getPostByCategory(categoryId);
		return new ResponseEntity<List<PostDto>>(posts,HttpStatus.OK);
	}
    
	//Get All Posts
	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPost(@RequestParam(value="pageNumber",defaultValue=AppConstants.PAGE_NUMBER,required=false) Integer pageNumber,
			@RequestParam(value="pageSize",defaultValue=AppConstants.PAGE_SIZE,required=false) Integer pageSize,
			@RequestParam(value="sortBy",defaultValue=AppConstants.SORT_BY,required=false) String sortBy,
			@RequestParam(value="sortDir",defaultValue=AppConstants.SORT_DIR,required=false) String sortDir
			){
//		List<PostDto> allPost = this.postService.getAllPosts(pageNumber,pageSize);
		PostResponse allPost = this.postService.getAllPosts(pageNumber,pageSize,sortBy,sortDir);

		return new ResponseEntity<>(allPost,HttpStatus.OK);
	}
	
	//get post by id
	@GetMapping("/post/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId){
		PostDto postDto = this.postService.getPostById(postId);
		return new ResponseEntity<>(postDto,HttpStatus.OK);
	}
	
	//delete post by id
	@DeleteMapping("/post/{postId}")
	public ApiResponse deletePost(@PathVariable Integer postId) {
		this.postService.deletePost(postId);
		return new ApiResponse("post is deleted successfully!!",true);
	}
	
	//update post
	
	@PutMapping("/post/{postId}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto,@PathVariable Integer postId){
		PostDto updatedPost = this.postService.updatePost(postDto, postId);
		return ResponseEntity.ok(updatedPost);
		
	}
	
	//search Posts
	@GetMapping("/posts/search/{keywords}")
	public ResponseEntity<List<PostDto>> searchPostsByTitle(@PathVariable("keywords") String keywords){
		List<PostDto> res = this.postService.searchPosts(keywords);
		return new ResponseEntity<List<PostDto>>(res,HttpStatus.OK);
	}
	
	//post image upload
	
	@PostMapping("/post/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image, @PathVariable Integer postId) throws IOException{
		
		PostDto postDto = this.postService.getPostById(postId);
		String fileName = this.fileService.uploadImage(path, image);
		
		postDto.setImage(fileName);
		PostDto updatedPost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<>(updatedPost,HttpStatus.OK);
	}
	
	//method to serve files
	@GetMapping(value="/posts/images/{imageName}",produces=MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException {
		
		InputStream resource = this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource,response.getOutputStream());
	}

}
