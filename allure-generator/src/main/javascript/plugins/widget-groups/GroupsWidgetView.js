import template from './GroupsWidgetView.hbs';
import {View} from 'backbone.marionette';
import {className, regions} from '../../decorators';
import PieChartView from '../../components/graph-pie-dynamic-chart/PieChartView';

@className('groups-widget')
@regions({
    chart: '.groups-widget__content'
})
export default class GroupsWidgetView extends View {
    template = template;

    onRender() {
        this.showChildView('chart', new PieChartView({
            model: this.getGroupsChartData(),
            showLegend: true
        }));
    }

    getGroupsChartData() {
        const groupsData = this.model.get('items');
        return groupsData.map(function(group) {
            var value = group.testCaseCount;
            return {
                name: group.groupName,
                value: value,
                part: value / this.model.length
            };
        }, this);
    }
}
