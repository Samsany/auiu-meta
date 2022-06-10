package com.auiucloud.core.common.tree;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 节点
 *
 * @author dries
 * @createDate 2022-06-08 16-15
 */
@Data
public class TreeNode implements INode {

    private static final long serialVersionUID = 7368167252001390544L;

    protected Long id;

    protected Long parentId;

    /**
     * 子孙节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<INode> children = new ArrayList<>();

    /**
     * 是否有子孙节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;

    @Override
    public Boolean getHasChildren() {
        return children.size() > 0 || this.hasChildren;
    }
}
