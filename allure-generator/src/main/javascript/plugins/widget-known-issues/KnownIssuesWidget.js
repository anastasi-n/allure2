import DataGridView from '../../components/data-grid/DataGridView';
import {on} from '../../decorators';
import template from './KnownIssuesWidget.hbs';

export default class KnownIssuesWidget extends DataGridView {
    template = template;
    settingsKey = 'knownIssuesSettings';

    initialize() {
        this.listLimit = 10;
    }

    @on('click .known-issues-widget__expand')
    onExpandClick() {
        this.listLimit = this.model.get('items').length;
        this.render();
    }

    serializeData() {
        var knownIssues = this.model.get('items');
        return {
            sorting: this.getSettings(),
            knownIssues: this.applySort(knownIssues).slice(0, this.listLimit),
            overLimit: knownIssues.length > this.listLimit
        };
    }
}
