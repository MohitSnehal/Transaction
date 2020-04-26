package com.transaction.service.impl;

import ch.qos.logback.core.encoder.EchoEncoder;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public TransactionResponse getTransactionById(Long id) throws Exception{
        if(null == id){
            log.error("BadRequestException id is null.");
            throw new BadRequestException("Transaction Id should not be null.");
        }

        Optional<TransactionModel> transactionModel = transactionRepository.findById(id);
        if(!transactionModel.isPresent()){
            throw new EntityNotFoundException("Entity not found for id : " + id);
        }

        return TransactionResponse.transform(transactionModel.get());
    }

    @Override
    public void createTransaction(Long id, CreateTransactionRequest request) throws Exception{
        log.info("started createTransaction :: {} {}",id,request);
        if(null == id){
            log.error("Transaction id found null {} {}",id,request);
            throw new BadRequestException("Transaction id can not be null");
        }
        Optional<TransactionModel> optionalTransactionModel = transactionRepository.findById(id);
        if(optionalTransactionModel.isPresent()){
            log.error("Transaction already exists {} {}",id,request);
            throw new InvalidRequestException("Transaction already exists");
        }
        createTransactionHelper(id,request);
    }

    private void createTransactionHelper(Long id, CreateTransactionRequest request) throws Exception {
        try{
            TransactionModel transactionModel = TransactionModel.builder()
                    .id(id)
                    .amount(request.getAmount())
                    .type(request.getType())
                    .parentId(request.getParentId())
                    .build();
            transactionRepository.save(transactionModel);
        } catch (DataIntegrityViolationException e){
            log.error("Foreign key constraint violation for {} {} {}",id,request,e);
            throw new InvalidRequestException("Parent id is invalid");
        } catch (Exception e){
            log.error("Error for {} {} {}",id,request,e);
            throw e;
        }
    }

    @Override
    public GetSumResponse getSum(Long id) throws Exception {
        if(null == id){
            log.error("Transaction Id should not be null. {}");
            throw new BadRequestException("Transaction Id should not be null.");
        }

        Optional<TransactionModel> transactionModel = transactionRepository.findById(id);
        if(!transactionModel.isPresent()){
            log.error("Entity not found for id : " + id);
            throw new EntityNotFoundException("Entity not found for id : " + id);
        }

        return GetSumResponse.builder()
                .sum(getTotalSumFromRoot(transactionModel.get()))
                .build();
    }

    @Override
    public PaginatedTransactionResponse getByTransactionType(String type, Integer pageNumber, Integer pageSize) throws Exception {
        if(null == type || type.isEmpty()){
            log.error("Type can not be null.");
            throw new BadRequestException("Type can not be null.");
        }

        int finalPageNumber = pageNumber != null ? pageNumber : Constant.DEFAULT_PAGE_NUMBER;
        int finalPageSize = pageSize != null ? pageSize : Constant.DEFAULT_PAGE_SIZE;
        List<TransactionModel> transactionModels = transactionRepository.findByType(type,finalPageNumber,finalPageSize);

        return PaginatedTransactionResponse.builder()
                .transactionResponses( (transactionModels != null && !transactionModels.isEmpty())
                        ? TransactionResponse.transform(transactionModels)
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
