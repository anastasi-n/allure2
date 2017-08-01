import './styles.scss';
import PopoverView from '../../../components/popover/PopoverView';
import {className, on} from '../../../decorators';
import template from './TotalResultSelectView.hbs';
import settings from '../../../utils/settings';
import $ from 'jquery';

@className('total-result-option-select popover')
class TotalResultSelectView extends PopoverView {

    initialize(params) {
        this.params = params;
        super.initialize({position: 'right'});
    }

    setContent() {
        this.$el.html(template({
            options: this.params.selectOptions,
            currentOptionId: settings.getTotalResultSelectOption()
        }));
    }

    show(anchor) {
        super.show(null, anchor);
        this.delegateEvents();
        setTimeout(() => {
            $(document).one('click', () => this.hide());
        });
    }

    @on('click .option-select__item')
    onTotalOptionsClick(e) {
        const optionId = this.$(e.currentTarget).data('id');
        settings.setTotalResultSelectOption(optionId);
    }
}

export default TotalResultSelectView;
