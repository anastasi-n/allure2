import './styles.scss';
import {View} from 'backbone.marionette';
import {className} from '../../decorators';
import template from './TestCaseGroupView.hbs';

@className('pane__section')
class TestCaseGroupView extends View {
    template = template;

    serializeData() {
        return {
            testCaseGroups: this.model.get('labels').filter(l => l.name === 'testCaseGroup').map(l => l.value)
        };
    }
}

export default TestCaseGroupView;
