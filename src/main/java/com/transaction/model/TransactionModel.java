package com.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction")
public class TransactionModel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id  ;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "type")
    private String type;

    @Column(name = "parent_id")
    private Long parentId;

}
