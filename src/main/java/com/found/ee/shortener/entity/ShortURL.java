package com.found.ee.shortener.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortURL {

    @Id
    private String id;
    
    private String longUrl;
    
    @Default
    private LocalDateTime creatingDate = LocalDateTime.now();
}
