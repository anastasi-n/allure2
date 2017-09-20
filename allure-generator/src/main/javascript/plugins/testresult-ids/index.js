import TestCaseIdView from './TestCaseIdView';

allure.api.addTestResultBlock(TestCaseIdView, {position: 'before'});

allure.api.addTranslation('en', {
    testCase: {
        ids :{
            name: 'Test case ids'
        }
    }
});
