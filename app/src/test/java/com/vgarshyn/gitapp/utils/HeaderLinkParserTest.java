package com.vgarshyn.gitapp.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by v.garshyn on 11.02.18.
 */
public class HeaderLinkParserTest {

    private static final String BASE_URL = "https://api.github.com/repositories/8575137/contributors";
    private static final String FIRST = BASE_URL +"?page=1&per_page=30";
    private static final String LAST = BASE_URL +"?page=4&per_page=30";
    private static final String NEXT = BASE_URL +"?page=3&per_page=30";
    private static final String PREV = BASE_URL +"?page=3&per_page=30";

    private static final String LINKS_FIRST = " <"+NEXT+">; rel=\"next\", <"+LAST+">; rel=\"last\"";
    private static final String LINKS_LAST = "<"+PREV+">; rel=\"prev\", <"+FIRST+">; rel=\"first\"";
    private static final String LINKS_FULL = "<"+PREV+">; rel=\"prev\", <"+NEXT+">; rel=\"next\", <"+LAST+">; rel=\"last\", <"+FIRST+">; rel=\"first\"";

    private HeaderLinkParser parserFirst;
    private HeaderLinkParser parserLast;
    private HeaderLinkParser parserFull;

    @Before
    public void setUp() throws Exception {
        parserFirst = new HeaderLinkParser(LINKS_FIRST);
        parserLast = new HeaderLinkParser(LINKS_LAST);
        parserFull = new HeaderLinkParser(LINKS_FULL);
    }

    @Test
    public void getFirstLink() throws Exception {
        assertNull(parserFirst.getFirstLink());
        assertNotNull(parserLast.getFirstLink());
        assertNotNull(parserFull.getFirstLink());

        assertEquals(FIRST, parserLast.getFirstLink());
        assertEquals(FIRST, parserFull.getFirstLink());
    }

    @Test
    public void getLastLink() throws Exception {
        assertNotNull(parserFirst.getLastLink());
        assertNull(parserLast.getLastLink());
        assertNotNull(parserFull.getLastLink());

        assertEquals(LAST, parserFirst.getLastLink());
        assertEquals(LAST, parserFull.getLastLink());
    }

    @Test
    public void getNextLink() throws Exception {
        assertNotNull(parserFirst.getNextLink());
        assertNull(parserLast.getNextLink());
        assertNotNull(parserFull.getNextLink());

        assertEquals(NEXT, parserFull.getNextLink());
    }

    @Test
    public void getPrevLink() throws Exception {
        assertNull(parserFirst.getPrevLink());
        assertNotNull(parserLast.getPrevLink());
        assertNotNull(parserFull.getPrevLink());

        assertEquals(PREV, parserLast.getPrevLink());
        assertEquals(PREV, parserFull.getPrevLink());
    }

    @Test
    public void getMaxPagesCount() throws Exception {
        assertEquals(4, parserFirst.getMaxPagesCount());
        assertEquals(4, parserLast.getMaxPagesCount());
        assertEquals(4, parserFull.getMaxPagesCount());
    }

    @Test
    public void getQueryParams() throws Exception {
        Map<String, String> params = HeaderLinkParser.getQueryParams(FIRST);
        assertNotNull(params);
        assertThat(params.isEmpty(), is(false));
        assertThat(params.get("page"), is("1"));
        assertThat(params.get("per_page"), is("30"));
    }

}