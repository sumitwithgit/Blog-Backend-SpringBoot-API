package com.blog.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.api.dtos.CategoryDto;
import com.blog.api.exception.CategoryException;
import com.blog.api.response.ApiResponse;
import com.blog.api.service.CategoryService;

@RestController
@RequestMapping("/api/categories/category")
public class CategoryController {

	private Logger logger=LoggerFactory.getLogger(CategoryController.class);
	
	@Autowired
	private CategoryService categoryService;
	
	@PostMapping("/")
	public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) throws CategoryException{
		CategoryDto createCategory = this.categoryService.createCategory(categoryDto);
		logger.info(" : createCategory");
		return new ResponseEntity<CategoryDto>(createCategory,HttpStatus.CREATED);
	}
	
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable("categoryId") Integer categoryId) throws CategoryException{
		CategoryDto updateCategory = this.categoryService.updateCategory(categoryDto, categoryId);
		logger.info(" : updateCategory");
		return new ResponseEntity<CategoryDto>(updateCategory,HttpStatus.CREATED);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<CategoryDto>> getAllCategory() throws CategoryException{
		List<CategoryDto> allCategory = this.categoryService.getAllCategory();
		logger.info(" : getAllCategory");
		return new ResponseEntity<List<CategoryDto>>(allCategory,HttpStatus.OK);
	}
	
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("categoryId") Integer categoryId) throws CategoryException{
		CategoryDto categoryDto = this.categoryService.getCategoryById(categoryId);
		logger.info(" : getCategoryById");
		return new ResponseEntity<CategoryDto>(categoryDto,HttpStatus.OK);
	}
	
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable("categoryId") Integer categoryId) throws CategoryException{
		this.categoryService.deleteCategoryById(categoryId);
		ApiResponse resp=new ApiResponse();
		resp.setMessage("Category Deleted SuccessFully.");
		resp.setStatusCode("204");
		logger.info(" : deleteCategoryById");
		return new ResponseEntity<ApiResponse>(resp,HttpStatus.OK);
	}
}
