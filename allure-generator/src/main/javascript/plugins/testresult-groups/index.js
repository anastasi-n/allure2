import TestCaseGroupView from './TestCaseGroupView';

allure.api.addTestResultBlock(TestCaseGroupView, {position: 'before'});

allure.api.addTranslation('en', {
    testCase: {
        groups: {
            name: 'Test case groups'
        }
    }
});
