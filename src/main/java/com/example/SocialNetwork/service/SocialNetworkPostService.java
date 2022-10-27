package com.example.SocialNetwork.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.SocialNetwork.entity.SocialNetworkPost;
import com.example.SocialNetwork.exception.PostNotFoundException;
import com.example.SocialNetwork.repository.SocialNetworkPostRepository;

@Service
public class SocialNetworkPostService {

    private final SocialNetworkPostRepository socialNetworkPostRepository;

    @Autowired
    public SocialNetworkPostService(SocialNetworkPostRepository socialNetworkPostRepository) {
        this.socialNetworkPostRepository = socialNetworkPostRepository;
    }

    public SocialNetworkPost getPostById(Long id) throws PostNotFoundException {
        Optional<SocialNetworkPost> post = socialNetworkPostRepository.findById(id);
        if (post.isPresent()) {
            return post.get();
        } else {
            throw new PostNotFoundException(id);
        }
    }

    public Page<SocialNetworkPost> getPosts(int page, int pageSize) {
        Pageable pageRequest = PageRequest.of(page, pageSize);
        return socialNetworkPostRepository.findAll(pageRequest);
    }

    public Page<SocialNetworkPost> getTop10Posts() {
        return new PageImpl<>(socialNetworkPostRepository.findTop10ByViewCount());
    }

    public SocialNetworkPost savePost(SocialNetworkPost entity) {
        return socialNetworkPostRepository.save(entity);
    }

    public void deletePostById(Long id) throws PostNotFoundException {
        SocialNetworkPost post = getPostById(id);
        socialNetworkPostRepository.delete(post);
    }
    
}
