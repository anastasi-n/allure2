import template from './GroupsWidgetView.hbs';
import {View} from 'backbone.marionette';
import {className, regions} from '../../decorators';
import GroupsChartView from '../../components/graph-groups-chart/GroupsChartView';

@className('groups-widget')
@regions({
    chart: '.groups-widget__content'
})
export default class GroupsWidgetView extends View {
    template = template;

    onRender() {
        this.showChildView('chart', new GroupsChartView({
            model: this.getGroupsChartData(),
            showLegend: true
        }));
    }

    getGroupsChartData() {
        const groupsData = this.model.get('items');
        const totalScenarios = this.model.length;
        var colorIncrementAmount = Math.floor(0xffffff/(groupsData.length + 1) - 5000);
        return groupsData.map(function(group) {
            var value = group.testCaseCount;
            return {
                name: group.groupName,
                value: value,
                part: value / totalScenarios,
                color: '#' + (0x1000000 + (colorIncrementAmount * (groupsData.indexOf(group) + 1)) + Math.random()*100000).toString(16).substr(1,6)
            };
        }, this);
    }
}
