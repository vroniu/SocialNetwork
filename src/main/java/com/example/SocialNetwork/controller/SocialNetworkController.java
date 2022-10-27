package com.example.SocialNetwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.SocialNetwork.entity.SocialNetworkPost;
import com.example.SocialNetwork.exception.PostNotFoundException;
import com.example.SocialNetwork.service.SocialNetworkPostService;

@RestController
@RequestMapping("/api/v1/posts")
public class SocialNetworkController {

    @Value("${page-size}")
    private int defaultPageSize;
    
    private final SocialNetworkPostService socialNetworkPostService;

    @Autowired
    public SocialNetworkController(SocialNetworkPostService socialNetworkPostService) {
        this.socialNetworkPostService = socialNetworkPostService;
    }

    @GetMapping("")
    public ResponseEntity<Page<SocialNetworkPost>> getPosts(@RequestParam Integer page, @RequestParam(required = false) Integer pageSize ) {
        if (pageSize == null) {
            pageSize = defaultPageSize;
        }
        return ResponseEntity.ok(socialNetworkPostService.getPosts(page, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(socialNetworkPostService.getPostById(id));
        } catch (PostNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/top10")
    public ResponseEntity<Page<SocialNetworkPost>> getTop10Posts() {
        return ResponseEntity.ok(socialNetworkPostService.getTop10Posts());
    }

    @PostMapping("")
    public ResponseEntity<?> savePost(@RequestBody SocialNetworkPost post) {
        if (post.getId() != null) {
            return ResponseEntity.badRequest().body("Invalid body: new post cannot have an ID");
        } else {
            return ResponseEntity.status(201).body(socialNetworkPostService.savePost(post));
        }
    }

    @PutMapping("")
    public ResponseEntity<?> updatePost(@RequestBody SocialNetworkPost post) {
        if (post.getId() == null) {
            return ResponseEntity.badRequest().body("Invalid body: cannot update post without an ID");
        } else {
            return ResponseEntity.ok(socialNetworkPostService.savePost(post));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable Long id) {
        try {
            socialNetworkPostService.deletePostById(id);
            return ResponseEntity.ok("Post with ID " + id + " deleted successfully");
        } catch (PostNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
