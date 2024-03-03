package com.springblogapp.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springblogapp.entities.Comment;

public interface CommentRepo extends JpaRepository<Comment, Integer> {

}
