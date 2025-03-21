package com.org701enti;

import com.org701enti.model.DocumentModel;
import com.org701enti.service.DocumentService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDateTime;

@SpringBootApplication
public class DocumentApplication {
    @Autowired
    private DocumentService documentService;

    public static void main(String[] args) {
        SpringApplication.run(DocumentApplication.class, args);
    }

    @PostConstruct
    public void init(){

    }
}
