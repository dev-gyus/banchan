package com.devgyu.banchan.items;

import com.devgyu.banchan.items.query.ItemOptionQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface ItemOptionRepository extends JpaRepository<ItemOption, Long>, ItemOptionQueryRepository {
    List<ItemOption> findAllByNameIn(List<String> name);
}
