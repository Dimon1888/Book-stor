package com.example.project.repository.book.spec;

import com.example.project.model.Book;
import com.example.project.repository.book.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    public static final String KEY = "isbn";

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        if (params == null || params.length == 0 || params[0] == null) {
            return (root, query, cb) -> null;
        }
        return (root, query, cb) -> cb.equal(root.get(KEY), params[0]);
    }
}
