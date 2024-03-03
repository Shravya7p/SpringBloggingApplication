package com.springblogapp.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springblogapp.entities.Category;
import com.springblogapp.exceptions.ResourceNotFoundException;
import com.springblogapp.payloads.CategoryDto;
import com.springblogapp.repos.CategoryRepo;
import com.springblogapp.services.CategoryService;


@Service
public class CategoryServiceImpl implements CategoryService{
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		Category cat = this.modelMapper.map(categoryDto, Category.class);
		Category addedCat = this.categoryRepo.save(cat);
		return this.modelMapper.map(addedCat,CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
		// TODO Auto-generated method stub
		Category cat = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category ","categoryId",categoryId));
		cat.setCategoryTitle(categoryDto.getCategoryTitle());
		cat.setCategoryDesc(categoryDto.getCategoryDesc());
	   Category updatedCat = this.categoryRepo.save(cat);
		return this.modelMapper.map(updatedCat,CategoryDto.class);
	}

	@Override
	public void deleteCategory(Integer categoryId) {
		// TODO Auto-generated method stub
		Category cat = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category ","categoryId",categoryId));
		this.categoryRepo.delete(cat);

	}

	@Override
	public CategoryDto getCategory(Integer categoryId) {
		// TODO Auto-generated method stub
		Category cat  = this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category ","categoryId",categoryId));
		
	    return this.modelMapper.map(cat, CategoryDto.class);
	}

	@Override
	public List<CategoryDto> getAllCategories() {
		// TODO Auto-generated method stub
		List<Category> categories = this.categoryRepo.findAll();
		List<CategoryDto> categoryDtos = categories.stream().map((cat)->this.modelMapper.map(cat,CategoryDto.class)).collect(Collectors.toList());
		
		return categoryDtos;
	}


}
