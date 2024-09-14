package cn.odboy.infra.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class PageResult<T> {

    private List<T> content;

    private long totalElements;
}
