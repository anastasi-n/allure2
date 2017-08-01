import {View} from 'backbone.marionette';
import {on} from '../../decorators';
import template from './EnvironmentConfigurationWidget.hbs';

const CATEGORY_LIMIT = 9;

export default class EnvironmentConfigurationWidget extends View {
    template = template;

    serializeData() {
        var items = this.model.get('items');
        items.forEach(function(category) {
            if (!category.categoryLimit) {
                category.categoryLimit = CATEGORY_LIMIT;
            }
        });
        return {
            items: items
        };
    }

    @on('click .environment-widget__expand')
    onExpandParametersClick(e) {
        const el = this.$(e.currentTarget);
        const categoryName = el.data('category');
        var category = this.model.get('items').filter(function(category) {
            return category.categoryName === categoryName;
        })[0];
        category.categoryLimit = category.environmentItems.length;
        this.render();
    }
}

