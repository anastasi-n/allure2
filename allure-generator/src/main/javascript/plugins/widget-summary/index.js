import SummaryWidgetView from './SummaryWidgetView';

allure.api.addWidget('widgets', 'summary', SummaryWidgetView);

allure.api.addTranslation('en', {
    widget: {
        summary: {
            coverage: 'Coverage'
        }
    }
});
