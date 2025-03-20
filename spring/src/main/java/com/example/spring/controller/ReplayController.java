package com.example.spring.controller;
import com.example.spring.entity.Replay;
import com.example.spring.service.ReplayService;
import com.example.spring.utils.ApiResultHandler;
import com.example.spring.utils.R;
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

