
package io.qameta.allure.history;

import io.qameta.allure.core.LaunchResults;
import io.qameta.allure.entity.ExecutorInfo;
import io.qameta.allure.entity.Statistic;
import io.qameta.allure.entity.TestResult;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static io.qameta.allure.executor.ExecutorPlugin.EXECUTORS_BLOCK_NAME;
import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * Plugin that adds history trend widget.
 *
 * @since 2.0
 */
public class HistoryTrendPlugin extends AbstractTrendPlugin<HistoryTrendItem> {

    public HistoryTrendPlugin(final ITrendManager<HistoryTrendItem> historyTrendManager) {
        super(historyTrendManager, "history-trend");
    }

    @Override
    protected HistoryTrendItem createCurrent(final List<LaunchResults> launchesResults) {
        final Statistic statistic = launchesResults.stream()
                .flatMap(results -> results.getResults().stream())
                .map(TestResult::getStatus)
                .collect(Statistic::new, Statistic::update, Statistic::merge);
        final HistoryTrendItem item = new HistoryTrendItem()
                .setStatistic(statistic);
        extractLatestExecutor(launchesResults).ifPresent(info -> {
            item.setBuildOrder(info.getBuildOrder());
            item.setReportName(info.getReportName());
            item.setReportUrl(info.getReportUrl());
        });
        return item;
    }

    private static Optional<ExecutorInfo> extractLatestExecutor(final List<LaunchResults> launches) {
        final Comparator<ExecutorInfo> comparator = comparing(ExecutorInfo::getBuildOrder, nullsFirst(naturalOrder()));
        return launches.stream()
                .map(launch -> launch.getExtra(EXECUTORS_BLOCK_NAME))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(ExecutorInfo.class::isInstance)
                .map(ExecutorInfo.class::cast)
                .max(comparator);
    }
}
