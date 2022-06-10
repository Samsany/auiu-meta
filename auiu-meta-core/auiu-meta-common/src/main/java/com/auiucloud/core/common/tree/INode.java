package com.auiucloud.core.common.tree;

import java.io.Serializable;
import java.util.List;

/**
 * 节点接口
 *
 * @author dries
 * @createDate 2022-06-08 16-15
 */
public interface INode extends Serializable {

    Long getId();

    Long getParentId();

    List<INode> getChildren();

    default Boolean getHasChildren() {
        return false;
    }

    ;

}
