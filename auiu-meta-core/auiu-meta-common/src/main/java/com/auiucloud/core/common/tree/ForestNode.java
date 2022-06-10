package com.auiucloud.core.common.tree;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 森林节点
 *
 * @author dries
 * @createDate 2022-06-08 16-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ForestNode extends TreeNode {

    private static final long serialVersionUID = -343336577890841964L;

    /**
     * 节点内容
     */
    private Object content;

    public ForestNode(Long id, Long parentId, Object content) {
        this.id = id;
        this.parentId = parentId;
        this.content = content;
    }

}
