package io.github.raonifn.jpaquerypaginator;

import java.io.Serializable;

public class Pagination implements Serializable {

    private static final long serialVersionUID = 5810822233381560037L;

    private int pageSize;

    private int page;
    
    private boolean enabled;

    public Pagination() {
    	this.enabled = true;
    }

    public Pagination(boolean enabled) {
    	this.enabled = enabled;
    }

    public Pagination(int pageSize, int page) {
        this.pageSize = pageSize;
        this.page = page;
        this.enabled = true;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

	public boolean isEnabled() {
		return enabled;
	}

}
