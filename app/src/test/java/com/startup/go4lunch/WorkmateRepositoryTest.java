package com.startup.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.startup.go4lunch.api.WorkmateApi;
import com.startup.go4lunch.model.RestaurantWorkmateVote;
import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.repository.WorkmateRepository;
import com.startup.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WorkmateRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private WorkmateApi workmateApi;
    private WorkmateRepository workmateRepository;

    @Before
    public void setUp() {
        workmateApi = mock(WorkmateApi.class);
        when(workmateApi.getWorkmateListLiveData()).thenReturn(new MutableLiveData<>(FAKE_WORKMATE_LIST));
        when(workmateApi.getRestaurantWorkmateVoteListLiveData()).thenReturn(new MutableLiveData<>(FAKE_RESTAURANT_WORKMATE_VOTE_LIST));
        workmateRepository = new WorkmateRepository(workmateApi);
    }


    @Test
    public void getWorkmateListLiveDataTest() throws InterruptedException {
        // Given
        @SuppressWarnings("redundant")
        List<Workmate> expectedWorkmateList = FAKE_WORKMATE_LIST;

        // When
        LiveData<List<Workmate>> result = workmateRepository.getWorkmateListLiveData();

        // Then
        List<Workmate> actualWorkmateList = LiveDataTestUtils.getValue(result);
        assertEquals(expectedWorkmateList, actualWorkmateList);
    }

    @Test
    public void getRestaurantWorkmateVoteListLiveData() throws InterruptedException {
        // Given
        @SuppressWarnings("redundant")
        List<RestaurantWorkmateVote> expectedRestaurantWorkmateVoteList = FAKE_RESTAURANT_WORKMATE_VOTE_LIST;

        // When
        LiveData<List<RestaurantWorkmateVote>> result = workmateRepository.getRestaurantWorkmateVoteListLiveData();

        // Then
        List<RestaurantWorkmateVote> actualRestaurantWorkmateVoteList = LiveDataTestUtils.getValue(result);
        assertEquals(expectedRestaurantWorkmateVoteList, actualRestaurantWorkmateVoteList);
    }

    @Test
    public void createWorkmateTest() {
        // Given
        Workmate expectedWorkmate = new Workmate("hfjfjgjj", "Expected Workmate", "http:...test", 4L);

        // When
        workmateRepository.createWorkmate(expectedWorkmate);

        // Then
        verify(workmateApi).createWorkmate(expectedWorkmate);
    }

    @Test
    public void getWorkmateFromUidTest() {
        // Given
        Workmate expectedWorkmate = FAKE_WORKMATE_LIST.get(1);

        // When
        Workmate actualWorkmate = workmateRepository.getWorkmateFromUid(expectedWorkmate.getUid());

        // Then
        assertEquals(expectedWorkmate, actualWorkmate);
    }

    @Test
    public void setRestaurantSelectedToWorkmateTest() {
        // Given
        final String expectedWorkmateUid = "fcffsvfv";
        final Long expectedRestaurantSelectedUid = 12345L;

        // When
        workmateRepository.setRestaurantSelectedToWorkmate(expectedWorkmateUid, expectedRestaurantSelectedUid);

        // Then
        verify(workmateApi).setWorkmateRestaurantSelectedUid(expectedWorkmateUid, expectedRestaurantSelectedUid);
    }

    @Test
    public void createRestaurantWorkmateVoteTest() {
        // Given
        final String expectedWorkmateUid = "dfhhdgfd";
        final long expectedRestaurantSelectedUid = 54321L;

        // When
        workmateRepository.createRestaurantWorkmateVote(expectedWorkmateUid, expectedRestaurantSelectedUid);

        // Then
        ArgumentCaptor<RestaurantWorkmateVote> captor = ArgumentCaptor.forClass(RestaurantWorkmateVote.class);
        verify(workmateApi).createRestaurantWorkmateVote(captor.capture());
        RestaurantWorkmateVote actualRestaurantWorkmateVote = captor.getValue();

        assertEquals(expectedWorkmateUid, actualRestaurantWorkmateVote.getWorkmateUid());
        assertEquals(expectedRestaurantSelectedUid, actualRestaurantWorkmateVote.getRestaurantUid());
    }

    @Test
    public void createRestaurantWorkmateVoteTest_WhenRestaurantWorkmateVoteExist() {
        // Given
        final RestaurantWorkmateVote expectedRestaurantWorkmateVote = FAKE_RESTAURANT_WORKMATE_VOTE_LIST.get(1);
        final String expectedWorkmateUid = expectedRestaurantWorkmateVote.getWorkmateUid();
        final long expectedRestaurantSelectedUid = expectedRestaurantWorkmateVote.getRestaurantUid();

        // When
        workmateRepository.createRestaurantWorkmateVote(expectedWorkmateUid, expectedRestaurantSelectedUid);

        // Then
        verify(workmateApi, Mockito.never()).createRestaurantWorkmateVote(Mockito.any(RestaurantWorkmateVote.class));
    }

    @Test
    public void removeRestaurantWorkmateVoteTest() {
        // Given
        final RestaurantWorkmateVote expectedRestaurantWorkmateVote = FAKE_RESTAURANT_WORKMATE_VOTE_LIST.get(1);
        final String expectedWorkmateUid = expectedRestaurantWorkmateVote.getWorkmateUid();
        final long expectedRestaurantSelectedUid = expectedRestaurantWorkmateVote.getRestaurantUid();

        // When
        workmateRepository.removeRestaurantWorkmateVote(expectedWorkmateUid, expectedRestaurantSelectedUid);

        // Then
        ArgumentCaptor<RestaurantWorkmateVote> captor = ArgumentCaptor.forClass(RestaurantWorkmateVote.class);
        verify(workmateApi).removeRestaurantWorkmateVote(captor.capture());
        RestaurantWorkmateVote actualRestaurantWorkmateVote = captor.getValue();

        assertEquals(expectedWorkmateUid, actualRestaurantWorkmateVote.getWorkmateUid());
        assertEquals(expectedRestaurantSelectedUid, actualRestaurantWorkmateVote.getRestaurantUid());
    }

    @Test
    public void getRestaurantWorkmateVoteTest() {
        // Given
        final RestaurantWorkmateVote expectedRestaurantWorkmateVote = FAKE_RESTAURANT_WORKMATE_VOTE_LIST.get(1);
        final String expectedWorkmateUid = expectedRestaurantWorkmateVote.getWorkmateUid();
        final long expectedRestaurantSelectedUid = expectedRestaurantWorkmateVote.getRestaurantUid();

        // When
        boolean actualResult = workmateRepository.getRestaurantWorkmateVote(expectedWorkmateUid, expectedRestaurantSelectedUid);

        // Then
        assertTrue(actualResult);
    }

    @Test
    public void getRestaurantWorkmateVoteTest_WhenRestaurantWorkmateVoteNotExist() {
        // Given
        final String expectedWorkmateUid = "vrgvrrvrv";
        final long expectedRestaurantSelectedUid = 516845641L;

        // When
        boolean actualResult = workmateRepository.getRestaurantWorkmateVote(expectedWorkmateUid, expectedRestaurantSelectedUid);

        // Then
        assertFalse(actualResult);
    }

    private static final List<Workmate> FAKE_WORKMATE_LIST = Arrays.asList(
            new Workmate("ec4e8cr", "Didier Durant", "http:...", 0L),
            new Workmate("ec4hrthtr", "Olivier Martin SearchString", "http:...", 3L),
            new Workmate("tyhgreyhryh", "Emilie Dupont", "http:...", 2L),
            new Workmate("rtherhhre", "Frederique Leblanc SearchString", "http:...", 2L));

    private static final List<RestaurantWorkmateVote> FAKE_RESTAURANT_WORKMATE_VOTE_LIST = Arrays.asList(
            new RestaurantWorkmateVote("ec4e8cr",2L),
            new RestaurantWorkmateVote("ec4hrthtr",3L),
            new RestaurantWorkmateVote("tyhgreyhryh",0L),
            new RestaurantWorkmateVote("ec4hrthtr",10L));
}
