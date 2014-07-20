package io.github.raonifn.jpaquerypaginator;

import javax.persistence.Query;

public interface PaginatedQuery<T> extends Query {

    public PaginatedResult<T> getPaginatedResult();
}
