package br.com.dextra.components.querypaginator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import br.com.dextra.components.querypaginator.Pagination;

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
        String queryString5 = "SELECT t.from from TesteBean"
            + " t where 0=0 and t.a = :a order by t.a";

        String queryError = "SELECT t.from TesteBean"
                + " t where 0=0 and t.a = :a";

        String expected = "SELECT count(*) from TesteBean"
                + " t where 0=0 and t.a = :a";


        assertEquals(expected, PaginatorUtils.mountCountQuery(queryString1));

        assertEquals(expected, PaginatorUtils.mountCountQuery(queryString2));

        assertEquals(expected, PaginatorUtils.mountCountQuery(queryString3));

        assertEquals(expected, PaginatorUtils.mountCountQuery(queryString4));

        assertEquals(expected, PaginatorUtils.mountCountQuery(queryString5));

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

        String expected1 = "SELECT count(DISTINCT t.bla) from TesteBean"
                + " t where 0=0 and t.a = :a";
        assertEquals(expected1, PaginatorUtils.mountCountQuery(queryString1));

        String queryString2 = "SELECT DISTINCT new br.com.TesteWrapper(d.id,d.data,a.paciente.nome) FROM TesteBean a"
        	   + " t where 0=0 and t.a = :a";

	    String expected2 = "SELECT count(DISTINCT new br.com.TesteWrapper(d.id,d.data,a.paciente.nome)) FROM TesteBean a"
     	   + " t where 0=0 and t.a = :a";
	    assertEquals(expected2, PaginatorUtils.mountCountQuery(queryString2));

    }

    @Test
    public void testMountCountQuerySubSelect() {
    	String queryString1 = "SELECT t from TesteBean"
            + " t where 0=0 and t.a = :a and (SELECT a from TesteBean2 a where a.c = :c)";

    	String expected = "SELECT count(*) from TesteBean"
            + " t where 0=0 and t.a = :a and (SELECT a from TesteBean2 a where a.c = :c)";

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
