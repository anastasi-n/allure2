import './styles.scss';
import DataGridView from '../../components/data-grid/DataGridView';
import {on} from '../../decorators';
import PerformanceDisplayToggleView from './performance-display/PerformanceDisplayToggleView';
import {regions, className} from '../../decorators';
import settings from '../../utils/settings';
import template from './PerformanceView.hbs';

@className('performance-grid')
@regions({
    performanceOptions: '.performance-options'
})
class PerformanceView extends DataGridView {
    template = template;
    settingsKey = 'performanceSettings';

    initialize() {
        this.listLimit = 17;
        this.pages = this.collection.toJSON();
        this.pages.forEach(page => {
            page.percentagePassed = this.calculatePercentage(page.passedAssertions, page.totalAssertions);
            page.percentageKnown = this.calculatePercentage(page.knownAssertions, page.totalAssertions);
            page.percentageFailed = this.calculatePercentage(page.failedAssertions, page.totalAssertions);
        });
        this.listenTo(settings, 'change', this.render);
    }

    onRender() {
        this.showChildView('performanceOptions', new PerformanceDisplayToggleView({}));
    }

    serializeData() {
        const showPercentage = settings.isPerformanceShowPercentage();
        return {
            sorting: this.getSettings(),
            showPercentage: showPercentage,
            pages: this.applySort(this.pages).slice(0, this.listLimit),
            overLimit: this.pages.length > this.listLimit,
            totalPages: this.pages.length
        };
    }

    @on('click .performance__expand')
    onExpandClick() {
        this.listLimit = this.pages.length;
        this.render();
    }

    calculatePercentage(value, total) {
        return total === 0 ? -1 : Math.round((value / total) * 100, 2);
    }
}

export default PerformanceView;
