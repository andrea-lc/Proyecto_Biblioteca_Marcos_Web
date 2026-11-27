package com.bibliot.gestion_biblioteca.service;

import com.bibliot.gestion_biblioteca.entities.Libro;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "bibliot.search.reindex-on-startup", havingValue = "true", matchIfMissing = true)
public class LibroIndexInitializer implements ApplicationRunner {

    private final EntityManagerFactory entityManagerFactory;

    public LibroIndexInitializer(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Search.mapping(entityManagerFactory)
                .scope(Libro.class)
                .massIndexer()
                .threadsToLoadObjects(2)
                .startAndWait();
    }
}
