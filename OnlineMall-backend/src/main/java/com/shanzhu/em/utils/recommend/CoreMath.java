package com.shanzhu.em.utils.recommend;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Lists;
import com.shanzhu.em.utils.recommend.dto.RelateDTO;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CoreMath {


    public List<Long> recommend(Long userId, List<RelateDTO> list) {
        Map<Double, Long> distances = computeNearestNeighbor(userId, list);
        if(MapUtil.isEmpty(distances)){
            return Collections.EMPTY_LIST;
        }
        Long nearest = distances.values().iterator().next();
        Iterator<Long> iterator = distances.values().iterator();
        while (iterator.hasNext()) {
            nearest = iterator.next();
        }
        Map<Long, List<RelateDTO>> userMap = list.stream().collect(Collectors.groupingBy(RelateDTO::getUserId));

        List<Long> neighborItemList = userMap.get(nearest).stream().map(e -> e.getProductId()).collect(Collectors.toList());
        List<Long> userItemList = userMap.get(userId).stream().map(e -> e.getProductId()).collect(Collectors.toList());

        List<Long> recommendList = new ArrayList<>();
        for (Long item : neighborItemList) {
            if (!userItemList.contains(item)) {
                recommendList.add(item);
            }
        }
        Collections.sort(recommendList);
        return recommendList;
    }


    private Map<Double, Long> computeNearestNeighbor(Long userId, List<RelateDTO> list) {
        Map<Long, List<RelateDTO>> userMap = list.stream().collect(Collectors.groupingBy(RelateDTO::getUserId));
        Map<Double, Long> distances = new TreeMap<>();

        userMap.forEach((k, v) -> {
            if (k.intValue() != userId.intValue()) {
                Double distance = pearsonDis(v, userMap.get(userId));
                if(distance != null){
                    distances.put(distance, k);
                }
            }
        });
        return distances;
    }


    private Double pearsonDis(List<RelateDTO> xList, List<RelateDTO> yList) {
        if(CollUtil.isEmpty(xList) || CollUtil.isEmpty(yList)){
            return null;
        }

        List<Integer> xs = Lists.newArrayList();
        List<Integer> ys = Lists.newArrayList();
        xList.forEach(x -> {
            yList.forEach(y -> {
                if (x.getProductId().intValue() == y.getProductId().intValue()) {
                    xs.add(x.getIndex());
                    ys.add(y.getIndex());
                }
            });
        });
        return getRelate(xs, ys);
    }


    public static Double getRelate(List<Integer> xs, List<Integer> ys) {
        int n = xs.size();
        double Ex = xs.stream().mapToDouble(x -> x).sum();
        double Ey = ys.stream().mapToDouble(y -> y).sum();
        double Ex2 = xs.stream().mapToDouble(x -> Math.pow(x, 2)).sum();
        double Ey2 = ys.stream().mapToDouble(y -> Math.pow(y, 2)).sum();
        double Exy = IntStream.range(0, n).mapToDouble(i -> xs.get(i) * ys.get(i)).sum();
        double numerator = Exy - Ex * Ey / n;
        double denominator = Math.sqrt((Ex2 - Math.pow(Ex, 2) / n) * (Ey2 - Math.pow(Ey, 2) / n));
        if (Double.isNaN(numerator) || denominator == 0) {
            return 0.0;
        }
        return -numerator / denominator;
    }

}
