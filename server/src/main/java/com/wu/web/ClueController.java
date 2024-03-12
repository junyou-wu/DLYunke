package com.wu.web;

import com.github.pagehelper.PageInfo;
import com.wu.model.TClue;
import com.wu.result.R;
import com.wu.service.ClueService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
