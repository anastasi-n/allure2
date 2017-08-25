package io.qameta.allure.history;

import io.qameta.allure.CommonJsonAggregator;
import io.qameta.allure.Constants;
import io.qameta.allure.Reader;
import io.qameta.allure.core.Configuration;
import io.qameta.allure.core.LaunchResults;
import io.qameta.allure.core.ResultsVisitor;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * Common part for trend plugins.
 *
 * @param <T> data type.
 */
public abstract class AbstractTrendPlugin<T extends Serializable> extends CommonJsonAggregator implements Reader {

    private final ITrendManager<T> trendManager;
    private final String name;

    protected AbstractTrendPlugin(final ITrendManager<T> trendManager, final String name) {
        super(Constants.WIDGETS_DIR, name + ".json");
        this.trendManager = trendManager;
        this.name = name;
    }

    @Override
    public void readResults(final Configuration configuration, final ResultsVisitor visitor, final Path directory) {
        try {
            final List<T> history = trendManager.load(configuration);
            if (!history.isEmpty()) {
                visitor.visitExtra(name, history);
            }
        } catch (IOException e) {
            visitor.error(e.getMessage(), e);
        }
    }

    @Override
    public void aggregate(final Configuration configuration, final List<LaunchResults> launchesResults,
                          final Path outputDirectory) throws IOException {
        super.aggregate(configuration, launchesResults, outputDirectory);
        trendManager.save(configuration, getTrendData(launchesResults));
    }

    @Override
    public List<T> getData(final List<LaunchResults> launches) {
        return getTrendData(launches);
    }

    private List<T> getTrendData(final List<LaunchResults> launchesResults) {
        final T item = createCurrent(launchesResults);
        final List<T> data = getHistoryItems(launchesResults);

        return Stream.concat(Stream.of(item), data.stream())
                .limit(20)
                .collect(Collectors.toList());
    }

    private List<T> getHistoryItems(final List<LaunchResults> launchesResults) {
        return launchesResults.stream()
                .map(this::getPreviousTrendData)
                .reduce(new ArrayList<>(), (first, second) -> {
                    first.addAll(second);
                    return first;
                });
    }

    private List<T> getPreviousTrendData(final LaunchResults results) {
        return results.getExtra(name, ArrayList::new);
    }

    protected abstract T createCurrent(List<LaunchResults> launchesResults);
}
