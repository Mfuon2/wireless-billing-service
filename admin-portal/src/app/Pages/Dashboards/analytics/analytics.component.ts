import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import { faTh, faCheck, faTrash, faAngleDown, faAngleUp } from '@fortawesome/free-solid-svg-icons';

declare let d3: any;

@Component({
  selector: 'app-analytics',
  templateUrl: './analytics.component.html',
  encapsulation: ViewEncapsulation.None
})
export class AnalyticsComponent implements OnInit {
  barChatOption;
  barChatData;
  lineChartOption;
  lineChartData;

  faTh = faTh;
  faCheck = faCheck;
  faTrash = faTrash;
  faAngleDown = faAngleDown;
  faAngleUp = faAngleUp;

  heading = 'Analytics Dashboard - {Under Development}';
  subheading = 'This is an example dashboard created using build-in elements and components.';
  icon = 'pe-7s-plane icon-gradient bg-tempting-azure';

  ngOnInit() {
    this.loadDiscreteBarChat();
    this.loadLineChart();
  }

  loadDiscreteBarChat() {
    this.barChatOption = {
      chart: {
        type: 'discreteBarChart',
        height: 450,
        margin : {
          top: 20,
          right: 20,
          bottom: 50,
          left: 100
        },
        x(d){return d.label; },
        y(d){return d.value; },
        showValues: true,
        valueFormat(d){
          return d3.format(',.4f')(d);
        },
        duration: 500,
        xAxis: {
          axisLabel: 'Months'
        },
        yAxis: {
          axisLabel: 'Clients'
        }
      }
    };
    this.barChatData = [
      {
        key: 'Cumulative Return',
        values: [
          {
            label : 'Jan' ,
            value : -29.765957771107
          } ,
          {
            label : 'Feb' ,
            value : 0
          } ,
          {
            label : 'Mar' ,
            value : 32.807804682612
          } ,
          {
            label : 'Apr' ,
            value : -29.765957771107
          },
          {
            label : 'May' ,
            value : 19.45946739256
          } ,
          {
            label : 'Jun' ,
            value : 0.19434030906893
          } ,
          {
            label : 'Jul' ,
            value : -98.079782601442
          } ,
          {
            label : 'Aug' ,
            value : -13.925743130903
          } ,
          {
            label : 'Sept' ,
            value : 0.19434030906893
          } ,
          {
            label : 'Oct' ,
            value : 50.1387322875705
          },
          {
            label : 'Nov' ,
            value : 0.19434030906893
          } ,
          {
            label : 'Dec' ,
            value : 0.19434030906893
          }
        ]
      }
    ];
  }

  loadLineChart() {
    this.lineChartOption = {
      chart: {
        type: 'lineWithFocusChart',
        height: 450,
        margin : {
          top: 20,
          right: 20,
          bottom: 40,
          left: 55
        },
        x(d){ return d.x; },
        y(d){ return d.y; },
        useInteractiveGuideline: true,
        dispatch: {
          stateChange(e){ console.log('stateChange'); },
          changeState(e){ console.log('changeState'); },
          tooltipShow(e){ console.log('tooltipShow'); },
          tooltipHide(e){ console.log('tooltipHide'); }
        },
        xAxis: {
          axisLabel: 'Time (ms)'
        },
        yAxis: {
          axisLabel: 'Voltage (v)',
          tickFormat(d){
            return d3.format('.02f')(d);
          },
          axisLabelDistance: -10
        },
        callback(chart){
          console.log('!!! lineChart callback !!!');
        }
      },
      title: {
        enable: true,
        text: 'Title for Line Chart'
      },
      subtitle: {
        enable: true,
        text: 'Subtitle for simple line chart. Lorem ipsum dolor sit amet, at eam blandit sadipscing, vim adhuc sanctus disputando ex, cu usu affert alienum urbanitas.',
        css: {
          'text-align': 'center',
          margin: '10px 13px 0px 7px'
        }
      },
      caption: {
        enable: true,
        html: '<b>Figure 1.</b> Lorem ipsum dolor sit amet, at eam blandit sadipscing, <span style="text-decoration: underline;">vim adhuc sanctus disputando ex</span>, cu usu affert alienum urbanitas. <i>Cum in purto erat, mea ne nominavi persecuti reformidans.</i> Docendi blandit abhorreant ea has, minim tantas alterum pro eu. <span style="color: darkred;">Exerci graeci ad vix, elit tacimates ea duo</span>. Id mel eruditi fuisset. Stet vidit patrioque in pro, eum ex veri verterem abhorreant, id unum oportere intellegam nec<sup>[1, <a href="https://github.com/krispo/angular-nvd3" target="_blank">2</a>, 3]</sup>.',
        css: {
          'text-align': 'justify',
          margin: '10px 13px 0px 7px'
        }
      }
    };
    this.lineChartData = this.sinAndCos();
  }

  /*Random Data Generator */
sinAndCos() {
    let sin = [], sin2 = [],
        cos = [];

    // Data is represented as an array of {x,y} pairs.
    for (let i = 0; i < 100; i++) {
      sin.push({x: i, y: Math.sin(i / 10)});
      sin2.push({x: i, y: i % 10 === 5 ? null : Math.sin(i / 10) * 0.25 + 0.5});
      cos.push({x: i, y: .5 * Math.cos(i / 10 + 2) + Math.random() / 10});
    }

    // Line chart data should be sent as an array of series objects.
    return [
      {
        values: sin,      // values - represents the array of {x,y} data points
        key: 'Sine Wave', // key  - the name of the series.
        color: '#ff7f0e'  // color - optional: choose your own line color.
      },
      {
        values: cos,
        key: 'Cosine Wave',
        color: '#2ca02c',
        area: true
      },
      {
        values: sin2,
        key: 'Another sine wave',
        color: '#7777ff',
        area: false      // area - set to true if you want this line to turn into a filled area chart.
      }
    ];
  }

}
