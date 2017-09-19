import KnownIssuesWidget from './KnownIssuesWidget';

allure.api.addWidget('widgets', 'knownIssues', KnownIssuesWidget);

allure.api.addTranslation('en', {
    widget: {
        knownIssues: {
            name: 'Known Issues',
            identifier: 'Jira Number',
            count: 'Number Of Detections',
            type: 'Issue Type',
            status: 'State',
            showAll: 'Show all known issues',
            empty: 'Known issues is not detected'
        }
    }
});
