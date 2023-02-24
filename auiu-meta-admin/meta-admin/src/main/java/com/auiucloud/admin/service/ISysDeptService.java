package com.auiucloud.admin.service;

import com.auiucloud.admin.domain.SysDept;
import com.auiucloud.admin.vo.SysDeptVO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【sys_dept(部门表)】的数据库操作Service
* @createDate 2022-12-08 14:26:31
*/
public interface ISysDeptService extends IService<SysDept> {

    PageUtils listPage(Search search);

    List<SysDept> selectSysDeptList(Search search);

    List<SysDeptVO> treeList(Search search);

    /**
     * 校验部门名称是否唯一
     *
     * @param sysDept 部门信息
     * @return 结果
     */
    boolean checkDeptNameExist(SysDept sysDept);

    boolean insertDept(SysDept sysDept);

    boolean updateDept(SysDept sysDept);

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    long selectNormalChildrenDeptById(Long deptId);

    List<SysDept> selectChildrenDeptById(Long deptId);

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    boolean removeDeptById(Long deptId);

    /**
     * 是否存在部门子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    boolean hasChildByDeptId(Long deptId);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    boolean checkDeptExistUser(Long deptId);

}
