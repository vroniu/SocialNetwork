package com.example.SocialNetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.SocialNetwork.entity.SocialNetworkPost;

@Repository
public interface SocialNetworkPostRepository extends PagingAndSortingRepository<SocialNetworkPost, Long> {
    
    @Query(value = "SELECT * FROM social_network_post ORDER BY view_count DESC LIMIT 10", nativeQuery = true)
    public List<SocialNetworkPost> findTop10ByViewCount();
    
}
