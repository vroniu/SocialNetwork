package com.example.SocialNetwork.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.SocialNetwork.entity.SocialNetworkPost;

@SpringBootTest
public class SocialNetworkPostRepositoryTest {

    @Autowired
    SocialNetworkPostRepository socialNetworkPostRepository;

    void loadSampleData(int postsToGenerate) {
        List<SocialNetworkPost> sampleData = new ArrayList<>(postsToGenerate);
        for (int i = 0; i < postsToGenerate; i++) {
            SocialNetworkPost post = new SocialNetworkPost();
            post.setAuthor("author" + i);
            post.setContent("hey! random content no: " + i);
            post.setViewCount(i * 10L);
            sampleData.add(post);
        }
        socialNetworkPostRepository.saveAll(sampleData);
    }


    @Test
    void testFindTop10ByViewCount_ShouldFindTopTenPosts_WhenDbPopulatedFully() {
        loadSampleData(150);

        List<SocialNetworkPost> result = socialNetworkPostRepository.findTop10ByViewCount();

        assertEquals(10, result.size());
        assertEquals(1490, result.get(0).getViewCount());
        assertEquals(1400, result.get(result.size() - 1).getViewCount());
    }

    @Test
    void testFindTop10ByViewCount_ShouldFindTopNPosts_WhenDbPopulatedPartly() {
        loadSampleData(6);

        List<SocialNetworkPost> result = socialNetworkPostRepository.findTop10ByViewCount();

        assertEquals(6, result.size());
        assertEquals(50, result.get(0).getViewCount());
        assertEquals(0, result.get(result.size() - 1).getViewCount());
    }
}
