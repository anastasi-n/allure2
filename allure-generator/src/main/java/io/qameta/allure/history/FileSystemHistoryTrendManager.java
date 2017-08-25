package io.qameta.allure.history;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.entity.Statistic;

import java.nio.file.Path;
import java.util.Objects;

/**
 * File system trend manager for history.
 */
public class FileSystemHistoryTrendManager extends AbstractFileSystemTrendManager<HistoryTrendItem> {

    @Override
    protected HistoryTrendItem parseItem(final Path trendFile, final ObjectMapper mapper, final JsonNode child)
            throws JsonProcessingException {
        if (Objects.nonNull(child.get("total"))) {
            final Statistic statistic = mapper.treeToValue(child, Statistic.class);
            return new HistoryTrendItem().setStatistic(statistic);
        }
        return mapper.treeToValue(child, HistoryTrendItem.class);
    }

    @Override
    protected String getTrendFileName() {
        return "history-trend.json";
    }
}
