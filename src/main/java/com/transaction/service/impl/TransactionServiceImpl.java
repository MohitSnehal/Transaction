package com.transaction.service.impl;

import com.transaction.constants.Constant;
import com.transaction.exceptions.BadRequestException;
import com.transaction.exceptions.InvalidRequestException;
import com.transaction.model.TransactionModel;
import com.transaction.repository.TransactionRepository;
import com.transaction.request.CreateTransactionRequest;
import com.transaction.response.GetSumResponse;
import com.transaction.response.PaginatedTransactionResponse;
import com.transaction.response.TransactionResponse;
import com.transaction.service.TransactionService;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public TransactionResponse getTransactionById(Long id) throws Exception{
        if(null == id){
            throw new BadRequestException("Transaction Id should not be null.");
        }

        Optional<TransactionModel> transactionModel = transactionRepository.findById(id);
        if(!transactionModel.isPresent()){
            throw new EntityNotFoundException("Entity not found for id : " + id);
        }

        return TransactionResponse.from(transactionModel.get());
    }

    @Override
    public void createTransaction(Long id, CreateTransactionRequest request) throws Exception{
        TransactionModel transactionModel = null;
        if(null == id){
            transactionModel = TransactionModel.builder()
                    .amount(request.getAmount())
                    .type(request.getType())
                    .parentId(request.getParentId())
                    .build();
        } else {
            Optional<TransactionModel> optionalTransactionModel = transactionRepository.findById(id);
            if(optionalTransactionModel.isPresent()){
                transactionModel = optionalTransactionModel.get();
                transactionModel.setAmount(request.getAmount());
                transactionModel.setParentId(request.getParentId());
                transactionModel.setType(request.getType());
            } else{
                throw new EntityNotFoundException("No transaction found for id : " + id);
            }
        }
        if(request.getParentId() != null){
            checkParentTransactionExists(request.getParentId());
        }
        transactionRepository.save(transactionModel);
    }

    private void checkParentTransactionExists(Long parentId) throws Exception {
        if(!transactionRepository.findById(parentId).isPresent()) {
            throw new InvalidRequestException("Invalid parent transaction id");
        }
    }

    @Override
    public GetSumResponse getSum(Long id) throws Exception {
        if(null == id){
            throw new BadRequestException("Transaction Id should not be null.");
        }

        Optional<TransactionModel> transactionModel = transactionRepository.findById(id);
        if(!transactionModel.isPresent()){
            throw new EntityNotFoundException("Entity not found for id : " + id);
        }

        return GetSumResponse.builder()
                .sum(getTotalSumFromRoot(transactionModel.get()))
                .build();
    }

    @Override
    public PaginatedTransactionResponse getByTransactionType(String type, Integer pageNumber, Integer pageSize) throws Exception {
        if(null == type || type.isEmpty()){
            throw new BadRequestException("Type can not be null.");
        }

        int finalPageNumber = pageNumber != null ? pageNumber : Constant.DEFAULT_PAGE_NUMBER;
        int finalPageSize = pageSize != null ? pageSize : Constant.DEFAULT_PAGE_SIZE;
        List<TransactionModel> transactionModels = transactionRepository.findByType(type,finalPageNumber,finalPageSize);

        return PaginatedTransactionResponse.builder()
                .transactionResponses( (transactionModels != null && !transactionModels.isEmpty())
                        ? TransactionResponse.from(transactionModels)
                        : new LinkedList<TransactionResponse>())
                .pageNumber(finalPageNumber)
                .pageSize(finalPageSize)
                .build();
    }

    //performing bfs for each node
    private Double getTotalSumFromRoot(TransactionModel transactionModel) {
        LinkedList<Long> transactionQueue = new LinkedList<>();
        transactionQueue.add(transactionModel.getId());
        HashSet<Long> visitedSet = new HashSet<>();

        double totalSum = transactionModel.getAmount();
        while(!transactionQueue.isEmpty()){
            Long currTransactionId = transactionQueue.remove();
            visitedSet.add(currTransactionId);

            List<TransactionModel> childTransactions = transactionRepository.findByParentId(currTransactionId);
            if(childTransactions != null){
                for(TransactionModel transaction : childTransactions){
                    if(!visitedSet.contains(transaction.getId())){
                        totalSum += transaction.getAmount();
                        transactionQueue.add(transaction.getId());
                    }
                }
            }
        }
        return totalSum;
    }
}
