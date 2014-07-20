package io.github.raonifn.jpaquerypaginator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class PaginatorUtils {

    public static String mountCountQuery(String queryString) {
        String fromQuery = removeOrderBy(queryString).trim();


        //String patternString = "(^\\s*select distinct ([\\w.\\d*]+))?(^|.*\\s)(from\\s+.*)";
        String patternString = "(^select\\s+(distinct\\s+(new){0,1}([\\w.\\d*]+)){0,1}(.*?)(\\s+|^)){0,1}(from\\s+.*)";

        Pattern pattern = Pattern.compile(
        		patternString,
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fromQuery);
        if (!matcher.matches()) {
            throw new RuntimeException("Invalid query: " + queryString);
        }

        fromQuery = matcher.group(7);

        String distinct = matcher.group(4);
        if("new".equals(distinct)){
        	distinct+=matcher.group(5);
        }
        if (distinct != null) {
            return "SELECT count(DISTINCT " + distinct + ") " + fromQuery;
        }

        return "SELECT count(*) " + fromQuery;
    }

    private static String removeOrderBy(String queryString) {
        Pattern pattern = Pattern.compile("(.*)\\s+order\\s+by\\s+.*",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(queryString);
        if (matcher.matches()) {
            return matcher.group(1);
        }

        return queryString;
    }

    public static int getOffset(Pagination pagination) {
        int pageSize = pagination.getPageSize();
        int page = pagination.getPage();
        int offset = (pageSize * (page - 1));
        return offset;
    }

    public static long getPageCount(long total, Pagination pagination) {
        int pageSize = pagination.getPageSize();
        double ret = Math.ceil((double) total / pageSize);
        return Double.valueOf(ret).longValue();
    }

}
