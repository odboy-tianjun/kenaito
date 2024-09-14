package cn.odboy.infra.mybatis;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.model.SelectOption;
import cn.odboy.infra.context.SecurityUtil;
import cn.odboy.infra.response.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * 公共抽象Mapper接口类
 */
public interface EasyMapper<E> extends BaseMapper<E> {
    /**
     * 批量插入
     *
     * @param entityList List<E>
     * @return 影响的行数
     */
    int insertBatchSomeColumn(List<E> entityList);

    /**
     * 根据id全量更新
     *
     * @param entity E
     * @return 影响的行数
     */
    int alwaysUpdateSomeColumnById(@Param("et") E entity);

    default QueryChainWrapper<E> select() {
        return ChainWrappers.queryChain(this);
    }

    default UpdateChainWrapper<E> update() {
        return ChainWrappers.updateChain(this);
    }

    default LambdaQueryChainWrapper<E> lambdaSelect() {
        return ChainWrappers.lambdaQueryChain(this);
    }

    default LambdaUpdateChainWrapper<E> lambdaUpdate() {
        return ChainWrappers.lambdaUpdateChain(this);
    }

    default LambdaQueryChainWrapper<E> lambdaWhoSelect() {
        String currentUsername;
        try {
            currentUsername = SecurityUtil.getCurrentUsername();
        } catch (Exception e) {
            System.err.println("有可能是定时任务调用了这个方法");
            currentUsername = "怎么可能会有这个用户嘞?";
        }
        LambdaQueryChainWrapper<E> wrapper = ChainWrappers.lambdaQueryChain(this);
        wrapper.apply("create_by = {0}", currentUsername);
        return wrapper;
    }

