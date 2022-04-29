package com.epam.esm.util;

/**
 * The class represents page requested by user
 */
public final class RequestedPage {

    private static final Long DEFAULT_PAGE = 1L;
    private static final Long DEFAULT_LIMIT = 10L;

    private final Long page;
    private final Long limit;
    private final Long offset;

    /**
     * Instantiates a new Request page.
     * Calculates limit and offset values to make database data selection
     *
     * @param page the page parameter.
     * @param limit the data per page limit parameter.
     */
    public RequestedPage(Long page, Long limit) {
        this.page = page != null && page > 0L ? page : DEFAULT_PAGE;
        this.limit = limit != null ? limit : DEFAULT_LIMIT;
        this.offset = (this.page - 1) * this.limit;
    }

    public Long getPage() {
        return page;
    }

    public Long getLimit() {
        return limit;
    }

    public Long getOffset() {
        return offset;
    }
}
