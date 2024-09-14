package cn.odboy.base.model;

import cn.odboy.base.MyObject;
import lombok.*;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SelectOption extends MyObject {
    private String label;
    private String value;
    private Map<String, Object> ext;
}
