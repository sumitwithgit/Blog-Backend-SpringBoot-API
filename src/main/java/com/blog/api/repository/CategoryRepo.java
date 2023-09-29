package com.blog.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.api.modal.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer>{

}
