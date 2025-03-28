
<template>
  <div>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-card
        style="
          display: inline-block;
          margin-left: 50px;
          margin-top: 30px;
          font-weight: bold;
          font-size: 22px;
          color: #ffb02a;
        "
        >￥Total:{{ total | numFilter }}</el-card
      >
      <el-tab-pane label="Bar chart" name="bar">
        <div
          id="bar"
          style="width: 1200px; height: 500px; margin: auto auto"
        ></div>
      </el-tab-pane>
      <el-tab-pane label="Pie charts" name="pie">
        <div
          id="pie"
          style="width: 600px; height: 600px; margin: 10px auto"
        ></div>
      </el-tab-pane>

      <el-tab-pane label="Earnings for the week" name="line1">
        <div
          id="weekLine"
          style="width: 900px; height: 500px; margin: 10px auto"
        ></div>
      </el-tab-pane>

      <el-tab-pane label="Revenue for the month" name="line2">
        <div
          id="monthLine"
          style="width: 1500px; height: 500px; margin: 10px auto"
        ></div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import * as echarts from "echarts";
export default {
  name: "IncomeChart",
  data() {
    return {
      sumIncome: 0,
      categoryIncomes: [],
      categoryNames: [],
      incomes: [],
      activeName: "bar",
      totalAll: 0,
      totalWeek: 0,
      totalMonth: 0,
      total: 0,
    };
  },
  methods: {
    handleClick(tab) {
      switch (tab.name) {
        case "bar":
          this.total = this.totalAll;
          break;
        case "pie":
          this.total = this.totalAll;
          break;
        case "line1":
          this.total = this.totalWeek;
          break;
        case "line2":
          this.total = this.totalMonth;
          break;
      }
    },
  },

  mounted() {
    var barChart = echarts.init(document.getElementById("bar"));
    var pieChart = echarts.init(document.getElementById("pie"));
    var lineChart1 = echarts.init(document.getElementById("weekLine"));
    var lineChart2 = echarts.init(document.getElementById("monthLine"));
    var barOption = {
      tooltip: {
        trigger: "item",
      },
      title: {
        text: "Bar chart of income statistics",
        x: "center",
      },
      label: {
        show: true,
        position: "top",
      },
      xAxis: {
        type: "category",
        data: [],
      },
      yAxis: {
        type: "value",
      },
      series: [
        {
          data: [],
          type: "bar",
        },
      ],
    };
    var pieOption = {
      tooltip: {
        trigger: "item",
      },

      title: {
        text: "Pie chart of income statistics",
        x: "center",
      },
      series: [
        {
          type: "pie",
          data: [],
        },
      ],
    };
    var lineOption1 = {
      tooltip: {
        trigger: "item",
      },
      label: {
        show: true,
      },
      title: {
        text: "Earnings for the week",
        x: "center",
      },
      xAxis: {
        type: "category",
        data: ["Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"],
      },
      yAxis: {
        type: "value",
      },
      series: [
        {
          data: [],
          type: "line",
        },
      ],
    };
    var lineOption2 = {
      tooltip: {
        trigger: "item",
      },
      label: {
        show: true,
      },
      title: {
        text: "Revenue for the month",
        x: "center",
      },
      xAxis: {
        type: "category",
        data: [],
      },
      yAxis: {
        type: "value",
      },
      series: [
        {
          data: [],
          type: "line",
        },
      ],
    };
    this.request.get("/api/income/chart").then((res) => {
      if (res.code === "200") {
        let categoryIncomes = res.data.categoryIncomes;
        let categoryNames = categoryIncomes.map((item) => {
          return item.categoryName;
        });
        let incomes = categoryIncomes.map((item) => {
          return item.categoryIncome;
        });
        barOption.xAxis.data = categoryNames;
        barOption.series[0].data = incomes;
        barChart.setOption(barOption);

        for (let i = 0; i < categoryNames.length; i++) {
          let item = { value: incomes[i], name: categoryNames[i] };
          pieOption.series[0].data.push(item);
        }
        pieChart.setOption(pieOption);
        let sum = 0;
        incomes.forEach((item) => {
          sum += item;
        });
        this.total = sum;
        this.totalAll = sum;
      }
    });
    this.request.get("/api/income/week").then((res) => {
      if (res.code === "200") {
        let weekIncome = res.data.weekIncome;
        lineOption1.series[0].data = weekIncome;
        lineChart1.setOption(lineOption1);
        let sum = 0;
        weekIncome.forEach((item) => {
          sum += item;
        });
        this.totalWeek = sum;
      }
    });
    this.request.get("/api/income/month").then((res) => {
      if (res.code === "200") {
        lineOption2.xAxis.data = res.data.monthDays;
        let monthIncome = res.data.monthIncome;
        lineOption2.series[0].data = monthIncome;
        lineChart2.setOption(lineOption2);
        let sum = 0;
        monthIncome.forEach((item) => {
          sum += item;
        });
        this.totalMonth = sum;
      }
    });
  },
  filters: {
    numFilter(value) {

      let realVal = Number(value).toFixed(2);

      return Number(realVal);
    },
  },
};
</script>
