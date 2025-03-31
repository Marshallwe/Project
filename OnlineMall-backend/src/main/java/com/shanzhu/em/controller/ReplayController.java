package com.shanzhu.em.controller;

import com.shanzhu.em.entity.Replay;
import com.shanzhu.em.service.ReplayService;
import com.shanzhu.em.utils.ApiResultHandler;
import com.shanzhu.em.utils.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class ReplayController {

    private final ReplayService replayService;


    @PostMapping("/replay")
    public R add(@RequestBody Replay replay) {
        int data = replayService.add(replay);
        if (data != 0) {
            return ApiResultHandler.buildApiResult(200, "Added successfully！", data);
        } else {
            return ApiResultHandler.buildApiResult(400, "Add failure！", null);
        }
    }


    @GetMapping("/replay/{messageId}")
    public R<List<Replay>> findAllById(@PathVariable("messageId") Integer messageId) {
        return ApiResultHandler.buildApiResult(200, "Query against messageId", replayService.findAllById(messageId));
    }
}
