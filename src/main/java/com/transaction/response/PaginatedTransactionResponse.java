package com.transaction.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedTransactionResponse {
    private List<TransactionResponse> transactionResponses;
    private int pageNumber;
    private int pageSize;
}
