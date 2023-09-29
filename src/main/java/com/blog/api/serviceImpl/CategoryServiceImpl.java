package com.blog.api.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.api.dtos.CategoryDto;
import com.blog.api.exception.CategoryException;
import com.blog.api.modal.Category;
import com.blog.api.repository.CategoryRepo;
import com.blog.api.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) throws CategoryException {
		Category saveCategory = this.categoryRepo.save(this.DtoToCategory(categoryDto));
		return this.CategoryToDto(saveCategory);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer CategoryId) throws CategoryException {
		CategoryDto categoryDto2 = this.getCategoryById(CategoryId);
		Category category = this.DtoToCategory(categoryDto2);
		if(categoryDto.getCategoryTitle()!=null) {
			category.setCategoryTitle(categoryDto.getCategoryTitle());
		}
		if(categoryDto.getCategoryDesc()!=null) {
			category.setCategoryDesc(categoryDto.getCategoryDesc());			
		}
		Category saveCategory = this.categoryRepo.save(category);
		return this.CategoryToDto(saveCategory);
	}

	@Override
	public CategoryDto getCategoryById(Integer categoryId) throws CategoryException {
		Optional<Category> catOpt = this.categoryRepo.findById(categoryId);
		if(catOpt.isPresent()) {
			Category category = catOpt.get();
			return this.CategoryToDto(category);
		}
		throw new CategoryException("Category Not found With Id : "+categoryId);
	}

	@Override
	public List<CategoryDto> getAllCategory() throws CategoryException {
		List<Category> allCategory = this.categoryRepo.findAll();
		if(allCategory.isEmpty() || allCategory.size()<=0) {
			throw new CategoryException("No Category Found.");
		}
		return this.allCategoryToDto(allCategory);
	}

	@Override
	public void deleteCategoryById(Integer categoryId) throws CategoryException {
		CategoryDto categoryDto = this.getCategoryById(categoryId);
		Category category = this.DtoToCategory(categoryDto);
		this.categoryRepo.delete(category);
	}
	
	public CategoryDto CategoryToDto(Category category) {
		return this.modelMapper.map(category, CategoryDto.class);
//		CategoryDto categoryDto=new CategoryDto();
//		categoryDto.setCategoryId(category.getCategoryId());
//		categoryDto.setCategoryTitle(category.getCategoryTitle());
//		categoryDto.setCategoryDesc(category.getCategoryDesc());
//		return categoryDto;
	}

	public Category DtoToCategory(CategoryDto categoryDto) {
		return this.modelMapper.map(categoryDto, Category.class);
//		Category category=new Category();
//		category.setCategoryId(categoryDto.getCategoryId());
//		category.setCategoryTitle(categoryDto.getCategoryTitle());
//		category.setCategoryDesc(categoryDto.getCategoryDesc());
//		return category;
	}
	
	public List<Category> allDtoToCategory(List<CategoryDto> categoryDtos){
		return categoryDtos.stream().map(category->this.DtoToCategory(category)).collect(Collectors.toList());
	}
	
	public List<CategoryDto> allCategoryToDto(List<Category> categories){
		return categories.stream().map(categoryDto->this.CategoryToDto(categoryDto)).collect(Collectors.toList());
	}
}
