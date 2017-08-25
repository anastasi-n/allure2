package io.qameta.allure.duration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.qameta.allure.history.AbstractFileSystemTrendManager;

import java.nio.file.Path;

/**
 * File system trend manager for duration.
 */
public class FileSystemDurationTrendManager extends AbstractFileSystemTrendManager<DurationTrendItem> {

    @Override
    protected DurationTrendItem parseItem(final Path trendFile, final ObjectMapper mapper, final JsonNode child)
            throws JsonProcessingException {
        return mapper.treeToValue(child, DurationTrendItem.class);
    }

    @Override
    protected String getTrendFileName() {
        return "duration-trend.json";
    }
}
