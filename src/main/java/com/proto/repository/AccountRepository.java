package com.proto.repository;

import com.proto.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Repository to access the accounts
 */
public interface AccountRepository extends PagingAndSortingRepository<Account, String>, JpaSpecificationExecutor<Account> {
    Page<Account> findAll(Pageable pageable);

    List<Account> findAll();

    Account findOne(String id);
}
