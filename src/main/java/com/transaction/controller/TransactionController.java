package com.transaction.controller;

import com.transaction.dto.ResponseDTO;
import com.transaction.request.CreateTransactionRequest;
import com.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/transactionservice")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Async
    @RequestMapping(method = RequestMethod.GET, value = "/transaction/{id}")
    public CompletableFuture<ResponseDTO> getTransactionById(@PathVariable final Long id) throws Exception {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setPayload(transactionService.getTransactionById(id));
        return CompletableFuture.completedFuture(responseDTO) ;
    }

    @Async
    @RequestMapping(method = RequestMethod.PUT, value = "/transaction/{id}")
    public CompletableFuture<ResponseDTO> createTransaction(@PathVariable final Long id ,  @RequestBody @Valid CreateTransactionRequest request) throws Exception {
        ResponseDTO responseDTO = new ResponseDTO();
        transactionService.createTransaction(id,request);
        responseDTO.setPayload("Success");
        return CompletableFuture.completedFuture(responseDTO) ;
    }

    @Async
    @RequestMapping(method = RequestMethod.GET, value = "/type/{type}")
    public CompletableFuture<ResponseDTO> getByType(@PathVariable final String type ,
                                                    @RequestParam(value= "pageNumber", required = false) final Integer pageNumber,
                                                    @RequestParam(value= "pageSize", required = false) final Integer pageSize) throws Exception {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setPayload(transactionService.getByTransactionType(type,pageNumber,pageSize));
        return CompletableFuture.completedFuture(responseDTO) ;
    }

    @Async
    @RequestMapping(method = RequestMethod.GET, value = "/sum/{id}")
    public CompletableFuture<ResponseDTO> getSum(@PathVariable final Long id) throws Exception {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setPayload(transactionService.getSum(id));
        return CompletableFuture.completedFuture(responseDTO) ;
    }

}
