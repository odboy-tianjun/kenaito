package cn.odboy.rest;

import cn.odboy.infra.response.PageArgs;
import cn.odboy.infra.response.PageResult;
import cn.odboy.modules.system.domain.User;
import cn.odboy.modules.system.domain.vo.UserQueryCriteria;
import cn.odboy.modules.system.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户数据 前端控制器
 * </p>
 *
 * @author odboy
 * @since 2024-09-13
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "DevOps：用户信息")
@RequestMapping("/api/devops/user")
public class UserDataController {
    private final UserService userService;

    @ApiOperation("分页查询用户列表")
    @PostMapping("/pageUserList")
    public ResponseEntity<Object> pageUserList(@RequestBody PageArgs<UserQueryCriteria> args) {
        PageResult<User> result = userService.queryAll(args.getBody(), new Page<>(args.getPage(), 20));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
