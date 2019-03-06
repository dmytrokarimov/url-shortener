package com.found.ee.shortener.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.found.ee.shortener.entity.ShortURL;
import com.found.ee.shortener.repository.ShortURLRepository;

@Component
public class ShortenerService {
	
	@Autowired
	ShortURLRepository repository;
	
	public ShortURL makeShort(String url) {
		ShortURL shortURL = new ShortURL();
		shortURL.setId(UUID.randomUUID().toString().replace("-", "").substring(0, 5));
		shortURL.setLongUrl(url);
		
		return repository.findById(shortURL.getId())
			.map(existEntity -> makeShort(url)) //if exist - re-create
			.orElseGet(() -> repository.save(shortURL));
	}
	
	public Optional<ShortURL> getById(String id) {
		return repository.findById(id);
	}
}
