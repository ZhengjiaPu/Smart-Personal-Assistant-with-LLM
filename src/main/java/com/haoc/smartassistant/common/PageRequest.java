package com.haoc.smartassistant.common;

import com.haoc.smartassistant.constant.CommonConstant;
import lombok.Data;

/**
 * Pagination request
 *
 */
@Data
public class PageRequest {

    /**
     * Current page number
     */
    private int current = 1;

    /**
     * Page size
     */
    private int pageSize = 10;

    /**
     * Sorting field
     */
    private String sortField;

    /**
     * Sorting order (default is ascending)
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
