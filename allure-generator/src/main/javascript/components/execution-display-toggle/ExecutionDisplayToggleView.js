import './styles.scss';
import {View} from 'backbone.marionette';
import {on, className} from '../../decorators';
import settings from '../../utils/settings';
import template from './ExecutionDisplayToggleView.hbs';

@className('execution-display-toggle')
class ExecutionDisplayToggleView extends View {
    template = template;

    serializeData() {
        const stepDisplayParams = settings.get('stepDisplayParams');
        return {
            stepDisplayParams: Object.keys(stepDisplayParams).map(stepDisplayParam => ({
                stepDisplayParam,
                active: stepDisplayParams[stepDisplayParam],
                translation: 'testResult.display.' + stepDisplayParam
            }))
        };
    }

    @on('click .button')
    onCheckChange(e) {
        const el = this.$(e.currentTarget);
        el.toggleClass('button_active');
        const displayParam = el.data('param');
        const checked = el.hasClass('button_active');
        const stepDisplayParams = settings.getStepDisplayParams();
        settings.setStepDisplayParams(Object.assign({}, stepDisplayParams, {[displayParam]: checked}));
    }
}

export default ExecutionDisplayToggleView;
