package com.found.ee.shortener;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.found.ee.shortener.controller.ShortenerController;
import com.found.ee.shortener.entity.CreateShortUrlRequest;
import com.found.ee.shortener.entity.ShortURL;
import com.found.ee.shortener.repository.ShortURLRepository;
import com.found.ee.shortener.service.ShortenerService;

@RunWith(SpringRunner.class)
@WebMvcTest(ShortenerController.class)
@EnableSpringDataWebSupport
public class ShortenerControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
    @MockBean
    ShortURLRepository repository;
    

    @Autowired
    private ShortenerController shortenerController;
    
    @Configuration
    @Import({ShortenerService.class, ShortenerController.class})
    static class Config{}
    
	@Test
	public void checkRedirect() throws Exception {
		ShortURL shortURL = ShortURL.builder().id("id").longUrl("http://looongurl").build();
        given(this.repository.findById(shortURL.getId())).willReturn(Optional.of(shortURL));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/{shortUrl}", shortURL.getId()))
        	.andExpect(status().is3xxRedirection())
        	.andExpect(redirectedUrl(shortURL.getLongUrl()))
            .andDo(print());
	}
	
	@Test
	public void checkNewShortLink() throws Exception {
		ShortURL shortURL = ShortURL.builder().id("id").longUrl("http://looongurl").build();
        given(this.repository.save(Mockito.any(ShortURL.class))).willReturn(shortURL);

        CreateShortUrlRequest request = new CreateShortUrlRequest("http://looongurl");
		
		MockHttpServletRequest httpRequest = new MockHttpServletRequest();
		
		ResponseEntity<String> response = shortenerController.createShortUrl(request, httpRequest);
		Assert.assertEquals(response.getBody(), "http://localhost:80/" + shortURL.getId());
	}
	
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
