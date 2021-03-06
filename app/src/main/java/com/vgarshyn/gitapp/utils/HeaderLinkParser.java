package com.vgarshyn.gitapp.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * GitHub API uses web linking https://tools.ietf.org/html/rfc5988 in Headers for pagination.
 * This class parse "Link" string in order to extract useful data such as firs, last link, max pages & etc.
 * See more https://developer.github.com/v3/#pagination
 *
 * Created by v.garshyn on 11.02.18.
 */

public class HeaderLinkParser {

    private static final String META_REL = "rel";

    private static final String KEY_FIRST = "first";
    private static final String KEY_LAST = "last";
    private static final String KEY_NEXT = "next";
    private static final String KEY_PREV = "prev";
    public static final String PARAM_PAGE = "page";

    private final HashMap<String, String> linksMap = new HashMap<>(4);

    /**
     * Constructor try to sort out passed "Link" string
     * @param linkHeader
     */
    public HeaderLinkParser(String linkHeader) {
        if (linkHeader != null) {
            String[] links = linkHeader.split(",");
            for (String link : links) {
                String[] segments = link.split(";");
                if (segments.length < 2) {
                    continue;
                }

                String linkPart = segments[0].trim();
                if (!linkPart.startsWith("<") || !linkPart.endsWith(">")) {
                    continue;
                }
                linkPart = linkPart.substring(1, linkPart.length() - 1);

                for (int i = 1; i < segments.length; i++) {
                    String[] rel = segments[i].trim().split("=");
                    if (rel.length < 2 || !META_REL.equals(rel[0])) {
                        continue;
                    }

                    String relValue = rel[1];
                    if (relValue.startsWith("\"") && relValue.endsWith("\"")) {
                        relValue = relValue.substring(1, relValue.length() - 1);
                    }

                    linksMap.put(relValue, linkPart);
                }
            }
        }
    }

    /**
     * Get link to first page
     * @return
     */
    public String getFirstLink() {
        return linksMap.get(KEY_FIRST);
    }

    /**
     * Get link to last page
     * @return
     */
    public String getLastLink() {
        return linksMap.get(KEY_LAST);
    }

    /**
     * Get link to next page
     * @return
     */
    public String getNextLink() {
        return linksMap.get(KEY_NEXT);
    }

    /**
     * Get link to previous page
     * @return
     */
    public String getPrevLink() {
        return linksMap.get(KEY_PREV);
    }

    /**
     * Get max available pages from available links.
     * Try to find link to last page and extract param {@link HeaderLinkParser#PARAM_PAGE}
     * @return
     */
    public int getMaxPagesCount() {
        String url;
        Map<String, String> params;
        String currentKey;
        if (linksMap.containsKey(KEY_LAST)) {
            currentKey = KEY_LAST;
        } else if (linksMap.containsKey(KEY_PREV) && !linksMap.containsKey(KEY_NEXT)) {
            currentKey = KEY_PREV;
        } else {
            return 0;
        }

        url = linksMap.get(currentKey);
        params = getQueryParams(url);
        String pageParam = params.get(PARAM_PAGE);
        if (pageParam != null) {
            try {
                int page = Integer.parseInt(pageParam);
                return KEY_LAST.equals(currentKey) ? page : page+1;
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        return 0;
    }

    /**
     * Parse Url to extract GET params
     *
     * @param query
     * @return
     */
    public static Map<String, String> getQueryParams(String query) {
        Map<String, String> map = new HashMap<>();
        String[] urlParts = query.split("\\?");
        if (urlParts != null && urlParts.length == 2) {
            String[] params = urlParts[1].split("&");
            for (String param:params) {
                String name = param.split("=")[0];
                String value = param.split("=")[1];
                map.put(name, value);
            }
        }
        return map;
    }
}

