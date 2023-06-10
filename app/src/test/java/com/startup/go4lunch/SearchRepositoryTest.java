package com.startup.go4lunch;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.startup.go4lunch.repository.SearchRepository;
import com.startup.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SearchRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private SearchRepository searchRepository;


    @Before
    public void setUp() {
        searchRepository = new SearchRepository();
    }

    @Test
    public void setMapFragmentSearchAndGetMapFragmentSearchLiveData() throws InterruptedException {
        // GIVEN
        String expectedString = "MapFragment Search String Test";

        // WHEN
        searchRepository.setMapFragmentSearch(expectedString);

        // THEN
        String actualString = LiveDataTestUtils.getValue(searchRepository.getMapFragmentSearchLivedata());
        assertEquals(expectedString, actualString);
    }

    @Test
    public void setRestaurantListSearchAndGetRestaurantListFragmentSearchLiveData() throws InterruptedException {
        // GIVEN
        String expectedString = "RestaurantListFragment Search String Test";

        // WHEN
        searchRepository.setRestaurantListSearch(expectedString);

        // THEN
        String actualString = LiveDataTestUtils.getValue(searchRepository.getRestaurantListFragmentSearchLiveData());
        assertEquals(expectedString, actualString);
    }

    @Test
    public void setWorkmateListSearchAndGetWorkmateListFragmentLiveData() throws InterruptedException {
        // GIVEN
        String expectedString = "WorkmateListFragment Search String Test";

        // WHEN
        searchRepository.setWorkmateListSearch(expectedString);

        // THEN
        String actualString = LiveDataTestUtils.getValue(searchRepository.getWorkmateListFragmentSearchLiveData());
        assertEquals(expectedString, actualString);
    }
}
