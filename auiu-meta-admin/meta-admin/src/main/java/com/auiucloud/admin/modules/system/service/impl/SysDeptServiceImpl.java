package com.auiucloud.admin.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.auiucloud.admin.modules.system.domain.SysDept;
import com.auiucloud.admin.modules.system.mapper.SysDeptMapper;
import com.auiucloud.admin.modules.system.service.ISysDeptService;
import com.auiucloud.admin.modules.system.vo.SysDeptTreeVO;
import com.auiucloud.admin.modules.system.vo.SysDeptVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.tree.ForestNodeMerger;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author dries
 * @description 针对表【sys_dept(部门表)】的数据库操作Service实现
 * @createDate 2022-12-08 14:26:31
 */
@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept>
        implements ISysDeptService {

    @NotNull
    private static LambdaQueryWrapper<SysDept> buildSearchParams(Search search) {
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtil.isNotBlank(search.getStartDate())) {
            queryWrapper.between(SysDept::getCreateTime, search.getStartDate(), search.getEndDate());
        }
        if (StringUtil.isNotBlank(search.getKeyword())) {
            queryWrapper.like(SysDept::getDeptName, search.getKeyword());
        }
        if (ObjectUtil.isNotNull(search.getStatus())) {
            queryWrapper.eq(SysDept::getStatus, search.getStatus());
        }
        queryWrapper.orderByAsc(SysDept::getSort);
        queryWrapper.orderByDesc(SysDept::getCreateTime);
        queryWrapper.orderByDesc(SysDept::getId);
        return queryWrapper;
    }

    @Override
    public PageUtils listPage(Search search) {
        LambdaQueryWrapper<SysDept> queryWrapper = buildSearchParams(search);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public List<SysDept> selectSysDeptList(Search search) {
        LambdaQueryWrapper<SysDept> queryWrapper = buildSearchParams(search);
        return Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList());
    }

    @Override
    public List<SysDeptTreeVO> treeList(Search search) {
        List<SysDept> list = this.selectSysDeptList(search);
        return ForestNodeMerger.merge(
                list.stream().map(dept -> {
                    SysDeptTreeVO sysDeptVO = new SysDeptTreeVO();
                    BeanUtils.copyProperties(dept, sysDeptVO);
                    return sysDeptVO;
                }).collect(Collectors.toList()));
    }

    /**
     * 根据部门ID查询部门详情
     *
     * @param id 部门ID
     * @return 结果
     */
    @Override
    public SysDeptVO getSysDeptVOInfoById(Long id) {
        SysDept sysDept = this.getById(id);
        if (ObjectUtil.isNotNull(sysDept)) {
            SysDeptVO sysDeptVO = new SysDeptVO();
            BeanUtils.copyProperties(sysDept, sysDeptVO);
            return sysDeptVO;
        }
        return null;
    }

    @Override
    public boolean insertDept(SysDept sysDept) {

        // 部门节点
        if (Objects.equals(sysDept.getParentId(), CommonConstant.ROOT_NODE_ID)) {
            sysDept.setAncestors("0");
        } else {
            SysDept info = this.getById(sysDept.getParentId());
            if (info.getStatus().equals(CommonConstant.STATUS_DISABLE_VALUE)) {
                throw new ApiException("部门停用，不允许新增");
            }
            sysDept.setAncestors(info.getAncestors() + "," + sysDept.getParentId());
        }

        return this.save(sysDept);
    }

    @Transactional
    @Override
    public boolean updateDept(SysDept sysDept) {
        SysDept newParentDept = this.getById(sysDept.getParentId());
        SysDept oldDept = this.getById(sysDept.getParentId());

        if (ObjectUtil.isNotNull(newParentDept) && ObjectUtil.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getId();
            String oldAncestors = oldDept.getAncestors();

            sysDept.setAncestors(newAncestors);
            this.updateDeptChildren(sysDept.getId(), newAncestors, oldAncestors);
        }

        boolean result = this.updateById(sysDept);
        if (result && sysDept.getStatus().equals(CommonConstant.STATUS_NORMAL_VALUE)
                && StrUtil.isNotBlank(sysDept.getAncestors())
                && !sysDept.getAncestors().equals("0")
        ) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            this.updateParentDeptStatusNormal(sysDept);
        }

        return result;
    }

    private void updateParentDeptStatusNormal(SysDept sysDept) {
        String ancestors = sysDept.getAncestors();
        if (StringUtil.isNotBlank(ancestors)) {
            LambdaUpdateWrapper<SysDept> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(SysDept::getStatus, CommonConstant.STATUS_NORMAL_VALUE);
            updateWrapper.inSql(SysDept::getId, ancestors);
            this.update(updateWrapper);
        }
    }

    private void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<SysDept> childrenDeptList = this.selectChildrenDeptById(deptId);
        if (CollUtil.isNotEmpty(childrenDeptList)) {
            for (SysDept child : childrenDeptList) {
                child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
            }

            this.updateBatchById(childrenDeptList);
        }
    }

    @Override
    public List<SysDept> selectChildrenDeptById(Long deptId) {
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.apply("find_in_set({0}, ancestors)", deptId);
        return Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList());
    }

    @Override
    public long selectNormalChildrenDeptById(Long id) {
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDept::getParentId, id);
        queryWrapper.eq(SysDept::getStatus, CommonConstant.STATUS_NORMAL_VALUE);
        queryWrapper.apply("find_in_set({0}, ancestors)", id);
        return this.count(queryWrapper);
    }

    @Override
    public boolean checkDeptNameExist(SysDept sysDept) {

        Long parentId = sysDept.getParentId();
        String deptName = sysDept.getDeptName();
        Long deptId = ObjectUtil.isNull(sysDept.getId()) ? -1L : sysDept.getId();

        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDept::getDeptName, deptName);
        queryWrapper.eq(SysDept::getParentId, parentId);
        queryWrapper.last("limit 1");
        SysDept dept = this.getOne(queryWrapper);

        return ObjectUtil.isNotNull(dept) && !Objects.equals(dept.getId(), deptId);
    }

    @Override
    public boolean removeDeptById(Long deptId) {
        return this.removeById(deptId);
    }

    @Override
    public boolean hasChildByDeptId(Long deptId) {
        List<SysDept> deptList = this.selectChildrenDeptById(deptId);
        return CollUtil.isNotEmpty(deptList);
    }
}




