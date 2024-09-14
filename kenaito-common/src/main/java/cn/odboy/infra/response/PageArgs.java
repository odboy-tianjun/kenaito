package cn.odboy.infra.response;

import lombok.*;

/**
 * 分页请求参数
 *
 * @author odboy
 * @date 2024-09-13
 */
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class PageArgs<T> {
    private long page = 1;
    private long pageSize = 10;
    private T body;
}
