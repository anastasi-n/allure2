import BaseChartView from '../../components/graph-base/BaseChartView';
import TooltipView from '../../components/tooltip/TooltipView';
import {on} from '../../decorators';
import {arc, pie} from 'd3-shape';
import {interpolate} from 'd3-interpolate';
import {select} from 'd3-selection';
import escape from '../../utils/escape';


class PieChartView extends BaseChartView {

    initialize(options) {
        this.options = options;
        this.model = this.options.model;
        this.showLegend = this.options ?  this.options.showLegend || false : false;

        this.arc = arc();
        this.pie = pie().sort(null).value(d => d.value);
        this.tooltip = new TooltipView({position: 'center'});
        this.data = this.model;
        this.updateChartColors();
    }

    setupViewport() {
        super.setupViewport();
        if(this.showLegend) {
            this.$el.append(this.getLegendTpl());
        }
        return this.svg;
    }

    onAttach() {
        const data = this.data;
        const width = this.$el.outerWidth();
        const radius = width/4 - 10;
        var leftOffset = width / 2;
        if(this.showLegend) {
            leftOffset -= 70;
        }
        this.arc.innerRadius(0).outerRadius(radius);

        this.svg = this.setupViewport();
        var sectors = this.svg.select('.chart__plot')
            .attrs({transform: `translate(${leftOffset},${radius})`})
            .selectAll('.chart__arc').data(this.pie(data))
            .enter()
            .append('path')
            .attr('class', d => 'chart__arc chart__arc_group_' + d.data.name)
            .attr('style', d => 'fill: ' + d.data.color);
        this.bindTooltip(sectors);

        if(this.firstRender) {
            sectors.transition().duration(750).attrTween('d', d => {
                const startAngleFn = interpolate(0, d.startAngle);
                const endAngleFn = interpolate(0, d.endAngle);
                return t =>
                    this.arc({startAngle: startAngleFn(t), endAngle: endAngleFn(t)});
            });
        } else {
            sectors.attr('d', d => this.arc(d));
        }
        super.onAttach();
    }

    formatNumber(number) {
        return (Math.floor(number * 100) / 100).toString();
    }

    getChartTitle() {
        const {passed, total} = this.statistic;
        return this.formatNumber((passed || 0) / total * 100) + '%';
    }

    getTooltipContent({data}) {
        const value = data.value || 0;
        const part = data.part || 0;
        const name = data.name;
        return escape`
            ${value} scenarios (${this.formatNumber(part * 100)}%)<br>
            ${name}
        `;
    }

    getLegendTpl() {
        return `<div class="chart__legend">
    ${this.data.map((item) =>
            `<div class="chart__legend-row" data-group="${item.name}">
<span class="chart__legend-icon" style="background: ${item.color}"></span> ${item.name}</div>`
        ).join('')}
</div>`;
    }

    updateChartColors() {
        var colorIncrementAmount = Math.floor(0xffffff / (this.data.length + 1) - 5000);
        this.data.forEach(d => {
            d['color'] = '#' + (0x1000000 + (colorIncrementAmount * (this.data.indexOf(d) + 1)) + Math.random() * 100000).toString(16).substr(1,6);
        });
    }

    @on('mouseleave .chart__legend-row')
    onLegendOut() {
        this.hideTooltip();
    }

    @on('mouseenter .chart__legend-row')
    onLegendHover(e) {
        const el = this.$(e.currentTarget);
        const group = el.data('group');
        const sector = this.$('.chart__arc_group_' + group)[0];
        const data = select(sector).datum();
        this.showTooltip(data, sector);
    }
}

export default PieChartView;