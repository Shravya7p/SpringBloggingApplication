package com.springblogapp.services;

import java.util.List;

import com.springblogapp.payloads.CategoryDto;

public interface CategoryService {
  
	
    CategoryDto createCategory(CategoryDto categoryDto);
	
	CategoryDto updateCategory(CategoryDto categoryDto,Integer categoyrId);
	
	void deleteCategory(Integer categoryId);
	
	CategoryDto getCategory(Integer categoryId);
	
	public List<CategoryDto> getAllCategories();
}
