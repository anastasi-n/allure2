import TestCaseGroupsChartView from './TestCaseGroupsWidgetView';

allure.api.addWidget('graph', 'mappings', TestCaseGroupsChartView);

allure.api.addTranslation('en', {
    widget: {
        testCaseGroups: {
            name: 'Test Case Groups'
        }
    }
});