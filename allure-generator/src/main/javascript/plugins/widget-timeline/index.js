import TimelineWidget from './TimelineWidget';

allure.api.addWidget('widgets', 'timeline', TimelineWidget);

allure.api.addTranslation('en', {
    widget: {
        timeline: {
            name: 'Timeline'
        }
    }
});
