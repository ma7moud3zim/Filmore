package com.azim.filmore.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.azim.filmore.entity.User;
import com.azim.filmore.entity.Video;
import com.azim.filmore.enums.Role;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);
	
	Optional<User> findByVerificationToken(String token);
	
	Optional<User> findByPasswordResetToken(String token);

	long countByRoleAndActive(Role admin, boolean active);
	
	@Query("SELECT u FROM User u WHERE "
			+ "LOWER(u.fullName) LIKE LOWER(CONCAT('%',:search,'%')) OR " 
			+ "LOWER(u.email) LIKE LOWER(CONCAT('%',:search,'%'))")
	
	Page<User> searchUsers(@Param("search") String search, Pageable pageable);

	long countByRole(Role admin);

	@Query("SELECT v.id FROM User u JOIN u.watchlist v WHERE u.email = :email AND v.id IN :videosIds")
	Set<Long> findWatchlistVideosIds(@Param("email") String email,@Param("videosIds") List<Long> videosIds);

	@Query("SELECT v FROM User u JOIN u.watchlist v"
			+ "WHERE u.id = :userId AND v.published = true AND ("
			+ "LOWER(v.title) LIKE LOWER(CONCAT('%',:search,'%')) OR " 
			+ "LOWER(v.description) LIKE LOWER(CONCAT('%',:search,'%')))")
	Page<Video> searhWatchlistByUserId(@Param("userId") Long userId, @Param("search") String search, Pageable pageable);

	@Query("SELECT v FROM User u JOIN u.watchlist v"
			+ "WHERE u.id = :userId AND v.published = true")
	Page<Video> findByWatchlistUserId(@Param("userId") Long userId, Pageable pageable);
	
}
