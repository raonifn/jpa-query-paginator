package io.github.raonifn.jpaquerypaginator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.text.MessageFormat;

import io.github.raonifn.jpaquerypaginator.Pagination;
import io.github.raonifn.jpaquerypaginator.PaginatorUtils;

import org.junit.Test;

public class PaginatorUtilsTest {

    @Test
    public void testMountCountQuery() {
        String queryString1 = "SELECT t from TesteBean"
                + " t where 0=0 and t.a = :a";
        String queryString2 = "from TesteBean t where 0=0 and t.a = :a";

        String queryString3 = "SELECT t.fromData from TesteBean"
                + " t where 0=0 and t.a = :a";
        String queryString4 = "SELECT t.from from TesteBean"
                + " t where 0=0 and t.a = :a";

        String queryError = "SELECT t.from TesteBean"
                + " t where 0=0 and t.a = :a";

        String expected = "SELECT count(*) FROM ({0})";


        assertEquals(MessageFormat.format(expected, queryString1), PaginatorUtils.mountCountQuery(queryString1));
        assertEquals(MessageFormat.format(expected, queryString2), PaginatorUtils.mountCountQuery(queryString2));
        assertEquals(MessageFormat.format(expected, queryString3), PaginatorUtils.mountCountQuery(queryString3));
        assertEquals(MessageFormat.format(expected, queryString4), PaginatorUtils.mountCountQuery(queryString4));

        try {
            PaginatorUtils.mountCountQuery(queryError);
            fail();
        } catch (RuntimeException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testMountCountQueryDistinct() {
        String queryString1 = "SELECT DISTINCT t.bla from TesteBean"
                + " t where 0=0 and t.a = :a";

        String expected1 = "SELECT count(*) FROM ("+queryString1+")";
        assertEquals(expected1, PaginatorUtils.mountCountQuery(queryString1));

        String queryString2 = "SELECT DISTINCT new br.com.TesteWrapper(d.id,d.data,a.paciente.nome) FROM TesteBean a"
        	   + " t where 0=0 and t.a = :a";

	    String expected2 = "SELECT count(*) FROM (" + queryString2+ ")".toUpperCase();
	    assertEquals(expected2, PaginatorUtils.mountCountQuery(queryString2));

    }

    @Test
    public void testMountCountQuerySubSelect() {
    	String queryString1 = "SELECT t from TesteBean"
            + " t where 0=0 and t.a = :a and (SELECT a from TesteBean2 a where a.c = :c)";

    	String expected = "SELECT count(*) FROM (" + queryString1 + ")";

    	assertEquals(expected, PaginatorUtils.mountCountQuery(queryString1));
    }

    @Test
    public void testGetOffset() {
        assertEquals(0, PaginatorUtils.getOffset(new Pagination(20, 1)));
        assertEquals(40, PaginatorUtils.getOffset(new Pagination(20, 3)));
    }

    @Test
    public void testGetPageCount() {
        Pagination pagination = new Pagination(20, 1);
        assertEquals(5l, PaginatorUtils.getPageCount(100, pagination));

        pagination.setPageSize(33);
        assertEquals(4l, PaginatorUtils.getPageCount(100, pagination));

        pagination.setPageSize(100);
        assertEquals(2l, PaginatorUtils.getPageCount(101, pagination));
    }
}
