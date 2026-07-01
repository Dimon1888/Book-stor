package com.example.project.repository.book.spec;

import com.example.project.model.Book;
import com.example.project.repository.book.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "title";
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, cb) -> cb.like(
                cb.lower(root.get("title")), "%" + params[0].toLowerCase() + "%"
        );
    }
}
