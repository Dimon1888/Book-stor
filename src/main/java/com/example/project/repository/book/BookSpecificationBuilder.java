package com.example.project.repository.book;

import com.example.project.dto.BookSearchParametersDto;
import com.example.project.model.Book;
import com.example.project.repository.book.spec.AuthorSpecificationProvider;
import com.example.project.repository.book.spec.IsbnSpecificationProvider;
import com.example.project.repository.book.spec.TitleSpecificationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = (root, query, cb) -> null;

        if (searchParameters.author() != null && !searchParameters.author().isEmpty()) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(AuthorSpecificationProvider.KEY)
                    .getSpecification(new String[]{searchParameters.author()}));
        }
        if (searchParameters.title() != null && !searchParameters.title().isEmpty()) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(TitleSpecificationProvider.KEY)
                    .getSpecification(new String[]{searchParameters.title()}));
        }
        if (searchParameters.isbn() != null && !searchParameters.isbn().isEmpty()) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(IsbnSpecificationProvider.KEY)
                    .getSpecification(new String[]{searchParameters.isbn()}));
        }
        return spec;
    }
}
