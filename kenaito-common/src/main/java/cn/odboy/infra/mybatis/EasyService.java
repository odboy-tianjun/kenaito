package cn.odboy.infra.mybatis;

import cn.odboy.infra.response.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface EasyService<T> extends IService<T> {
    LambdaQueryWrapper<T> oh();

    <G> int quickSave(G resources);

    <G> int quickSaveBatch(List<G> resources);

    <G> int quickModifyById(G resources);

    <G> boolean quickModifyBatchById(Collection<G> resources, int batchSize);

    <G> G quickGetOneById(Serializable id, Class<G> targetClazz);

    T quickGetOneWho(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> orderColumn);

    <G> G quickGetOneWho(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> orderColumn, Class<G> clazz);

    T quickGetOne(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> orderColumn);

    <G> G quickGetOne(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> orderColumn, Class<G> clazz);

    <G> List<G> quickListByIds(List<Serializable> ids, Class<G> targetClazz);

    <G> List<G> quickListWho(LambdaQueryWrapper<T> wrapper, Class<G> targetClazz);

    <Q> List<T> quickList(Q criteria);

    List<T> quickList(LambdaQueryWrapper<T> wrapper);

    <G, Q> List<G> quickList(Q criteria, Class<G> targetClazz);

    <G> List<G> quickList(LambdaQueryWrapper<T> wrapper, Class<G> targetClazz);

    List<T> quickListWho(LambdaQueryWrapper<T> wrapper);

    long quickCountWho(LambdaQueryWrapper<T> wrapper);

    PageResult<T> quickPage(LambdaQueryWrapper<T> wrapper, IPage<T> pageable);

    <G> PageResult<G> quickPage(LambdaQueryWrapper<T> wrapper, IPage<T> pageable, Class<G> targetClazz);

    <G, Q> PageResult<G> quickPage(Q criteria, IPage<T> pageable, Class<G> targetClazz);

    <Q> PageResult<T> quickPage(Q criteria, IPage<T> pageable);

    int quickUpdate(LambdaQueryWrapper<T> wrapper, T entity);

    int quickUpdateWho(LambdaQueryWrapper<T> wrapper, T entity);

    int quickDelete(LambdaQueryWrapper<T> wrapper);

    int quickDeleteWho(LambdaQueryWrapper<T> wrapper);

    <G> PageResult<G> quickPageWho(LambdaQueryWrapper<T> wrapper, IPage<T> pageable, Class<G> targetClazz);

    <G, Q> PageResult<G> quickPageWho(Q criteria, IPage<T> pageable, Class<G> targetClazz);

    PageResult<T> quickPageWho(LambdaQueryWrapper<T> wrapper, IPage<T> pageable);
}
