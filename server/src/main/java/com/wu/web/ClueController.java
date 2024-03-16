package com.wu.web;

import com.github.pagehelper.PageInfo;
import com.wu.Query.ClueQuery;
import com.wu.Query.ClueRemarkQuery;
import com.wu.model.TClue;
import com.wu.model.TClueRemark;
import com.wu.result.R;
import com.wu.service.ClueService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
public class ClueController {

    @Resource
    ClueService clueService;

    @PreAuthorize(value = "hasAuthority('clue:list')")
    @GetMapping(value = "/api/clues")
    public R cluePage(@RequestParam(value = "current", required = false) Integer current) {
        if (current == null) {
            current = 1;
        }
        PageInfo<TClue> pageInfo = clueService.getClueByPage(current);
        return R.OK(pageInfo);
    }
    @PreAuthorize(value = "hasAuthority('clue:import')")
    @PostMapping(value = "/api/importExcel")
    public R importExcel(MultipartFile file , @RequestHeader(value = "Authorization") String token) throws IOException {
//        先接到前端传过来的文件，并转化成数据流
        clueService.importExcel(file.getInputStream(),token);
        return R.OK();
    }

    @GetMapping("/api/clue/{phone}")
    public R checkPhone(@PathVariable(value = "phone") String phone) {
        Boolean check = clueService.checkPhone(phone);
        return check ? R.OK() : R.FAIL();
    }
    @PreAuthorize(value = "hasAuthority('clue:add')")
    @PostMapping("/api/clue/add")
    public R addClue(ClueQuery clueQuery,@RequestHeader(value = "Authorization") String token){
        clueQuery.setToken(token);
        int add = clueService.addClue(clueQuery);

        return add >= 1 ? R.OK() : R.FAIL();
    }
    @PreAuthorize(value = "hasAuthority('clue:edit')")
    @PostMapping("/api/clue/edit")
    public R editClue(ClueQuery clueQuery,@RequestHeader(value = "Authorization") String token){
        clueQuery.setToken(token);
        int add = clueService.updateClue(clueQuery);

        return add >= 1 ? R.OK() : R.FAIL();
    }
    @PreAuthorize(value = "hasAuthority('clue:view')")
    @GetMapping("/api/clue/detail/{id}")
    public R loadClue(@PathVariable(value = "id") Integer id){
        TClue tClue = clueService.getClueById(id);
        return R.OK(tClue);
    }
    @PreAuthorize(value = "hasAuthority('clue:delete')")
    @GetMapping(value = "/api/clue/delete/{id}")
    public R delClue(@PathVariable(value = "id") Integer id) {
        int del = clueService.delClueById(id);
        return del >= 1 ? R.OK() : R.FAIL();
    }

    @GetMapping(value = "/api/clue/batchDel")
    public R batchDelClue(@RequestParam(value = "ids") String ids){
        List<String> idList = Arrays.asList(ids.split(","));

        int batchDel = clueService.batchDelActivitiesById(idList);

        return batchDel >= idList.size() ? R.OK() : R.FAIL();
    }

    @GetMapping("/api/clue/remark")
    public R clueRemarkPage(@RequestParam(value = "current", required = false) Integer current,
                            @RequestParam(value = "clueId") Integer clueId) {

        ClueRemarkQuery clueRemarkQuery = new ClueRemarkQuery();
        clueRemarkQuery.setClueId(clueId);

        if (current == null) {
            current = 1;
        }
        PageInfo<TClueRemark> pageInfo = clueService.getClueRemarkByPage(current, clueRemarkQuery);
        return R.OK(pageInfo);
    }

    @PostMapping("/api/clue/remark")
    public R addActivityRemark(@RequestBody ClueRemarkQuery clueRemarkQuery, @RequestHeader(value = "Authorization") String token) {
        //axios提交post请求，提交过来的是json数据，使用@RequestBody注解接收
        clueRemarkQuery.setToken(token);
        int save = clueService.saveClueRemark(clueRemarkQuery);
        return save >= 1 ? R.OK( ) : R.FAIL();
    }

    @GetMapping("/api/clue/remark/{id}")
    public R getRemark(@PathVariable(value = "id") Integer id){

        TClueRemark tClueRemark =  clueService.getRemark(id);

        return R.OK(tClueRemark);
    }

    @PostMapping("/api/clue/remark/edit")
    public R editActivityRemark(@RequestBody ClueRemarkQuery clueRemarkQuery , @RequestHeader(value = "Authorization") String token){
        clueRemarkQuery.setToken(token);
        int update = clueService.updateRemark(clueRemarkQuery);
        return update >= 1 ? R.OK() : R.FAIL();
    }

    @GetMapping("/api/clue/remark/delete/{id}")
    public R delRemark(@PathVariable(value = "id") Integer id){

        int delete =  clueService.deleteRemark(id);

        return delete >= 1 ? R.OK() : R.FAIL();
    }

}
