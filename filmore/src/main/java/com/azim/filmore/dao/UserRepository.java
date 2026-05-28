package com.azim.filmore.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azim.filmore.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
}
