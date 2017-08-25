package io.qameta.allure.duration;

import io.qameta.allure.core.LaunchResults;
import io.qameta.allure.entity.ExecutorInfo;
import io.qameta.allure.history.AbstractTrendPlugin;
import io.qameta.allure.history.ITrendManager;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static io.qameta.allure.executor.ExecutorPlugin.EXECUTORS_BLOCK_NAME;
import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * Plugin that generates data for Duration-Trend graph.
 */
public class DurationTrendPlugin extends AbstractTrendPlugin<DurationTrendItem> {

    public DurationTrendPlugin(final ITrendManager<DurationTrendItem> historyTrendManager) {
        super(historyTrendManager, "duration-trend");
    }

    @Override
    protected DurationTrendItem createCurrent(final List<LaunchResults> launchesResults) {
        final DurationTrendItem item = new DurationTrendItem();
        extractLatestExecutor(launchesResults).ifPresent(info -> {
            item.setBuildOrder(info.getBuildOrder());
            item.setReportName(info.getReportName());
            item.setReportUrl(info.getReportUrl());
        });
        launchesResults.stream()
                .flatMap(launch -> launch.getResults().stream())
                .forEach(item::updateTime);
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
