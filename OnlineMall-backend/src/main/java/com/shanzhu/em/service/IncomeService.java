package com.shanzhu.em.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.shanzhu.em.mapper.IncomeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeMapper incomeMapper;


    public Map<String, Object> getChart() {
        Map<String, Object> chartMap = new HashMap<>();
        List<Map<String, Object>> categoryIncomes = incomeMapper.selectCategoryIncome();

        BigDecimal sumIncome = incomeMapper.selectSumIncome();
        chartMap.put("categoryIncomes", categoryIncomes);
        chartMap.put("sumIncome", sumIncome);
        return chartMap;
    }


    public Map<String, Object> getWeekIncome() {
        ArrayList<BigDecimal> weekIncome = new ArrayList<>();
        ArrayList<String> weekDays = new ArrayList<>();
        DateTime dateTime = DateUtil.beginOfWeek(DateUtil.date());
        for (int i = 0; i < 7; i++) {
            DateTime thisDay = DateUtil.offsetDay(dateTime, i);
            DateTime nextDay = DateUtil.offsetDay(dateTime, i + 1);
            String weekDay = thisDay.toString();
            String formattedWeekday = weekDay.substring(weekDay.indexOf("-") + 1, weekDay.indexOf(" "));
            weekDays.add(formattedWeekday);
            BigDecimal dayIncome = incomeMapper.getDayIncome(thisDay.toString(), nextDay.toString());
            if (dayIncome == null) {
                dayIncome = new BigDecimal(0);
            }
            weekIncome.add(dayIncome);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("weekDays", weekDays);
        map.put("weekIncome", weekIncome);
        return map;
    }


    public Map<String, Object> getMonthIncome() {
        ArrayList<BigDecimal> monthIncome = new ArrayList<>();
        ArrayList<String> monthDays = new ArrayList<>();
        DateTime dateTime = DateUtil.beginOfMonth(DateUtil.date());
        for (int i = 0; i < 30; i++) {
            DateTime thisDay = DateUtil.offsetDay(dateTime, i);
            DateTime nextDay = DateUtil.offsetDay(dateTime, i + 1);
            String weekDay = thisDay.toString();
            String formattedWeekday = weekDay.substring(weekDay.indexOf("-") + 1, weekDay.indexOf(" "));
            monthDays.add(formattedWeekday);
            BigDecimal dayIncome = incomeMapper.getDayIncome(thisDay.toString(), nextDay.toString());
            if (dayIncome == null) {
                dayIncome = new BigDecimal(0);
            }
            monthIncome.add(dayIncome);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("monthDays", monthDays);
        map.put("monthIncome", monthIncome);
        return map;
    }
}
