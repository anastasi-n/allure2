import './styles.scss';
import {View} from 'backbone.marionette';
import {Model} from 'backbone';
import {findWhere} from 'underscore';
import template from './SummaryWidgetView.hbs';
import {regions, on} from '../../decorators';
import PieChartView from '../../components/graph-pie-chart/PieChartView';
import TotalResultSelectView from './select-view/TotalResultSelectView';
import settings from '../../utils/settings';
import CoverageToggleView from './coverage-toggle/CoverageToggleView';

@regions({
    chart: '.summary-widget__chart',
    coverageToggle: '.coverage__toggle'
})
class SummaryWidgetView extends View {
    template = template;

    initialize(){
        this.listenTo(settings, 'change', this.render);
    }

    onRender() {
        this.optSelectView = new TotalResultSelectView({
            selectOptions: this.selectOptions,
            currentOptionId: this.currentOptionId
        });
        var statistic = this.getStatistic();
        this.showChildView('chart', new PieChartView({
            model: new Model({statistic}),
            showLegend: false
        }));
        this.showChildView('coverageToggle', new CoverageToggleView({}));
    }

    serializeData() {
        this.selectOptions = [{id: 'scenariosWithoutExamplesStatistic', translation: 'widget.summary.scenariosWithoutExamples'}, {id: 'scenariosStatistic', translation: 'widget.summary.scenarios'}, {id: 'storiesStatistic', translation: 'widget.summary.stories'}];
        this.currentOptionId = settings.getTotalResultSelectOption();
        this.isCoverageChecked = settings.isCoverageChecked();

        const testRuns = this.model.get('testRuns');
        const length = testRuns && testRuns.length;
        return Object.assign(super.serializeData(), {
            isAggregated: length > 1,
            launchesCount: length,
            currentOption: findWhere(this.selectOptions, {id: this.currentOptionId}),
            statistic: this.getStatistic(),
            isCoverageChecked: this.isCoverageChecked
        });
    }

    @on('click .widget__option-small')
    onOptionClick(e) {
        if(this.optSelectView.isVisible()) {
            this.optSelectView.hide();
        } else {
            this.optSelectView.show(this.$(e.currentTarget));
        }
    }

    getStatistic() {
        var actualStatistic;
        if (this.isCoverageChecked && this.currentOptionId !== 'storiesStatistic') {
            actualStatistic = new Object();
            var currentStatistic = this.model.get(this.currentOptionId);
            actualStatistic['covered'] = currentStatistic.total - currentStatistic.notcovered;
            actualStatistic['notcovered'] = currentStatistic.notcovered;
            actualStatistic['total'] = currentStatistic.total;
        } else {
            actualStatistic = JSON.parse(JSON.stringify(this.model.get(this.currentOptionId)));
            actualStatistic.total -= actualStatistic.notcovered;
            delete actualStatistic['notcovered'];
        }
        return actualStatistic;
    }
}

export default SummaryWidgetView;