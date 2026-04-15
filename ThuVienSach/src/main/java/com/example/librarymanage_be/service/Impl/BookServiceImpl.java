package com.example.librarymanage_be.service.Impl;

import com.example.librarymanage_be.dto.request.BookRequest;
import com.example.librarymanage_be.dto.response.BookResponse;
import com.example.librarymanage_be.elasticsearch.document.BookDocument;
import com.example.librarymanage_be.elasticsearch.service.ElasticSearchBookService;
import com.example.librarymanage_be.entity.Author;
import com.example.librarymanage_be.entity.Book;
import com.example.librarymanage_be.entity.BookAuthor;
import com.example.librarymanage_be.enums.BookStatus;
import com.example.librarymanage_be.mapper.BookMapper;
import com.example.librarymanage_be.repo.*;
import com.example.librarymanage_be.service.AuthorService;
import com.example.librarymanage_be.service.BookService;
import com.example.librarymanage_be.service.CategoryService;
import com.example.librarymanage_be.service.PublisherService;
import com.example.librarymanage_be.specification.BookSpecification;
import com.example.librarymanage_be.utils.EntityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.librarymanage_be.specification.BookSpecification.idIn;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final ElasticSearchBookService elasticSearchBookService;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final PublisherService publisherService;
    private final BookAuthorRepository bookAuthorRepository;

    @Override
    public BookResponse create(BookRequest bookRequest) {
        log.info("[BOOK] Creating a new book with title={}", bookRequest.getTitle());
        Book bookMap = bookMapper.toEntity(bookRequest);
        bookMap.setAvailableQuantity(bookRequest.getTotalQuantity());
        bookMap.setPrice(bookRequest.getPrice());
        if (bookMap.getAvailableQuantity() > 0) {
            bookMap.setBookStatus(BookStatus.AVAILABLE);
        } else {
            bookMap.setBookStatus(BookStatus.INACTIVE);
        }
        bookMap.setCategory(categoryService.findEntityById(bookRequest.getCategoryId()));
        bookMap.setPublisher(publisherService.findEntityById(bookRequest.getPublisherId()));

        List<Integer> authorIds = bookRequest.getAuthorIds()
                .stream()
                .distinct()
                .toList();

        List<Author> authors = authorService.findListAuthorsById(authorIds);

        List<BookAuthor> bookAuthors = authors.stream()
                .map(author -> {
                    BookAuthor ba = new BookAuthor();
                    ba.setBook(bookMap);
                    ba.setAuthor(author);
                    return ba;
                })
                .toList();
        bookMap.setBookAuthors(bookAuthors);
        Book savedBook = bookRepository.save(bookMap);
        elasticSearchBookService.save(savedBook);
        log.info("[BOOK] Created book with name={}", savedBook.getTitle());
        return bookMapper.toResponse(savedBook);
    }


    @Override
    public Page<BookResponse> getBooks(Pageable pageable) {
        log.info("[BOOK] Getting books with page={},size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Book> books = bookRepository.findAll(pageable);
        log.info("[BOOK] Found {} publishers", books.getTotalElements());
        return books.map(bookMapper::toResponse);
    }

    @Override
    public Book getEntityById(Integer id) {
        return EntityUtils.getOrThrow(
                bookRepository.findById(id),
                "Book not found with id=" + id
        );
    }

    @Override
    @Cacheable(value = "books", key = "#id")
    public BookResponse findById(Integer id) {
        Book book = getEntityById(id);
        log.info("[BOOK] Found successfully a new Author with id={}", book.getBookId());
        return bookMapper.toResponse(book);
    }

    @Override
    @CachePut(value = "books", key = "#bookId")
    public BookResponse update(Integer bookId, BookRequest bookRequest) {
        log.info("[BOOK] Updating Author with id={}", bookId);
        Book bookExist = EntityUtils.getOrThrow(bookRepository.findById(bookId), "Book not found");
        bookMapper.updateBook(bookExist, bookRequest);
        if (bookRequest.getCategoryId() != null) {
            bookExist.setCategory(categoryService.findEntityById(bookRequest.getCategoryId()));
        }
        if (bookRequest.getPublisherId() != null) {
            bookExist.setPublisher(publisherService.findEntityById(bookRequest.getPublisherId()));
        }
        if (bookRequest.getAuthorIds() != null) {
            List<Integer> authorIds = bookRequest.getAuthorIds()
                    .stream()
                    .distinct()
                    .toList();
            bookAuthorRepository.deleteByBook_BookId(bookId);
            bookAuthorRepository.flush();
            //Lấy author mới
            List<Author> authors = authorService.findListAuthorsById(authorIds);
            List<BookAuthor> bookAuthors = authors.stream().map(author -> {
                BookAuthor ba = new BookAuthor();
                ba.setBook(bookExist);
                ba.setAuthor(author);
                return ba;
            }).toList();
            bookAuthorRepository.saveAll(bookAuthors);
            bookExist.setBookAuthors(bookAuthors);
        }
        bookExist.setPrice(bookRequest.getPrice());
        if (bookRequest.getTotalQuantity() != null) {
            Integer borrowed = bookExist.getTotalQuantity() - bookExist.getAvailableQuantity();
            bookExist.setTotalQuantity(bookRequest.getTotalQuantity());
            bookExist.setAvailableQuantity(bookRequest.getTotalQuantity() - borrowed);
            if (bookExist.getAvailableQuantity() > 0) {
                bookExist.setBookStatus(BookStatus.AVAILABLE);
            } else {
                bookExist.setBookStatus(BookStatus.OUT_OF_STOCK);
            }
        }
        Book updatedBook = bookRepository.save(bookExist);
        elasticSearchBookService.save(updatedBook);
        log.info("[BOOK] Updated successfully a new book with id={},name={}", updatedBook.getBookId(), updatedBook.getTitle());
        return bookMapper.toResponse(updatedBook);
    }

    @Override
    @CacheEvict(value = "books", key = "#bookId")
    public void delete(Integer bookId) {
        Book bookExisted = EntityUtils.getOrThrow(bookRepository.findById(bookId), "Book not found");
        bookRepository.delete(bookExisted);
        elasticSearchBookService.deleteById(bookId);
        log.info("[BOOK] Deleted book with id={}", bookId);
    }

    @Override
    public Page<BookResponse> searchBooks(String keyword, String categoryName, String authorName, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            Specification<Book> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
            if (categoryName != null) {
                spec = spec.and(BookSpecification.hasCategory(categoryName));
            }
            if (authorName != null) {
                spec = spec.and(BookSpecification.hasAuthor(authorName));
            }
            return bookRepository.findAll(spec, pageable).map(bookMapper::toResponse);
        }

        Page<BookDocument> esResult = elasticSearchBookService.search(keyword, pageable);
        if (esResult.isEmpty()) {
            return Page.empty(pageable);
        }
        //Lấy id
        List<Integer> ids = esResult.getContent()
                .stream()
                .map(BookDocument::getId)
                .toList();

        Specification<Book> spec = Specification.where(idIn(ids));

//        Specification<Book> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        if (categoryName != null) {
            spec = spec.and(BookSpecification.hasCategory(categoryName));
        }
        if (authorName != null) {
            spec = spec.and(BookSpecification.hasAuthor(authorName));
        }
        List<Book> books = bookRepository.findAll(spec);

        //GIỮ ORDER ES
        Map<Integer, Integer> orderMap = new HashMap<>();
        for (int i = 0; i < ids.size(); i++) {
            orderMap.put(ids.get(i), i);
        }
        books.sort(Comparator.comparingInt(
                b -> orderMap.getOrDefault(b.getBookId(), Integer.MAX_VALUE)
        ));

        //MAP RESPONSE
        List<BookResponse> responses = books.stream()
                .map(bookMapper::toResponse)
                .toList();

        // RETURN PAGE
        return new PageImpl<>(
                responses,
                pageable,
                esResult.getTotalElements()
        );
    }
}
