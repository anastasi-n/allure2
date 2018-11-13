import {View} from 'backbone.marionette';
import {on} from '../../decorators';
import template from './GlobalVariablesWidget.hbs';

export default class GlobalVariablesWidget extends View {
    template = template;

    initialize() {
        this.listLimit = 10;
    }

    @on('click .global-variables-widget__expand')
    onExpandClick() {
        this.listLimit = this.model.get('items').length;
        this.render();
    }

    serializeData() {
        const items = this.model.get('items');
        return {
            items: items.slice(0, this.listLimit),
            overLimit: items.length > this.listLimit
        };
    }
}
