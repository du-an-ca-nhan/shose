package com.example.shose.server.repository;

import com.example.shose.server.dto.request.category.FindCategoryRequest;
import com.example.shose.server.dto.response.CategoryResponse;
import com.example.shose.server.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query(value = """
            SELECT
                ROW_NUMBER() OVER (ORDER BY m.last_modified_date DESC ) AS stt,
                m.id AS id,
                m.name AS name,
                m.status AS status,
                m.created_date AS createdDate,
                m.last_modified_date AS lastModifiedDate
            FROM category m
            WHERE 
                ( :#{#req.name} IS NULL 
                    OR :#{#req.name} LIKE '' 
                    OR name LIKE %:#{#req.name}% ) 
            AND 
                ( :#{#req.status} IS NULL 
                    OR :#{#req.status} LIKE '' 
                    OR status LIKE :#{#req.status} )
            GROUP BY m.id
            ORDER BY m.last_modified_date DESC  
            """, nativeQuery = true)
    List<CategoryResponse> getAll( @Param("req") FindCategoryRequest req);

    @Query("SELECT a FROM Category a WHERE a.name =:name")
    Category getOneByName(@Param("name") String name);

    @Query("SELECT s FROM Category s WHERE s.name =:name AND s.id <> :id")
    Category getByNameExistence(@Param("name") String name,
                                @Param("id") String id);


}
