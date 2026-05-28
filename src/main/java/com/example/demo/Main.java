package com.example.demo;

import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(BookService bookService) {
        return args -> {
            Book book = new Book();
            book.setTitle("Clean Code");
            book.setAuthor("Robert Martin");
            book.setIsbn("978-0132350884");
            book.setPrice(new BigDecimal("999.00"));

            bookService.save(book);

            System.out.println("--- Books in Database ---");
            bookService.findAll().forEach(System.out::println);
            System.out.println("-------------------------");
        };
    }
}
