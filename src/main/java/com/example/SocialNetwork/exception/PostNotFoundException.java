package com.example.SocialNetwork.exception;

public class PostNotFoundException extends IllegalArgumentException {

    public PostNotFoundException(Long id) {
        super("Post with this ID: " + id + " was not found");
    }
    
}
