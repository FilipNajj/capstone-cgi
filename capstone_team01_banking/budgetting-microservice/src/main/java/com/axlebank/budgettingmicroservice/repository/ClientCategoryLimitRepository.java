package com.axlebank.budgettingmicroservice.repository;

import com.axlebank.budgettingmicroservice.models.ClientCategoryLimit;
import com.axlebank.budgettingmicroservice.models.ClientCustomInfoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ClientCategoryLimitRepository extends JpaRepository<ClientCategoryLimit, ClientCustomInfoId> {


}
