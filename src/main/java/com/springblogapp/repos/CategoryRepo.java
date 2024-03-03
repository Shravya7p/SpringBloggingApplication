package com.springblogapp.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springblogapp.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

}
