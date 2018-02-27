package com.vgarshyn.gitapp.utils

import java.util.HashMap

/**
 * GitHub API uses web linking https://tools.ietf.org/html/rfc5988 in Headers for pagination.
 * This class parse "Link" string in order to extract useful data such as firs, last link, max pages & etc.
 * See more https://developer.github.com/v3/#pagination
 *
 * Created by v.garshyn on 11.02.18.
 */

class HeaderLinkParser(linkHeader: String?) {

    private val linksMap = HashMap<String, String>(4)

    /**
     * Get link to first page
     * @return
     */
    val firstLink: String?
        get() = linksMap[KEY_FIRST]

    /**
     * Get link to last page
     * @return
     */
    val lastLink: String?
        get() = linksMap[KEY_LAST]

    /**
     * Get link to next page
     * @return
     */
    val nextLink: String?
        get() = linksMap[KEY_NEXT]

    /**
     * Get link to previous page
     * @return
     */
    val prevLink: String?
        get() = linksMap[KEY_PREV]

    /**
     * Get max available pages from available links.
     * Try to find link to last page and extract param [HeaderLinkParser.PARAM_PAGE]
     * @return
     */
    val maxPagesCount: Int
        get() {
            var url: String?
            val params: Map<String, String>
            val currentKey: String
            if (linksMap.containsKey(KEY_LAST)) {
                currentKey = KEY_LAST
            } else if (linksMap.containsKey(KEY_PREV) && !linksMap.containsKey(KEY_NEXT)) {
                currentKey = KEY_PREV
            } else {
                return 0
            }

            url = linksMap[currentKey]
            params = getQueryParams(url)
            val pageParam = params[PARAM_PAGE]
            if (pageParam != null) {
                try {
                    val page = Integer.parseInt(pageParam)
                    return if (KEY_LAST == currentKey) page else page + 1
                } catch (e: NumberFormatException) {
                    return 0
                }

            }

            return 0
        }

    init {
        linkHeader?.let {
            val links = linkHeader.split(",".toRegex())
            for (link in links) {
                val segments = link.split(";".toRegex())
                if (segments.size < 2) {
                    continue
                }

                var linkPart = segments[0].trim { it <= ' ' }
                if (!linkPart.startsWith("<") || !linkPart.endsWith(">")) {
                    continue
                }
                linkPart = linkPart.substring(1, linkPart.length - 1)

                for (i in 1 until segments.size) {
                    val rel = segments[i].trim { it <= ' ' }.split("=".toRegex())
                    if (rel.size < 2 || META_REL != rel[0]) {
                        continue
                    }

                    var relValue = rel[1]
                    if (relValue.startsWith("\"") && relValue.endsWith("\"")) {
                        relValue = relValue.substring(1, relValue.length - 1)
                    }

                    linksMap[relValue] = linkPart
                }
            }
        }
    }

    companion object Util {

        private val META_REL = "rel"
        private val KEY_FIRST = "first"
        private val KEY_LAST = "last"
        private val KEY_NEXT = "next"
        private val KEY_PREV = "prev"

        val PARAM_PAGE = "page"

        /**
         * Parse Url to extract GET params
         *
         * @param query
         * @return
         */
        fun getQueryParams(query: String?): Map<String, String> {
            val map = HashMap<String, String>()
            query?.let {
                val urlParts = query.split("\\?".toRegex())
                if (urlParts.size == 2) {
                    val params = urlParts[1].split("&".toRegex())
                    for (param in params) {
                        val name = param.split("=".toRegex())[0]
                        val value = param.split("=".toRegex())[1]
                        map[name] = value
                    }
                }

            }
            return map
        }
    }
}

