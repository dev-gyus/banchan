package com.devgyu.banchan.modules.category;

import com.devgyu.banchan.modules.category.query.CategoryQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryQueryRepository {
    List<Category> findAllByNameIn(List<String> names);

    Category findByName(String category);
}
