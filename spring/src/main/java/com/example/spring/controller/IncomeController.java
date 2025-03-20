package com.example.spring.controller;


import com.example.spring.common.R;
import com.example.spring.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;



@RestController
@RequestMapping("/api/income")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;


    @GetMapping("/chart")
    public R<Map<String,Object>> getChart() {
        return R.success(incomeService.getChart());
    }


    @GetMapping("/week")
    public R<Map<String,Object>> getWeekIncome() {
        return R.success(incomeService.getWeekIncome());
    }


    @GetMapping("/month")
    public R<Map<String,Object>> getMonthIncome() {
        return R.success(incomeService.getMonthIncome());
    }

}
