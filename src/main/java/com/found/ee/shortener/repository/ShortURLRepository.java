package com.found.ee.shortener.repository;

import com.found.ee.shortener.entity.ShortURL;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortURLRepository extends CrudRepository<ShortURL, String> {

}
