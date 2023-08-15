package com.maple.config;

import com.maple.entity.ConsolationLetter;
import com.maple.repository.consolationLetter.ConsolationLetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DatabaseInitializerConfig {

    private final ConsolationLetterRepository consolationLetterRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            for (int i = 0; i < 30; i++) {
                ConsolationLetter letter = ConsolationLetter.builder()
                        .senderName("단풍이")
                        .content("안녕하세요!!")
                        .build();
                consolationLetterRepository.save(letter);
            }
        };
    }
}
