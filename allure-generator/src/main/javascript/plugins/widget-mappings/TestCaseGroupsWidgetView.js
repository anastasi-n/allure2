import template from './TestCaseGroupsWidgetView.hbs';
import {View} from 'backbone.marionette';
import {className, regions} from '../../decorators';
import PieChartView from '../../components/graph-pie-dynamic-chart/PieChartView';

@className('test-case-groups-widget')
@regions({
    chart: '.test-case-groups-widget__content'
})
export default class TestCaseGroupsWidgetView extends View {
    template = template;

    onRender() {
        this.showChildView('chart', new PieChartView({
            model: this.getChartData(),
            showLegend: true
        }));
    }

    getChartData() {
        var items = [].concat(...this.model.get('items').
                filter(i => i.type === 'test case group').map(i => i.items));
        return items.map(function(item) {
            var value = item.count;
            return {
                name: item.name,
                value: value,
                part: value / this.model.length
            };
        }, this);
    }
}
