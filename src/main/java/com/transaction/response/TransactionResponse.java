package com.transaction.response;

import com.transaction.model.TransactionModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private Long id;
    private Double amount;
    private String type;
    private Long parentId;

    public static List<TransactionResponse> from(List<TransactionModel> transactionModelList) {
        return transactionModelList.stream()
                .map(transactionModel -> from(transactionModel))
                .collect(Collectors.toList());
    }

    public static TransactionResponse from(TransactionModel transactionModel) {
        return TransactionResponse.builder()
                .id(transactionModel.getId())
                .amount(transactionModel.getAmount())
                .type(transactionModel.getType())
                .parentId(transactionModel.getParentId())
                .build();
    }
}
