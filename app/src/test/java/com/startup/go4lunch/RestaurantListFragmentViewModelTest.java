package com.startup.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.ui.RestaurantListFragmentViewModel;
import com.startup.go4lunch.utils.LiveDataTestUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantListFragmentViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private RestaurantListFragmentViewModel restaurantListFragmentViewModel;

    @Test
    public void getRestaurantListLiveData() throws InterruptedException {
        // Given
        List<Restaurant>restaurantListExpected = FAKE_RESTAURANTS_LIST;
        MutableLiveData<List<Restaurant>> restaurantListLiveData = new MutableLiveData<>(restaurantListExpected);
        when(restaurantRepository.getRestaurantListLiveData()).thenReturn(restaurantListLiveData);

        // When
        LiveData<List<Restaurant>> result = restaurantListFragmentViewModel.getRestaurantListLiveData();

        // Then
        List<Restaurant> actualRestaurantList = LiveDataTestUtils.getValue(result);
        assertEquals(restaurantListExpected, actualRestaurantList);
    }

    private static final List<Restaurant> FAKE_RESTAURANTS_LIST = Arrays.asList(
            new Restaurant(0L, "Restaurant 0", "type 0", 1000, 2000, "1 chemin de la Paix, 75002 Paris", "0h00"),
            new Restaurant(1L, "Restaurant 1", "type 1", 1111, 2111, "1 rue de la Paix, 75002 Paris", "1h00"),
            new Restaurant(2L, "Restaurant 2", "type 2", 1222, 2222, "2 rue de la Paix, 75002 Paris", "2h00"),
            new Restaurant(3L, "Restaurant 3", "type 3", 1333, 2333, "3 rue de la Paix, 75002 Paris", "3h00"),
            new Restaurant(4L, "Restaurant 4", "type 4", 1444, 2444, "4 rue de la Paix, 75002 Paris", "4h00"),
            new Restaurant(5L, "Restaurant 5", "type 5", 1555, 2555, "5 rue de la Paix, 75002 Paris", "5h00"));
}