import './styles.scss';
import template from './HistoryTrendWidgetView.hbs';
import {View} from 'backbone.marionette';
import {className, regions} from '../../decorators/index';
import TrendChartView from '../../components/graph-trend-chart/TrendChartView';
import {scaleOrdinal} from 'd3-scale';
import {values} from '../../utils/statuses';


@regions({
    chart: '.history-trend__chart'
})
@className('history-trend')
class HistoryTrendWidgetView extends View {
    template = template;

    onRender() {
        this.showChildView('chart', new TrendChartView({
            model: this.model,
            hideLines: true,
            hidePoints: true,
            colors: scaleOrdinal(['#9f94bf', '#fd5a3e', '#ffd050', '#d35ebe', '#aaa', '#97cc64', '#6495ed']).domain(values),
            keys: values
        }));
    }
}

export default HistoryTrendWidgetView;
