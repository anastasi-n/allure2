import TestCaseRequirementView from './TestCaseRequirementView';

allure.api.addTestResultBlock(TestCaseRequirementView, {position: 'before'});

allure.api.addTranslation('en', {
    testCase: {
        requirements: {
            name: 'Requirement ids'
        }
    }
});
