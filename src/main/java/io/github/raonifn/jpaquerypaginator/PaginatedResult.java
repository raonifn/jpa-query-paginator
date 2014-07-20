package io.github.raonifn.jpaquerypaginator;

import java.io.Serializable;
import java.util.List;

public class PaginatedResult<T> implements Serializable {

    private static final long serialVersionUID = -6100543252915548367L;

    private Pagination pagination;

    private List<T> result;

    private long pageCount, resultCount;

    public PaginatedResult() {
    }

    public PaginatedResult(Pagination pagination, List<T> result, long pageCount, long resultCount) {
        this.pagination = pagination;
        this.result = result;
        this.pageCount = pageCount;
        this.resultCount = resultCount;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public List<T> getResult() {
        return result;
    }

    public long getPageCount() {
        return pageCount;
    }
    
    public long getResultCount() {
        return resultCount;
    }

    public boolean isEmpty() {
        return result.isEmpty();
    }
}
