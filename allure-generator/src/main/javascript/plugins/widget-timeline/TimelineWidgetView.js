
import {className} from '../../decorators';
import duration from '../../helpers/duration';
import {select} from 'd3-selection';
import 'd3-selection-multi';
import template from './TimelineWidgetView.hbs';
import {axisBottom} from 'd3-axis';
import TimelineView from '../tab-timeline/TimelineView';

const BAR_GAP = 2;
const BAR_HEIGHT = 20;
const PADDING = 30;

@className('timeline-widget')
class TimelineWidgetView extends TimelineView {

    setupViewport() {
        this.$el.html(template({PADDING}));
        this.svgChart = select(this.$el[0]).select('.timeline__chart_svg');
        this.svgBrush = select(this.$el[0]).select('.timeline__brush_svg');
    }

    doShow() {
        this.width = this.$el.width() > 2 * PADDING ? this.$el.width() - 2 * PADDING : this.$el.width();

        const domain = [this.collection.time.start, this.collection.time.stop];
        this.chartX.domain(domain).range([0, this.width]);
        this.brushX.domain(domain).range([0, this.width]);

        this.setupViewport();

        let height = 10;
        const group = this.svgChart.select('.timeline__plot');
        height += this.drawTestGroups(this.data, height, group, true);

        select(this.$el[0]).select('.timeline__brush')
            .style('top', () => { return height + BAR_HEIGHT + 'px'; });

        this.xChartAxis = this.makeAxis(
            axisBottom(),
            this.svgChart.select('.timeline__chart__axis_x'),
            {
                scale: this.chartX,
                tickFormat: () => '',
                tickSizeOuter: 0,
                tickSizeInner: height
            }
        );

        this.xBrushAxis = this.makeAxis(
            axisBottom(),
            this.svgBrush.select('.timeline__brush__axis_x'),
            {
                scale: this.chartX,
                tickFormat: d => duration(d - this.collection.time.start, 2),
                tickSizeOuter: 0
            },
            {
                top: BAR_GAP,
                left: PADDING
            }
        );

        this.svgChart.attr('height', () => {
            return height;
        });

        super.onRender();
    }
}

export default TimelineWidgetView;
