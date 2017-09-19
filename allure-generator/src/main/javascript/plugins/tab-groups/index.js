'use strict';

allure.api.addTranslation('en', {
    tab: {
        groups: {
            name: 'Groups'
        }
    }
});

allure.api.addTab('groups', {
    title: 'tab.groups.name',
    icon: 'fa fa-list',
    route: 'groups(/)(:testGroup)(/)(:testResult)(/)(:testResultTab)(/)',
    onEnter: (testGroup, testResult, testResultTab) => new allure.components.TreeLayout({
        testGroup,
        testResult,
        testResultTab,
        tabName: 'tab.groups.name',
        baseUrl: 'groups',
        url: 'data/groups.json'
    })
});
