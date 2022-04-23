package com.epam.esm.util;

/**
 * The class represents page requested by user
 */
public final class RequestedPage {

    private static final Integer DEF_PAGE = 1;
    private static final Integer DEF_LIMIT = 10;

    private final Integer page;
    private final Integer limit;
    private final Integer offset;

    /**
     * Instantiates a new Request page.
     * Calculates limit and offset values to make sql data selection
     *
     * @param page the page parameter.
     * @param limit the data per page limit parameter.
     */
    public RequestedPage(Integer page, Integer limit) {
        this.page = page != null ? page : DEF_PAGE;
        this.limit = limit != null ? limit : DEF_LIMIT;
        this.offset = (this.page - 1) * this.limit;
    }

    /**
     * Gets the page.
     *
     * @return the page
     */
    public int getPage() {
        return page;
    }

    /**
     * Gets the limit.
     *
     * @return the limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Gets the offset.
     *
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }
}
