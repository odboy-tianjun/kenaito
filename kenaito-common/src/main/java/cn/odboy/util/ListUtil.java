package cn.odboy.util;

import cn.odboy.base.MyEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListUtil {
    /**
     * 根据对象T的属性去重
     *
     * @param items      对象T集合
     * @param classifier 对象T属性表达式, 比如: User::getName
     */
    public static <T, K> List<T> distinctByTArgs(List<T> items, Function<? super T, ? extends K> classifier) {
        return items.stream()
                .collect(Collectors.groupingBy(classifier))
                .values().stream()
                .map(monitorItem -> monitorItem.iterator().next())
                .collect(Collectors.toList());
    }

    // list转map，保留新值 -> Map<String, User> userMap = items.stream().collect(Collectors.toMap(User::getName, user -> user))
    // list转map，保留旧值 -> Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getName, user -> user, (existing, replacement) -> existing));

    public static void main(String[] args) {
        List<MyEntity> entities = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            MyEntity entity = new MyEntity();
            if (i % 2 == 0) {
                entity.setCreateBy("odboy");
            } else {
                entity.setCreateBy("admin");
            }
            entity.setUpdateBy("admin");
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            entities.add(entity);
        }
        List<MyEntity> entities1 = ListUtil.distinctByTArgs(entities, MyEntity::getCreateBy);
        System.err.println(entities1);
    }
}
