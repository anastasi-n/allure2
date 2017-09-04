import MappingsTreeLayout from './MappingsTreeLayout';

allure.api.addTab('mappings', {
    title: 'tab.mappings.name', icon: 'fa fa-map-signs',
    route: 'mappings(/)(:testGroup)(/)(:testResult)(/)(:testResultTab)(/)',
    onEnter: (testGroup, testResult, testResultTab) => new MappingsTreeLayout({
            testGroup,
            testResult,
            testResultTab,
            tabName: 'tab.mappings.name',
            baseUrl: 'mappings',
            url: 'data/mappings.json'
        })
});

allure.api.addTranslation('en', {
    tab: {
        mappings: {
            name: 'Mappings'
        }
    }
});
