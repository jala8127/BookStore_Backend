package com.example.BookStore.repository;
import com.example.BookStore.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    // Optional: Add custom query methods if you want

}