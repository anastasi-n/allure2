package io.qameta.allure.history;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.context.JacksonContext;
import io.qameta.allure.core.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;

/**
 * 
 * File system trend manager.
 *
 * @param <T> type of items.
 */
public abstract class AbstractFileSystemTrendManager<T extends Serializable> implements ITrendManager<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileSystemTrendManager.class);

    private Path historyDirectory;

    @Override
    public List<T> load(final Configuration configuration) throws IoTrendException {
        final Path trendFile = historyDirectory.resolve(getTrendFileName());
        if (trendFile.toFile().exists()) {
            try (InputStream is = Files.newInputStream(trendFile)) {
                final ObjectMapper mapper = configuration.requireContext(JacksonContext.class).getValue();
                final JsonNode jsonNode = mapper.readTree(is);
                return getStream(jsonNode).map(child -> parseItemSafely(trendFile, mapper, child))
                        .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());

            } catch (IOException e) {
                throw new IoTrendException("Could not read trend file " + trendFile, e);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void save(final Configuration configuration, final List<T> trendItems) throws IoTrendException {
        try (OutputStream os = Files
                .newOutputStream(Files.createDirectories(historyDirectory).resolve(getTrendFileName()))) {
            configuration.requireContext(JacksonContext.class).getValue().writeValue(os, trendItems);
        } catch (IOException e) {
            throw new IoTrendException(e);
        }
    }

    private Stream<JsonNode> getStream(final JsonNode jsonNode) {
        return stream(spliteratorUnknownSize(jsonNode.elements(), Spliterator.ORDERED), false);
    }

    private Optional<T> parseItemSafely(final Path trendFile, final ObjectMapper mapper,
                                        final JsonNode child) {
        try {
            return Optional.ofNullable(parseItem(trendFile, mapper, child));
        } catch (JsonProcessingException e) {
            LOGGER.warn("Could not read {}", trendFile, e);
            return Optional.empty();
        }
    }

    protected abstract T parseItem(Path trendFile, ObjectMapper mapper, JsonNode child)
            throws JsonProcessingException;

    protected abstract String getTrendFileName();

    public void setHistoryDirectory(final Path historyDirectory) {
        this.historyDirectory = historyDirectory;
    }
}
