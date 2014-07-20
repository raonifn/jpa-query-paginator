package br.com.dextra.components.querypaginator;

import java.lang.reflect.Proxy;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class Paginator {

    private EntityManager entityManager;

    private Pagination pagination;

    private Query realQuery;

    private Query countQuery;

    public Paginator(EntityManager entityManager, Pagination pagination) {
        this.entityManager = entityManager;
        this.pagination = pagination;
    }

    @SuppressWarnings("rawtypes")
	public PaginatedQuery createQuery(String queryString) {
        String countQueryString = PaginatorUtils.mountCountQuery(queryString);

        this.realQuery = entityManager.createQuery(queryString);
        this.countQuery = entityManager.createQuery(countQueryString);

        PaginatorInvocationHandler ih = new PaginatorInvocationHandler(
                pagination, realQuery, countQuery);
        return (PaginatedQuery) Proxy.newProxyInstance(getClass()
                .getClassLoader(), new Class[] { PaginatedQuery.class }, ih);
    }
}
