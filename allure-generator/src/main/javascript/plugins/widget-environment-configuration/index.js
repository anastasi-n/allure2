import EnvironmentConfigurationWidget from './EnvironmentConfigurationWidget';

allure.api.addWidget('widgets', 'environment', EnvironmentConfigurationWidget);

allure.api.addTranslation('en', {
    widget: {
        environmentConfiguration: {
            name: 'Environment',
            showAll: 'Show all'
        }
    }
});
