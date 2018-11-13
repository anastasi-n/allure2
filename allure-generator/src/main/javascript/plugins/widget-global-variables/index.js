import GlobalVariablesWidget from './GlobalVariablesWidget';

allure.api.addWidget('widgets', 'globalVariables', GlobalVariablesWidget);

allure.api.addTranslation('en', {
    widget: {
        globalVariables: {
            name: 'Global Variables',
            showAll: 'Show all',
            empty: 'There are no global variables'
        }
    }
});
