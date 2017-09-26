package io.qameta.allure.timeline;

import io.qameta.allure.CommonJsonAggregator;
import io.qameta.allure.CompositeAggregator;
import io.qameta.allure.core.LaunchResults;
import io.qameta.allure.entity.LabelName;
import io.qameta.allure.entity.TestResult;
import io.qameta.allure.tree.TestResultTree;
import io.qameta.allure.tree.Tree;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static io.qameta.allure.tree.TreeUtils.groupByLabels;

/**
 * Plugin that generates data for Timeline tab.
 *
 * @since 2.0
 */
@SuppressWarnings("PMD.UseUtilityClass")
public class TimelinePlugin extends CompositeAggregator {

    protected static final String JSON_FILE_NAME = "timeline.json";

    public TimelinePlugin() {
        super(Arrays.asList(new JsonAggregator(), new WidgetAggregator()));
    }

    protected static Tree<TestResult> getData(final List<LaunchResults> launchResults) {
        // @formatter:off
        final Tree<TestResult> timeline = new TestResultTree(
            "timeline",
            testResult -> groupByLabels(testResult, LabelName.HOST, LabelName.THREAD)
        );
        // @formatter:on

        launchResults.stream()
                .map(LaunchResults::getResults)
                .flatMap(Collection::stream)
                .forEach(timeline::add);
        return timeline;
    }

    /**
     * Aggregator for generation of json.
     */
    private static class JsonAggregator extends CommonJsonAggregator {

        JsonAggregator() {
            super(JSON_FILE_NAME);
        }

        @Override
        protected Tree<TestResult> getData(final List<LaunchResults> launches) {
            return TimelinePlugin.getData(launches);
        }
    }

    /**
     * Aggregator for generation widget data.
     */
    private static class WidgetAggregator extends CommonJsonAggregator {

        WidgetAggregator() {
            super("widgets", JSON_FILE_NAME);
        }

        @Override
        protected Object getData(final List<LaunchResults> launches) {
            return TimelinePlugin.getData(launches);
        }
    }
}
