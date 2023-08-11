/*
package com.maple;

import com.maple.entity.Letter;
import com.maple.repository.user.LetterRepository;
import com.maple.repository.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initData(LetterRepository letterRepository, UserRepository userRepository) {
        return args -> {
            IntStream.rangeClosed(1, 30).forEach(i -> {
                Letter letter = Letter.builder()
                        .senderName("단풍이")
                        .content("안녕하세요, 당신은 편지를 못받았어요.")
                        .createdAt(i)
                        .build();
                letterRepository.save(letter);
            });
        };
    }
}*/
