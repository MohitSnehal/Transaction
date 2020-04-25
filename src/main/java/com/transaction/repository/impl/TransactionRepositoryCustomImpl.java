package com.transaction.repository.impl;

import com.transaction.model.TransactionModel;
import com.transaction.repository.TransactionRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class TransactionRepositoryCustomImpl implements TransactionRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TransactionModel> findByType(String type, int pageNumber, int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TransactionModel> paginatedQuery = criteriaBuilder.createQuery(TransactionModel.class);
        Root root = paginatedQuery.from(TransactionModel.class);

        //added % so that more results show up
        Predicate predicate = criteriaBuilder.like(root.get("type"), "%" + type + "%");
        paginatedQuery.where(predicate);
        TypedQuery<TransactionModel> typedPaginatedQuery = entityManager.createQuery(paginatedQuery);
        typedPaginatedQuery.setFirstResult((pageNumber - 1) * pageSize);
        typedPaginatedQuery.setMaxResults(pageSize);

        return typedPaginatedQuery.getResultList();
    }
}
