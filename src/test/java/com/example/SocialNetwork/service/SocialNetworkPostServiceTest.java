package com.example.SocialNetwork.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.SocialNetwork.TestUtils;
import com.example.SocialNetwork.entity.SocialNetworkPost;
import com.example.SocialNetwork.exception.PostNotFoundException;
import com.example.SocialNetwork.repository.SocialNetworkPostRepository;

@SpringBootTest
public class SocialNetworkPostServiceTest {

    @InjectMocks
    private SocialNetworkPostService socialNetworkPostService;

    @Mock
    SocialNetworkPostRepository socialNetworkPostRepository;


    @Test
    void testDeletePostById_ShouldDeletePost_IfPostExists() {
        SocialNetworkPost postToDelete = new SocialNetworkPost();
        postToDelete.setId(999L);
        Mockito.when(socialNetworkPostRepository.findById(999L)).thenReturn(Optional.of(postToDelete));
        
        socialNetworkPostService.deletePostById(999L);

        ArgumentCaptor<SocialNetworkPost> postArgumentCaptor = ArgumentCaptor.forClass(SocialNetworkPost.class);
        Mockito.verify(socialNetworkPostRepository).delete(postArgumentCaptor.capture());
        assertEquals(postArgumentCaptor.getValue(), postToDelete);

    }

    @Test
    void testDeletePostById_ShouldThrowException_IfPostDoesntExist() {
        Mockito.when(socialNetworkPostRepository.findById(999L)).thenReturn(Optional.empty());

        
        Exception exception = assertThrows(PostNotFoundException.class, () -> {
            socialNetworkPostService.deletePostById(999L);
        });

        assertEquals(exception.getMessage(), "Post with this ID: 999 was not found");
    }

    @Test
    void testGetPostById_ShouldReturnPost_IfPostsExists() {
        SocialNetworkPost postToRetrieve = new SocialNetworkPost();
        postToRetrieve.setId(999L);
        postToRetrieve.setAuthor("author");
        postToRetrieve.setContent("content");
        postToRetrieve.setViewCount(100L);
        Mockito.when(socialNetworkPostRepository.findById(999L)).thenReturn(Optional.of(postToRetrieve));

        SocialNetworkPost post = socialNetworkPostService.getPostById(999L);

        assertEquals(post.getAuthor(), "author");
        assertEquals(post.getContent(), "content");
        assertEquals(post.getViewCount(), 100L);
    }

    @Test
    void testGetPostById_ShouldThrowException_IfPostsDoesntExists() {
        Mockito.when(socialNetworkPostRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(PostNotFoundException.class, () -> {
            socialNetworkPostService.getPostById(999L);
        });

        assertEquals("Post with this ID: 999 was not found", exception.getMessage());
    }

    @Test
    void testGetPosts_ShouldReturnPageFromRepository() {
        Page<SocialNetworkPost> mockPage = new PageImpl<>(TestUtils.generateSamplePosts(15));
        Mockito.when(socialNetworkPostRepository.findAll((Pageable)Mockito.any())).thenReturn(mockPage);

        Page<SocialNetworkPost> result = socialNetworkPostService.getPosts(0, 15);

        assertEquals(15, result.getContent().size());
        assertEquals("author 0", result.getContent().get(0).getAuthor());
    }

    @Test
    void testGetTop10Posts_ShouldReturnPageFromRepository() {
        Mockito.when(socialNetworkPostRepository.findTop10ByViewCount()).thenReturn(TestUtils.generateSamplePosts(10));

        Page<SocialNetworkPost> result = socialNetworkPostService.getTop10Posts();

        assertEquals(10, result.getContent().size());
        assertEquals(10, result.getNumberOfElements());
    }

    @Test
    void testSavePost_ShouldCallRepoistoryMethod() {
        SocialNetworkPost postToSave = new SocialNetworkPost();
        postToSave.setAuthor("author");
        postToSave.setContent("Hello! Test content");
        postToSave.setViewCount(21L);

        socialNetworkPostService.savePost(postToSave);

        ArgumentCaptor<SocialNetworkPost> postArgumentCaptor = ArgumentCaptor.forClass(SocialNetworkPost.class);
        Mockito.verify(socialNetworkPostRepository).save(postArgumentCaptor.capture());
        assertEquals("author", postArgumentCaptor.getValue().getAuthor());
        assertEquals("Hello! Test content", postArgumentCaptor.getValue().getContent());
        assertEquals(21L, postArgumentCaptor.getValue().getViewCount());
    }
}
