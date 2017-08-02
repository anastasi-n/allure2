import MappingsLayout from './MappingsLayout';

allure.api.addTab('mappings', {
    title: 'tab.mappings.name', icon: 'fa fa-map-signs',
    route: 'mappings(/:testcaseId)(/:attachmentId)',
    onEnter: (...routeParams) => new MappingsLayout({routeParams})
});

allure.api.addTranslation('en', {
    tab: {
        mappings: {
            name: 'Mappings'
        }
    }
});
