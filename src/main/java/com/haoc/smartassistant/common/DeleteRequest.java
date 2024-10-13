package com.haoc.smartassistant.common;

import java.io.Serializable;
import lombok.Data;

/**
 * Delete Request
 *
 *
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}