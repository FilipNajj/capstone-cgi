package com.axlebank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.axlebank.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{

	List<Account> findAllByProfileId(int profileId);
}
