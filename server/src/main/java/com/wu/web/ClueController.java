package com.wu.web;

import com.github.pagehelper.PageInfo;
import com.wu.model.TClue;
import com.wu.result.R;
import com.wu.service.ClueService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ClueController {

    @Resource
    ClueService clueService;
    @GetMapping(value = "/api/clues")
    public R cluePage(@RequestParam(value = "current", required = false) Integer current) {
        if (current == null) {
            current = 1;
        }
        PageInfo<TClue> pageInfo = clueService.getClueByPage(current);
        return R.OK(pageInfo);
    }

    @PostMapping(value = "/api/importExcel")
    public R importExcel(MultipartFile file , @RequestHeader(value = "Authorization") String token) throws IOException {
//        先接到前端传过来的文件，并转化成数据流
        clueService.importExcel(file.getInputStream(),token);
        return R.OK();
    }
}
