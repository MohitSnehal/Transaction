package com.transaction.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
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
