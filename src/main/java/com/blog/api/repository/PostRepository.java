package com.blog.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blog.api.modal.Category;
import com.blog.api.modal.Post;
import com.blog.api.modal.User;

public interface PostRepository extends JpaRepository<Post, Integer>{

	List<Post> findByUser(User user);
	
	List<Post> findByCategory(Category category);

	List<Post> findByTitleContaining(String title);
	
	List<Post> findByContentContaining(String content);
	
	@Query("SELECT DISTINCT p From Post p Where p.title LIKE %:query% OR p.content LIKE %:query%")
	List<Post> searchPost(@Param("query") String query);
}
