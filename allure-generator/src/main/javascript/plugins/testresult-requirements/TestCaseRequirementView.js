import './styles.scss';
import {View} from 'backbone.marionette';
import {className} from '../../decorators';
import template from './TestCaseRequirementView.hbs';

@className('pane__section')
class TestCaseRequirementView extends View {
    template = template;

    serializeData() {
        return {
            requirementIds: this.model.get('labels').filter(l => l.name === 'requirementId').map(l => l.value)
        };
    }
}

export default TestCaseRequirementView;
