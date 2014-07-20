package br.com.dextra.components.querypaginator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.Query;

class PaginatorInvocationHandler implements InvocationHandler {
    private Pagination pagination;

    private Query realQuery;

    private Query countQuery;

    PaginatorInvocationHandler(Pagination pagination, Query realQuery,
            Query countQuery) {
        this.pagination = pagination;
        this.realQuery = realQuery;
        this.countQuery = countQuery;
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        String methodName = method.getName();
        if ("getPaginatedResult".equals(methodName)) {
            return getPaginatedResult();
        }

        // avoid getSingleResult and getResultList
        if (!methodName.startsWith("get")) {
            method.invoke(countQuery, args);
        }

        return method.invoke(realQuery, args);
    }

    @SuppressWarnings( { "unchecked", "rawtypes" })
    private Object getPaginatedResult() {
    	if (pagination.isEnabled()) {
            int offset = PaginatorUtils.getOffset(pagination);
            int pageSize = pagination.getPageSize();

            realQuery.setFirstResult(offset);
            realQuery.setMaxResults(pageSize);
    	}
    	
    	List result = realQuery.getResultList();

        long totalRegs = getResultCount();
        long pageCount = getPageCount(pagination, totalRegs);
        
        return new PaginatedResult(pagination, result, pageCount, totalRegs);
    }

    private long getPageCount(Pagination pagination, long totalRegs) {
        return PaginatorUtils.getPageCount(totalRegs, pagination);
    }
    
    private long getResultCount() {
        return ((Number) countQuery.getSingleResult()).longValue();
    }
}
