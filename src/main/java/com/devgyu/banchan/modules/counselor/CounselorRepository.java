package com.devgyu.banchan.modules.counselor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CounselorRepository extends JpaRepository<Counselor, Long> {
}
