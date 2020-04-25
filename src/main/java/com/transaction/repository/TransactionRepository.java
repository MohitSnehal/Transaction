package com.transaction.repository;

import com.transaction.model.TransactionModel;
import com.transaction.response.TransactionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.LinkedList;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionModel, Long>, TransactionRepositoryCustom {

    @Query("SELECT tm from TransactionModel tm WHERE tm.parentId = ?1")
    LinkedList<TransactionModel> findByParentId(Long currTransactionId);
}
