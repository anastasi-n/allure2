package io.qameta.allure.duration;

import io.qameta.allure.context.JacksonContext;
import io.qameta.allure.core.Configuration;
import io.qameta.allure.core.ResultsVisitor;
import io.qameta.allure.entity.Status;
import io.qameta.allure.entity.Time;
import io.qameta.allure.history.ITrendManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static io.qameta.allure.testdata.TestData.createSingleLaunchResults;
import static io.qameta.allure.testdata.TestData.randomTestResult;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DurationTrendPluginTest {

    private static final String TIME_TREND_BLOCK_NAME = "duration-trend";

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock
    private ITrendManager<DurationTrendItem> durationTrendManager;

    @Test
    public void shouldReadData() throws Exception {
        final Configuration configuration = mock(Configuration.class);
        final ResultsVisitor visitor = mock(ResultsVisitor.class);

        List<DurationTrendItem> groupTimes = Collections.singletonList(new DurationTrendItem());
        when(durationTrendManager.load(configuration)).thenReturn(groupTimes);
        final DurationTrendPlugin plugin = new DurationTrendPlugin(durationTrendManager);
        plugin.readResults(configuration, visitor, mock(Path.class));

        verify(visitor, times(1)).visitExtra(TIME_TREND_BLOCK_NAME, groupTimes);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldAggregateForEmptyReport() throws Exception {
        final Path outputDirectory = temporaryFolder.newFolder().toPath();
        final Configuration configuration = mock(Configuration.class);
        final JacksonContext context = mock(JacksonContext.class);
        final ObjectMapper mapper = mock(ObjectMapper.class);

        when(configuration.requireContext(JacksonContext.class)).thenReturn(context);
        when(context.getValue()).thenReturn(mapper);

        final DurationTrendPlugin plugin = new DurationTrendPlugin(durationTrendManager);
        plugin.aggregate(configuration, Collections.emptyList(), outputDirectory);

        final ArgumentCaptor<List<DurationTrendItem>> captor = ArgumentCaptor.forClass(List.class);
        verify(durationTrendManager, times(1)).save(eq(configuration), captor.capture());
        verify(mapper, times(1)).writeValue(Mockito.any(OutputStream.class), captor.capture());
        assertThat(captor.getValue()).hasSize(1);
    }

    @Test
    public void shouldGetData() throws Exception {
        final List<DurationTrendItem> history = Collections.singletonList(new DurationTrendItem());
        final List<DurationTrendItem> data = new DurationTrendPlugin(durationTrendManager).getData(createSingleLaunchResults(
                singletonMap(TIME_TREND_BLOCK_NAME, history),
                randomTestResult().setStatus(Status.PASSED).setTime(Time.create(1L, 2L)),
                randomTestResult().setStatus(Status.FAILED).setTime(Time.create(2L, 3L)),
                randomTestResult().setStatus(Status.FAILED).setTime(Time.create(3L, 4L))
        ));

       /* assertThat(data)
                .hasSize(1 + history.size())
                .extracting(GroupTime::getDuration)
                .first()
                .isEqualTo(3L);*/

        final List<DurationTrendItem> next = data.subList(1, data.size());

        assertThat(next)
                .containsExactlyElementsOf(history);
    }
}
