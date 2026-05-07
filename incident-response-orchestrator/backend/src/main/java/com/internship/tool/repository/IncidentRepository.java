package com.internship.tool.repository;

import com.internship.tool.entity.Incident;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface IncidentRepository
        extends JpaRepository<Incident, UUID> {

    // FIND BY STATUS

    @EntityGraph(attributePaths = {})
    @Query("""
            SELECT i
            FROM Incident i
            WHERE i.status = :status
            """)
    List<Incident> findByStatus(
            @Param("status") String status
    );

    // SEARCH BY TITLE

    @EntityGraph(attributePaths = {})
    @Query("""
            SELECT i
            FROM Incident i
            WHERE LOWER(i.title)
            LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<Incident> searchByTitle(
            @Param("keyword") String keyword
    );

    // DATE RANGE QUERY

    @EntityGraph(attributePaths = {})
    @Query("""
            SELECT i
            FROM Incident i
            WHERE i.createdAt
            BETWEEN :start AND :end
            """)
    List<Incident> findByDateRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // GLOBAL SEARCH

    @EntityGraph(attributePaths = {})
    @Query("""
            SELECT i
            FROM Incident i
            WHERE LOWER(i.title)
            LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(i.description)
            LIKE LOWER(CONCAT('%', :q, '%'))
            """)
    List<Incident> search(
            @Param("q") String q
    );

    // FIND BY PRIORITY

    @EntityGraph(attributePaths = {})
    @Query("""
            SELECT i
            FROM Incident i
            WHERE i.priority = :priority
            """)
    List<Incident> findByPriority(
            @Param("priority") String priority
    );
}