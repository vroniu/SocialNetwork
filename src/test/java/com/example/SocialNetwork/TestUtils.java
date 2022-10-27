package com.example.SocialNetwork;

import java.util.ArrayList;
import java.util.List;

import com.example.SocialNetwork.entity.SocialNetworkPost;

public class TestUtils {
    public static List<SocialNetworkPost> generateSamplePosts(int postsToGenerate) {
        List<SocialNetworkPost> sampleData = new ArrayList<>(postsToGenerate);
        for (int i = 0; i < postsToGenerate; i++) {
            SocialNetworkPost post = new SocialNetworkPost();
            post.setAuthor("author " + i);
            post.setContent("hey! random content no: " + i);
            post.setViewCount(i * 10L);
            sampleData.add(post);
        }
        return sampleData;
    }
}
