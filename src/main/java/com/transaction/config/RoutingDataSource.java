package com.transaction.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<Route> ctx = new ThreadLocal<>();

    public enum Route {
        PRIMARY
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return ctx.get() ;
    }
}