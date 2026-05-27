package com.azim.filmore.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import com.azim.filmore.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
public class User {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String fullName;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role=Role.USER;
	
	@Column(nullable=false)
	private boolean active=true;
	
	@Column(nullable =false)
	private boolean emailVarified = false;
	
	@Column(unique =true)
	private String varificationToken;
	
	@Column
	private Instant varificationTokenExpiry;
	
	
	@Column
	private String passwordResetToken;
	
	@Column
	private Instant passwordResetTokenExpiry;
	
	@CreationTimestamp
	@Column(nullable =false, updatable=false)
	private Instant createdAt;
	
	@CreationTimestamp
	@Column(nullable =false, updatable=false)	
	private Instant updatedAt;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "user_watchlist", 
			joinColumns = @JoinColumn(name = "user_id"), 
			inverseJoinColumns = @JoinColumn(name = "video_id")
	)
	private Set<Video> watchlist = new HashSet<>();
	
	public void addToWatchlist(Video video) {
		watchlist.add(video);
	}
	public void removeToWatchlist(Video video) {
		watchlist.remove(video);
	}
	
	
}
