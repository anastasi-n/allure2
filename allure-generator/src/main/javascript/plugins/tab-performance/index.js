import PerformanceLayout from './PerformanceLayout';

allure.api.addTab('performance', {
    title: 'tab.performance.name', icon: 'fa fa-dashboard',
    route: 'performance',
    onEnter: () => new PerformanceLayout()
});

allure.api.addTranslation('en', {
    tab: {
        performance: {
            name: 'Performance',
            totalPages: 'Total Pages',
            pageUrl: 'Page',
            loadsCount: 'Number of Loads',
            minLoadTime: 'Min Load Time',
            maxLoadTime: 'Max Load Time',
            averageLoadTime: 'Average Load Time',
            totalAssertions: 'Number of Asserts',
            passed: 'Passed',
            known: 'Known',
            failed: 'Failed',
            showAll: 'Show all pages',
            showPercentage: 'Show percentage'
        }
    }
});
