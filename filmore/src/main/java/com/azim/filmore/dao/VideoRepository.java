package com.azim.filmore.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azim.filmore.entity.Video;

public interface VideoRepository extends JpaRepository<Video,Long> {

}