    /**
     * 获取字典
     *
     * @param wrapper   LambdaQueryWrapper<E>
     * @param keyName   键对应的列名称
     * @param valueName 值对应的列名称
     * @return Map<String, Object>
     */
    default Map<String, Object> selectMap(LambdaQueryWrapper<E> wrapper, String keyName, String valueName) {
        Map<String, Object> result = new ConcurrentHashMap<>(1);
        List<E> list = this.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return new HashMap<>();
        }
        E tempObj = list.stream().findFirst().orElse(null);
        if (tempObj == null) {
            return new HashMap<>();
        }
        Field[] fields = ReflectUtil.getFields(tempObj.getClass());
        for (E e : list) {
            Field keyField = null;
            Field valueField = null;
            for (Field field : fields) {
                String fieldName = field.getName();
                if (fieldName.equals(keyName)) {
                    keyField = field;
                } else if (fieldName.equals(valueName)) {
                    valueField = field;
                    break;
                }
            }
            String fieldKey = String.valueOf(ReflectUtil.getFieldValue(e, keyField));
            Object fieldValue = ReflectUtil.getFieldValue(e, valueField);
            if (fieldKey != null) {
                result.put(fieldKey, fieldValue);
            }
        }
        return result;
    }

    /**
     * 获取下拉选项
     *
     * @param wrapper   LambdaQueryWrapper<E>
     * @param labelName 标题对应的列名称
     * @param valueName 值对应的列名称
     * @return List<MySelectOptionVO>
     */
    default List<SelectOption> selectLabelValue(LambdaQueryWrapper<E> wrapper, String labelName, String valueName) {
        Queue<SelectOption> result = new ConcurrentLinkedQueue<>();
        List<E> list = this.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        E tempObj = list.stream().findFirst().orElse(null);
        if (tempObj != null) {
            Field[] fields = ReflectUtil.getFields(tempObj.getClass());
            for (E e : list) {
                Field labelField = null, valueField = null;
                for (Field field : fields) {
                    String fieldName = field.getName();
                    if (fieldName.equals(labelName)) {
                        labelField = field;
                    } else if (fieldName.equals(valueName)) {
                        valueField = field;
                        break;
                    }
                }
                String fieldLabel = String.valueOf(ReflectUtil.getFieldValue(e, labelField));
                String fieldValue = String.valueOf(ReflectUtil.getFieldValue(e, valueField));
                if (fieldLabel != null) {
                    result.add(SelectOption.builder()
                            .label(fieldLabel)
                            .value(fieldValue)
                            .build()
                    );
                }
            }
        }
        return new ArrayList<>(result);
    }

    /**
     * 获取下拉选项和扩展属性
     *
     * @param wrapper   LambdaQueryWrapper<E>
     * @param labelName 标题对应的列名称
     * @param valueName 值对应的列名称
     * @return List<MySelectOptionVO>
     */
    default List<SelectOption> selectLabelValueWithExt(LambdaQueryWrapper<E> wrapper, String labelName, String valueName, List<String> extPropNames) {
        Queue<SelectOption> result = new ConcurrentLinkedQueue<>();
        List<E> list = this.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        E tempObj = list.stream().findFirst().orElse(null);
        if (tempObj != null) {
            Field[] fields = ReflectUtil.getFields(tempObj.getClass());
            for (E e : list) {
                Field labelField = null, valueField = null;
                for (Field field : fields) {
                    String fieldName = field.getName();
                    if (fieldName.equals(labelName)) {
                        labelField = field;
                    } else if (fieldName.equals(valueName)) {
                        valueField = field;
                        break;
                    }
                }
                String fieldKey = String.valueOf(ReflectUtil.getFieldValue(e, labelField));
                String fieldValue = String.valueOf(ReflectUtil.getFieldValue(e, valueField));
                if (fieldKey != null) {
                    SelectOption optionVO = SelectOption.builder().label(fieldKey).value(fieldValue).build();
                    Map<String, Object> extMap = new ConcurrentHashMap<>(1);
                    List<String> newExtPropNames = extPropNames.stream().filter(StrUtil::isNotEmpty).collect(Collectors.toList());
                    // 初始化fieldMap
                    Map<String, Field> fieldMap = new ConcurrentHashMap<>(1);
                    Arrays.stream(fields).forEach(f -> fieldMap.put(f.getName(), f));
                    // 获取扩展值
                    newExtPropNames.forEach(extPropName -> {
                        Field targetPropField = fieldMap.getOrDefault(extPropName, null);
                        if (targetPropField != null) {
                            String extPropValue = String.valueOf(ReflectUtil.getFieldValue(e, targetPropField));
                            extMap.put(extPropName, extPropValue);
                        }
                    });
                    optionVO.setExt(extMap);
                    result.add(optionVO);
                }
            }
        }
        return new ArrayList<>(result);
    }

    /**
     * 横转纵
     *
     * @param wrapper   LambdaQueryWrapper<E>
     * @param propNames 需要提取的字段
     * @return List<List < Object>>
     */
    default List<List<String>> horizontalToVertical(LambdaQueryWrapper<E> wrapper, List<String> propNames) {
        if (CollUtil.isEmpty(propNames)) {
            return new ArrayList<>();
        }
        Map<String, Queue<String>> tempMap = new ConcurrentHashMap<>(1);
        List<E> list = this.selectList(wrapper);
        propNames.forEach(propName -> {
            Queue<String> innerVerticals = tempMap.getOrDefault(propName, null);
            if (innerVerticals == null) {
                innerVerticals = new ConcurrentLinkedQueue<>();
            }
            for (E e : list) {
                Object fieldValue = ReflectUtil.getFieldValue(e, propName);
                String newFieldValue = fieldValue == null ? "" : String.valueOf(fieldValue);
                innerVerticals.add(newFieldValue);
                tempMap.put(propName, innerVerticals);
            }
        });
        Collection<Queue<String>> values = tempMap.values();
        Queue<List<String>> result = new ConcurrentLinkedQueue<>();
        values.forEach(value -> result.add(new ArrayList<>(value)));
        return new ArrayList<>(result);
    }

    /**
     * 条件查询列表, 并返回期望的对象
     *
     * @param wrapper LambdaQueryWrapper<E>
     * @param clazz   期望的对象类型
     * @return List<T>
     */
    default <T> List<T> selectList(LambdaQueryWrapper<E> wrapper, Class<T> clazz) {
        List<E> list = this.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        return BeanUtil.copyToList(list, clazz);
    }

    /**
     * 条件查询列表, 并返回期望的对象
     *
     * @param wrapper LambdaQueryWrapper<E>
     * @param clazz   期望的对象类型
     * @return List<T>
     */
    default <T> List<T> selectList(LambdaQueryChainWrapper<E> wrapper, Class<T> clazz) {
        List<E> list = wrapper.list();
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        return BeanUtil.copyToList(list, clazz);
    }

    /**
     * 条件查询分页列表, 并返回期望的对象
     *
     * @param pageable 分页参数
     * @param wrapper  LambdaQueryWrapper<E> wrapper
     * @param clazz    期望的对象类型
     * @return IPage<T>
     */
    default <T> PageResult<T> selectPage(Pageable pageable, LambdaQueryWrapper<E> wrapper, Class<T> clazz) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        pageNumber = pageNumber <= 0 ? 1 : pageNumber;
        pageSize = pageSize <= 0 ? 10 : pageSize;
        IPage<E> pageInfo = this.selectPage(new Page<>(pageNumber, pageSize), wrapper);
        if (CollUtil.isEmpty(pageInfo.getRecords())) {
            return new PageResult<>(new ArrayList<>(), pageInfo.getTotal());
        }
        return new PageResult<>(BeanUtil.copyToList(pageInfo.getRecords(), clazz), pageInfo.getTotal());
    }

    /**
     * 条件查询分页列表, 并返回期望的对象
     *
     * @param pageable 分页参数
     * @param wrapper  LambdaQueryWrapper<E> wrapper
     * @param clazz    期望的对象类型
     * @return IPage<T>
     */
    default <T> PageResult<T> selectPage(Pageable pageable, LambdaQueryChainWrapper<E> wrapper, Class<T> clazz) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        pageNumber = pageNumber <= 0 ? 1 : pageNumber;
        pageSize = pageSize <= 0 ? 10 : pageSize;
        IPage<E> pageInfo = wrapper.page(new Page<>(pageNumber, pageSize));
        if (CollUtil.isEmpty(pageInfo.getRecords())) {
            return new PageResult<>(new ArrayList<>(), pageInfo.getTotal());
        }
        return new PageResult<>(BeanUtil.copyToList(pageInfo.getRecords(), clazz), pageInfo.getTotal());
    }

    /**
     * 获取目标类
     *
     * @param wrapper LambdaQueryChainWrapper<E> wrapper
     * @param clazz   期望的对象类型
     * @return T
     */
    default <T> T selectOne(LambdaQueryChainWrapper<E> wrapper, Class<T> clazz) {
        E e = this.selectOne(wrapper);
        if (e == null) {
            return null;
        }
        return BeanUtil.copyProperties(e, clazz);
    }
}

