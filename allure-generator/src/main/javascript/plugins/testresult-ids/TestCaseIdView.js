import './styles.scss';
import {View} from 'backbone.marionette';
import {className} from '../../decorators';
import template from './TestCaseIdView.hbs';

@className('pane__section')
class TestCaseIdView extends View {
    template = template;

    serializeData() {
        return {
            testCaseIds: this.model.get('labels').filter(l => l.name === 'testCaseId').map(l => l.value)
        };
    }
}

export default TestCaseIdView;
