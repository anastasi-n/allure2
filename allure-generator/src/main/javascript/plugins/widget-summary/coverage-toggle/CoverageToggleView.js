import './styles.scss';
import {View} from 'backbone.marionette';
import {on} from '../../../decorators';
import settings from '../../../utils/settings';
import template from './CoverageToggleView.hbs';

class CoverageToggleView extends View {
    template = template;

    serializeData() {
        const coverageChecked = settings.isCoverageChecked();
        return {
            active: coverageChecked
        };
    }

    @on('click .button')
    onCheckChange(e) {
        const el = this.$(e.currentTarget);
        el.toggleClass('button_active');
        const checked = el.hasClass('button_active');
        settings.setCoverageChecked(checked);
    }
}

export default CoverageToggleView;
