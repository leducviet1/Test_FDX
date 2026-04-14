package com.example.librarymanage_be.elasticsearch.service;

import com.example.librarymanage_be.elasticsearch.document.BookDocument;
import com.example.librarymanage_be.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ElasticSearchBookService {
    private final ElasticsearchOperations elasticsearchOperations;

    public void save(Book book) {
        elasticsearchOperations.save(toDocument(book));
    }

    public void deleteById(Integer bookId) {
        elasticsearchOperations.delete(String.valueOf(bookId), BookDocument.class);
    }

    public Page<BookDocument> search(String keyword, Pageable pageable) {
        NativeQuery query = NativeQuery.builder().withQuery(q -> q
                .bool(b -> b
                        .must(m -> m
                                .multiMatch(mm -> mm
                                        .query(keyword).fields("title",
                                                "authorNames",
                                                "description",
                                                "categoryName",
                                                "publisherName")
                                        .fuzziness("AUTO"))))).withPageable(pageable).build();
        SearchHits<BookDocument> hits = elasticsearchOperations.search(query,BookDocument.class);
        List<BookDocument> content = hits.stream().map(SearchHit::getContent).toList();

        return new PageImpl<>(content, pageable, hits.getTotalHits());
    }

    private BookDocument toDocument(Book book) {
        BookDocument document = new BookDocument();
        document.setId(book.getBookId());
        document.setTitle(book.getTitle());
        document.setPrice(book.getPrice());
        document.setDescription(book.getDescription());
        document.setCategoryName(book.getCategory() == null ? null : book.getCategory().getCategoryName());
        document.setPublisherName(book.getPublisher() == null ? null : book.getPublisher().getPublisherName());
        document.setAuthorNames(book.getBookAuthors() == null
                ? List.of()
                : book.getBookAuthors()
                .stream()
                .map(bookAuthor -> bookAuthor.getAuthor().getAuthorName())
                .toList());
        return document;
    }
}
