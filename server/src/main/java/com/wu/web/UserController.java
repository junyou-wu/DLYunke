package com.wu.web;

import com.github.pagehelper.PageInfo;
import com.wu.Query.UserQuery;
import com.wu.model.TUser;
import com.wu.result.R;
import com.wu.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/api/userList")
    public R getUserList(@RequestParam(value = "current",required = false) Integer current) {
        if(current == null || current == 0){
            current = 1;
        }

        PageInfo<TUser> userByPage = userService.getUserByPage(current);

        return R.OK(userByPage);
    }

    @GetMapping("/api/user/{id}")
    public R userDetail(@PathVariable(value = "id") Integer id) {
        TUser tUser = userService.getUserDetailById(id);
        return R.OK(tUser);
    }

    @PostMapping("/api/user/insert")
    public R userInsert(UserQuery userQuery,@RequestHeader(value = "Authorization") String token){

        userQuery.setToken(token);
        int flag = userService.insertUser(userQuery);
        return flag >= 1 ? R.OK() : R.FAIL();
    }

    @PostMapping("/api/user/edit")
    public R editUser(UserQuery userQuery, @RequestHeader(value = "Authorization") String token) {
        userQuery.setToken(token);
        int update = userService.updateUser(userQuery);
        return update >= 1 ? R.OK() : R.FAIL();
    }

    @GetMapping("/api/user/delete/{id}")
    public R delUser(@PathVariable(value = "id") Integer id) {
        int del = userService.delUserById(id);
        return del >= 1 ? R.OK() : R.FAIL();
    }

    @GetMapping("/api/user/batchDel")
    public R batchDelUser(@RequestParam(value = "ids") String ids) {
        //字符串转成字符串数组
        List<String> idList = Arrays.asList(ids.split(","));

        int batchDel = userService.batchDelUserIds(idList);

        return batchDel >= idList.size() ? R.OK() : R.FAIL();
    }

    @GetMapping(value = "/api/owner")
    public R owner() {
        List<TUser> ownerList = userService.getOwnerList();
        return R.OK(ownerList);
    }

}
