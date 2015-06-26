package com.proto.repository;

import com.proto.entity.AppUser;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Repository to access the users
 */
public interface UserRepository extends PagingAndSortingRepository<AppUser, String>, JpaSpecificationExecutor<AppUser> {
    List<AppUser> findByAccountIdentifier(String accountIdentifier);

    void deleteByAccountIdentifier(String accountIdentifier);
}
