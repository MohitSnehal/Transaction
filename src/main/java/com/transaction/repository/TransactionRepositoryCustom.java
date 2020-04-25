package com.transaction.repository;


import com.transaction.model.TransactionModel;

import java.util.List;

public interface TransactionRepositoryCustom {

    List<TransactionModel> findByType(String type, int pageNumber, int pageSize);
}
