package io.github.raonifn.jpaquerypaginator;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import io.github.raonifn.jpaquerypaginator.PaginatedQuery;
import io.github.raonifn.jpaquerypaginator.PaginatedResult;
import io.github.raonifn.jpaquerypaginator.Pagination;
import io.github.raonifn.jpaquerypaginator.Paginator;
import io.github.raonifn.jpaquerypaginator.PaginatorUtils;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Test;

public class PaginatorTest {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testPaginator() {
        String queryString = "from TesteBean";
        String countQueryString = PaginatorUtils.mountCountQuery(queryString);

        long totalRegs = 20l;
        Pagination pagination = new Pagination(20, 3);

        int paramCount = 1;
        Object paramValue = new Object();

        ArrayList<Object> retornList = new ArrayList<Object>();

        Query realQuery = createMock(Query.class);
        expect(realQuery.setFirstResult(PaginatorUtils.getOffset(pagination)))
                .andReturn(realQuery).once();
        expect(realQuery.setMaxResults(pagination.getPageSize())).andReturn(
                realQuery).once();
        expect(realQuery.setParameter(paramCount, paramValue)).andReturn(
                realQuery).once();
        expect(realQuery.getResultList()).andReturn(retornList).once();

        Query countQuery = createMock(Query.class);
        expect(countQuery.setParameter(paramCount, paramValue)).andReturn(
                realQuery).once();
        expect(countQuery.getSingleResult()).andReturn(totalRegs).once();

        EntityManager em = createMock(EntityManager.class);
        expect(em.createQuery(queryString)).andReturn(realQuery).once();
        expect(em.createQuery(countQueryString)).andReturn(countQuery).once();

        replay(realQuery, countQuery, em);

        Paginator paginator = new Paginator(em, pagination);
        PaginatedQuery paginatedQuery = paginator.createQuery(queryString);
        paginatedQuery.setParameter(paramCount, paramValue);
        PaginatedResult<Object> paginatedResult = paginatedQuery
                .getPaginatedResult();

        assertEquals(PaginatorUtils.getPageCount(totalRegs, pagination),
                paginatedResult.getPageCount());
        assertEquals(pagination, paginatedResult.getPagination());
        assertEquals(retornList, paginatedResult.getResult());

        verify(realQuery);
        verify(countQuery);
        verify(em);
    }

    @SuppressWarnings({ "rawtypes" })
    @Test
    public void testPaginatorWithoutPagination() {
        String queryString = "from TesteBean";
        String countQueryString = PaginatorUtils.mountCountQuery(queryString);

        Pagination pagination = new Pagination(20, 3);

        ArrayList<Object> retornList = new ArrayList<Object>();

        Query realQuery = createMock(Query.class);
        expect(realQuery.getResultList()).andReturn(retornList).once();

        Query countQuery = createMock(Query.class);

        EntityManager em = createMock(EntityManager.class);
        expect(em.createQuery(queryString)).andReturn(realQuery).once();
        expect(em.createQuery(countQueryString)).andReturn(countQuery).once();

        replay(realQuery, countQuery, em);

        Paginator paginator = new Paginator(em, pagination);
        PaginatedQuery paginatedQuery = paginator.createQuery(queryString);
        List listReturned = paginatedQuery.getResultList();

        assertEquals(retornList, listReturned);

        verify(realQuery, countQuery, em);
    }
}
