package com.blog.api.service;

import java.util.List;

import com.blog.api.dtos.CategoryDto;
import com.blog.api.exception.CategoryException;

public interface CategoryService {

	CategoryDto createCategory(CategoryDto categoryDto) throws CategoryException;
	
	CategoryDto updateCategory(CategoryDto categoryDto, Integer CategoryId) throws CategoryException;
	
	CategoryDto getCategoryById(Integer categoryId) throws CategoryException;
	
	List<CategoryDto> getAllCategory() throws CategoryException;
	
	void deleteCategoryById(Integer categoryId) throws CategoryException;
	
}
