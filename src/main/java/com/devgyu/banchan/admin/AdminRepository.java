package com.devgyu.banchan.admin;

import com.devgyu.banchan.admin.query.AdminQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AdminRepository extends JpaRepository<Admin, Long>, AdminQueryRepository {
}
