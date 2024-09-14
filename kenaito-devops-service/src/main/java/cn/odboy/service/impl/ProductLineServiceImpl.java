package cn.odboy.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.odboy.constant.ProductLineUserRoleEnum;
import cn.odboy.domain.ProductLine;
import cn.odboy.domain.ProductLineUser;
import cn.odboy.infra.response.PageArgs;
import cn.odboy.mapper.ProductLineMapper;
import cn.odboy.service.ProductLineService;
import cn.odboy.service.ProductLineUserService;
import cn.odboy.util.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 产品线 服务实现类
 * </p>
 *
 * @author odboy
 * @since 2024-09-13
 */
@Service
@AllArgsConstructor
public class ProductLineServiceImpl extends ServiceImpl<ProductLineMapper, ProductLine> implements ProductLineService {
    private ProductLineUserService productLineUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ProductLine.CreateArgs args) {
        ProductLine record = new ProductLine();
        record.setName(args.getName());
        record.setDescription(args.getDescription());
        record.setPid(args.getPid());
        this.save(record);

        List<ProductLineUser> addList = new ArrayList<>();
        List<Long> adminList = args.getAdminList();
        List<Long> peList = args.getPeList();
        if (CollUtil.isNotEmpty(adminList)) {
            for (Long userId : adminList) {
                ProductLineUser base = new ProductLineUser();
                base.setProductLineId(record.getId());
                base.setUserId(userId);
                base.setRoleCode(ProductLineUserRoleEnum.ADMIN.getCode());
                addList.add(base);
            }
        }
        if (CollUtil.isNotEmpty(peList)) {
            for (Long userId : peList) {
                ProductLineUser base = new ProductLineUser();
                base.setProductLineId(record.getId());
                base.setUserId(userId);
                base.setRoleCode(ProductLineUserRoleEnum.PE.getCode());
                addList.add(base);
            }
        }
        if (CollUtil.isNotEmpty(addList)) {
            this.productLineUserService.saveBatch(addList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(ProductLine.ModifyArgs args) {
        ProductLine record = new ProductLine();
        record.setId(args.getId());
        record.setName(args.getName());
        record.setDescription(args.getDescription());
        record.setPid(args.getPid());
        this.updateById(record);
        this.productLineUserService.removeByProductLineId(record.getId());
        List<ProductLineUser> addList = new ArrayList<>();
        List<Long> adminList = args.getAdminList();
        List<Long> peList = args.getPeList();
        if (CollUtil.isNotEmpty(adminList)) {
            for (Long userId : adminList) {
                ProductLineUser base = new ProductLineUser();
                base.setProductLineId(record.getId());
                base.setUserId(userId);
                base.setRoleCode(ProductLineUserRoleEnum.ADMIN.getCode());
                addList.add(base);
            }
        }
        if (CollUtil.isNotEmpty(peList)) {
            for (Long userId : peList) {
                ProductLineUser base = new ProductLineUser();
                base.setProductLineId(record.getId());
                base.setUserId(userId);
                base.setRoleCode(ProductLineUserRoleEnum.PE.getCode());
                addList.add(base);
            }
        }
        if (CollUtil.isNotEmpty(addList)) {
            this.productLineUserService.saveBatch(addList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(ProductLine.RemoveByIdsArgs args) {
        this.removeBatchByIds(args.getIds());
        this.productLineUserService.removeBatchByProductLineIds(args.getIds());
    }

    @Override
    public List<ProductLine.TreeNode> getTree() {
        return this.convertToTree(this.list());
    }

    @Override
    public IPage<ProductLine> queryPage(PageArgs<ProductLine> args) {
        ProductLine body = args.getBody();
        LambdaQueryWrapper<ProductLine> wrapper = new LambdaQueryWrapper<>();
        if (body != null) {
            wrapper.like(StrUtil.isNotBlank(body.getName()), ProductLine::getName, body.getName());
            wrapper.eq(body.getPid() != null, ProductLine::getPid, body.getPid());
            if(body.getPid() == null){
                wrapper.isNull(ProductLine::getPid);
            }
        }
        return this.page(new Page<>(args.getPage(), args.getPageSize()), wrapper);
    }

    private List<ProductLine.TreeNode> convertToTree(List<ProductLine> list) {
        List<ProductLine.TreeNode> nodes = list.stream().map(item -> {
            ProductLine.TreeNode treeNode = new ProductLine.TreeNode();
            treeNode.setId(item.getId());
            treeNode.setPid(item.getPid());
            treeNode.setName(item.getName());
            treeNode.setChildren(new ArrayList<>());
            return treeNode;
        }).collect(Collectors.toList());

        Map<Long, ProductLine.TreeNode> idMap = new HashMap<>(10);
        Set<Long> ids = new HashSet<>();

        for (ProductLine.TreeNode item : nodes) {
            idMap.put(item.getId(), item);
        }

        for (ProductLine.TreeNode item : nodes) {
            Long parentId = item.getPid();
            if (parentId == null) {
                // 根节点
                continue;
            }
            ProductLine.TreeNode parent = idMap.get(parentId);
            if (parent != null) {
                parent.getChildren().add(item);
                ids.add(item.getId());
            }
        }

        // 找到根节点
        List<ProductLine.TreeNode> rootNodes = new ArrayList<>();
        for (ProductLine.TreeNode item : nodes) {
            if (!ids.contains(item.getId())) {
                rootNodes.add(item);
            }
        }
        return rootNodes;
    }
}
