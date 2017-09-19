import GroupsChartView from './GroupsWidgetView';

allure.api.addWidget('graph', 'groups', GroupsChartView);

allure.api.addTranslation('en', {
    widget: {
        groups: {
            name: 'Groups'
        }
    }
});