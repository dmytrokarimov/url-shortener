package com.found.ee.shortener.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.found.ee.shortener.entity.CreateShortUrlRequest;
import com.found.ee.shortener.entity.ShortURL;
import com.found.ee.shortener.service.ShortenerService;

@RestController
@RequestMapping("/")
public class ShortenerController {
	
	private static Logger LOG = LoggerFactory.getLogger(ShortenerController.class);

	@Autowired
	ShortenerService service;

	@GetMapping("{shortUrl}")
	public ResponseEntity<String> redirectShortUrl(@PathVariable String shortUrl, ModelMap model) {
		Optional<ShortURL> shortURL = service.getById(shortUrl);
		
		if (shortURL.isPresent()) {
			try {
				URI url = new URI(shortURL.get().getLongUrl());
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setLocation(url);
				return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(httpHeaders).build();
			} catch (URISyntaxException e) {
				LOG.error(e.getMessage(), e);
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Short URL " + shortUrl + " is not found");
	}
	
	@PostMapping("")
	public ResponseEntity<String> createShortUrl(@RequestBody CreateShortUrlRequest request, HttpServletRequest httpRequest) {
		if (StringUtils.isBlank(request.getLongUrl())) {
			return ResponseEntity.badRequest().body("Field longUrl cannot be empty!");
		}
		
		ShortURL shortUrl = service.makeShort(request.getLongUrl());
		
		return ResponseEntity.ok(getBaseUrl(httpRequest) + "/" + shortUrl.getId());
	}
	
	private String getBaseUrl(HttpServletRequest req) {
	    return req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
	}
}
