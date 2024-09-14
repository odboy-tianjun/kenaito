package cn.odboy.util;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.script.ScriptRuntimeException;
import org.springframework.stereotype.Component;

import javax.script.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 脚本工具
 *
 * @date 2023-12-05
 */
@Component
public class ScriptUtil {
    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

    /**
     * @param script 函数表达式
     * @return 返回值
     */
    public Object evalObj(String script, Object compareObj) {
        try {
            if (script.contains("function")) {
                throw new RuntimeException("script格式异常");
            }
            // 构建一个不同的脚本上下文
            ScriptContext newContext = new SimpleScriptContext();
            if (compareObj != null) {
                Field[] fields = ReflectUtil.getFields(compareObj.getClass());

                Map<String, Object> filedNameValueMap = new ConcurrentHashMap<>();
                Arrays.stream(fields).parallel().forEach(f -> {
                    filedNameValueMap.put(f.getName(), ReflectUtil.getFieldValue(compareObj, f));
                });

                Set<Map.Entry<String, Object>> entries = filedNameValueMap.entrySet();
                long count = entries.parallelStream().filter(f -> f.getValue() != null).count();
                if (count == 0) {
                    throw new RuntimeException("比较对象中没有有效值");
                }
                Bindings engineScope = engine.createBindings();
                entries.parallelStream().forEach(f -> {
                    // 在新的执行引擎作用域中添加与上面相同名称的变量
                    engineScope.put(f.getKey(), f.getValue());
                });
                newContext.setBindings(engineScope, ScriptContext.ENGINE_SCOPE);
            }
            if (script.contains("function")) {
                // 由ScriptEngines实现的接口, 其方法允许调用以前已执行的脚本中的过程
                Invocable inv = (Invocable) engine;
                // 调用全局函数, 并传入参数
                inv.invokeFunction("hello", "测试");
            }
            // 执行脚本, 注意这里指定新的执行作用域上下文
            return engine.eval(script, newContext);
        } catch (ScriptException e) {
            throw new ScriptRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new ScriptRuntimeException(e);
        }
    }

    /**
     * @param script       函数表达式, 例如: function hello(name) { print('Hello, ' + name); }
     * @param functionName 函数名称, 例如: "hello"
     * @param args         函数参数, 例如: "小明"
     * @return 返回值
     */
    public Object evalFunction(String script, String functionName, Object... args) {
        if (!script.contains("function")) {
            throw new RuntimeException("script格式异常");
        }
        try {
            // 执行脚本
            engine.eval(script);
            // 由ScriptEngines实现的接口，其方法允许调用以前已执行的脚本中的过程。
            Invocable inv = (Invocable) engine;
            // 调用全局函数，并传入参数
            return inv.invokeFunction(functionName, args);
        } catch (ScriptException e) {
            throw new ScriptRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new ScriptRuntimeException(e);
        }
    }
}
