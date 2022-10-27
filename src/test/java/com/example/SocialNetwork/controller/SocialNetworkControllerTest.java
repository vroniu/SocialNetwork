package com.example.SocialNetwork.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.SocialNetwork.TestUtils;
import com.example.SocialNetwork.entity.SocialNetworkPost;
import com.example.SocialNetwork.exception.PostNotFoundException;
import com.example.SocialNetwork.service.SocialNetworkPostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class SocialNetworkControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Value("${page-size}")
    Integer defaultPageSize;

    @MockBean
    SocialNetworkPostService socialNetworkPostService;

    static ObjectMapper mapper = new ObjectMapper();

    static HttpHeaders headers = new HttpHeaders();

    @BeforeAll
    static void setUp() {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void testDeletePostById_ShouldReturnSuccessMessage_IfPostExists() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/posts/30")).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("Post with ID 30 deleted successfully", result.getResponse().getContentAsString());
    }

    @Test
    void testDeletePostById_ShouldReturnErrorMessage_IfPostDoesntExist() throws Exception {
        Mockito.doThrow(new PostNotFoundException(40L)).when(socialNetworkPostService).deletePostById(40L);
        
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/posts/40")).andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertEquals("Post with this ID: 40 was not found", result.getResponse().getContentAsString());
    }


    @Test
    void testGetPostById_ShouldReturnPost_IfPostsExists() throws Exception {
        SocialNetworkPost post = TestUtils.generateSamplePost(30L);
        Mockito.when(socialNetworkPostService.getPostById(30L)).thenReturn(post);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/30")).andReturn();
        SocialNetworkPost resultResponse = mapper.readValue(result.getResponse().getContentAsString(), SocialNetworkPost.class);

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(post, resultResponse);
    }

    @Test
    void testGetPostById_ShouldReturnErrorMessage_IfPostsDoesntExist() throws Exception {
        Mockito.when(socialNetworkPostService.getPostById(40L)).thenThrow(new PostNotFoundException(40L));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/40")).andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertEquals("Post with this ID: 40 was not found", result.getResponse().getContentAsString());
    }

    @Test
    void testGetPosts_ShouldReturnDefaultListSize_IfSizeNotSpecified() throws Exception {
        Mockito.when(socialNetworkPostService.getPosts(Mockito.anyInt(), Mockito.anyInt())).thenReturn(
            new PageImpl<SocialNetworkPost>(TestUtils.generateSamplePosts(defaultPageSize))
        );

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts?page=0")).andReturn();

        ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> pageSizeCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(socialNetworkPostService).getPosts(pageCaptor.capture(), pageSizeCaptor.capture());
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(0, pageCaptor.getValue());
        assertEquals(defaultPageSize, pageSizeCaptor.getValue());
    }

    @Test
    void testGetPosts_ShouldReturnCustomListSize_IfSizeSpecified() throws Exception {
        Mockito.when(socialNetworkPostService.getPosts(Mockito.anyInt(), Mockito.anyInt())).thenReturn(
            new PageImpl<SocialNetworkPost>(TestUtils.generateSamplePosts(defaultPageSize))
        );

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts?page=5&pageSize=30")).andReturn();
        
        ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> pageSizeCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(socialNetworkPostService).getPosts(pageCaptor.capture(), pageSizeCaptor.capture());

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(5, pageCaptor.getValue());
        assertEquals(30, pageSizeCaptor.getValue());
    }

    @Test
    void testGetPosts_ShouldReturnErrorMessage_IfPageNotSpecified() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts")).andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }


    @Test
    void testGetTop10Posts_ShouldReturnTop10Posts() throws Exception {
        Mockito.when(socialNetworkPostService.getTop10Posts()).thenReturn(
            new PageImpl<SocialNetworkPost>(TestUtils.generateSamplePosts(10))
        );

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/top10")).andReturn();

        Mockito.verify(socialNetworkPostService, times(1)).getTop10Posts();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void testSavePost_ShouldSavePost_IfPostHasNoId() throws Exception {
        SocialNetworkPost postToSave = TestUtils.generateSamplePost(null);
        SocialNetworkPost savedPost = TestUtils.generateSamplePost(30L);
        Mockito.when(socialNetworkPostService.savePost(postToSave)).thenReturn(savedPost);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/posts")
            .headers(headers)
            .content(mapper.writeValueAsString(postToSave)))
        .andReturn();
        SocialNetworkPost resultResponse = mapper.readValue(
            result.getResponse().getContentAsString(), SocialNetworkPost.class);

        assertEquals(201, result.getResponse().getStatus());
        assertEquals(30, resultResponse.getId());
    }

    @Test
    void testSavePost_ShouldReturnErrorMessage_IfPostHasId() throws Exception {
        SocialNetworkPost postToSave = TestUtils.generateSamplePost(40L);
        
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/posts")
            .headers(headers)
            .content(mapper.writeValueAsString(postToSave)))
        .andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertEquals("Invalid body: new post cannot have an ID", result.getResponse().getContentAsString());
    }

    @Test
    void testUpdatePost_ShouldUpdatePost_IfPostHasId() throws Exception {
        SocialNetworkPost postToSave = TestUtils.generateSamplePost(35L);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/posts")
            .headers(headers)
            .content(mapper.writeValueAsString(postToSave)))
        .andReturn();

        ArgumentCaptor<SocialNetworkPost> postArgumentCaptor = ArgumentCaptor.forClass(SocialNetworkPost.class);
        verify(socialNetworkPostService, times(1)).savePost(postArgumentCaptor.capture());
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(postToSave, postArgumentCaptor.getValue());
    }

    @Test
    void testUpdatePost_ShouldReturnErororMessage_IfPostHasNoId() throws JsonProcessingException, Exception {
        SocialNetworkPost postToSave = TestUtils.generateSamplePost(null);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/posts")
            .headers(headers)
            .content(mapper.writeValueAsString(postToSave)))
        .andReturn();

    assertEquals(400, result.getResponse().getStatus());
    assertEquals("Invalid body: cannot update post without an ID", result.getResponse().getContentAsString());
    }
}
