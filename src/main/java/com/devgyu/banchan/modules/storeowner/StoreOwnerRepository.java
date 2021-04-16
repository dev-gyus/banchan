package com.devgyu.banchan.modules.storeowner;

import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.modules.storeowner.query.StoreOwnerQueryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface StoreOwnerRepository extends JpaRepository<StoreOwner, Long>, StoreOwnerQueryRepository {
    StoreOwner findByEmail(String email);

}
