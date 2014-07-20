package br.com.dextra.components.querypaginator;

import javax.persistence.Query;

public interface PaginatedQuery<T> extends Query {

    public PaginatedResult<T> getPaginatedResult();
}
