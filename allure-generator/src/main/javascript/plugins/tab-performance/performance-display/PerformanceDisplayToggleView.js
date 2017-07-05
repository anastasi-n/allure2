import {View} from 'backbone.marionette';
import {on} from '../../../decorators';
import settings from '../../../utils/settings';
import template from './PerformanceDisplayToggleView.hbs';

class PerformanceDisplayToggleView extends View {
    template = template;

    serializeData() {
        return {
            performanceShowPercentage: settings.isPerformanceShowPercentage()
        };
    }

    @on('click .button')
    onCheckChange(e) {
        const el = this.$(e.currentTarget);
        el.toggleClass('button_active');
        const checked = el.hasClass('button_active');
        settings.setPerformanceShowPercentage(checked);
    }
}

export default PerformanceDisplayToggleView;
