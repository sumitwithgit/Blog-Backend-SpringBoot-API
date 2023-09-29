package com.blog.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.api.modal.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

}
