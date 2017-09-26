import template from './TimelineWidget.hbs';
import {View} from 'backbone.marionette';
import {regions} from '../../decorators';
import TimelineWidgetView from './TimelineWidgetView';
import TreeCollection from '../../data/tree/TreeCollection';

@regions({
    chart: '.timeline-widget'
})
export default class TimelineWidget extends View {
    template = template;

    initialize() {
        super.initialize();
        var url = 'data/timeline.json';
        this.items = new TreeCollection([], {url});
    }

    loadData() {
        return this.items.fetch();
    }

    onRender() {
        const dataPromise = this.loadData();
        if (dataPromise) {
            dataPromise
                .then(() => {
                    this.showChildView('chart', this.getContentView());
                });
        } else {
            this.showChildView('chart', this.getContentView());
        }
    }

    getContentView() {
        return new TimelineWidgetView({collection: this.items});
    }
}
