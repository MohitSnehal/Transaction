package com.transaction.service;

import com.transaction.request.CreateTransactionRequest;
import com.transaction.response.GetSumResponse;
import com.transaction.response.PaginatedTransactionResponse;
import com.transaction.response.TransactionResponse;

public interface TransactionService {

    TransactionResponse getTransactionById(Long id)  throws Exception;
    void createTransaction(Long id, CreateTransactionRequest request);
    GetSumResponse getSum(Long id) throws Exception;
    PaginatedTransactionResponse getByTransactionType(String type, Integer pageNumber, Integer pageSize) throws Exception;

}
