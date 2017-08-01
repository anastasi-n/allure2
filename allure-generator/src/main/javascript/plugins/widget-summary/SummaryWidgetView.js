import './styles.scss';
import {View} from 'backbone.marionette';
import {Model} from 'backbone';
import {findWhere} from 'underscore';
import template from './SummaryWidgetView.hbs';
import {regions, on} from '../../decorators';
import PieChartView from '../../components/graph-pie-chart/PieChartView';
import TotalResultSelectView from './select-view/TotalResultSelectView';
import settings from '../../utils/settings';

@regions({
    chart: '.summary-widget__chart'
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
        var statistic = this.model.get(this.currentOptionId);
        this.showChildView('chart', new PieChartView({
            model: new Model({statistic}),
            showLegend: false
        }));
    }

    serializeData() {
        this.selectOptions = [{id: 'scenariosWithoutExamplesStatistic', translation: 'widget.summary.scenariosWithoutExamples'}, {id: 'scenariosStatistic', translation: 'widget.summary.scenarios'}, {id: 'storiesStatistic', translation: 'widget.summary.stories'}];
        this.currentOptionId = settings.getTotalResultSelectOption();

        const testRuns = this.model.get('testRuns');
        const length = testRuns && testRuns.length;
        return Object.assign(super.serializeData(), {
            isAggregated: length > 1,
            launchesCount: length,
            currentOption: findWhere(this.selectOptions, {id: this.currentOptionId}),
            statistic: this.model.get(this.currentOptionId)
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
}

export default SummaryWidgetView;