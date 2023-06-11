package com.startup.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.RestaurantMapMarker;
import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.SearchRepository;
import com.startup.go4lunch.repository.WorkmateRepository;
import com.startup.go4lunch.ui.MapFragmentViewModel;
import com.startup.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MapFragmentViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private SearchRepository searchRepository;

    @Mock
    private WorkmateRepository workmateRepository;

    private MapFragmentViewModel mapFragmentViewModel;

    final Location FAKE_LOCATION = getFakeLocation(45.84846468468, 3.564449894894);


    @Before
    public void setUp() {
        when(locationRepository.getLocationLiveData()).thenReturn(new MutableLiveData<>(FAKE_LOCATION));
        when(restaurantRepository.getRestaurantListLiveData()).thenReturn(new MutableLiveData<>(FAKE_RESTAURANTS_LIST));
        when(searchRepository.getMapFragmentSearchLivedata()).thenReturn(new MutableLiveData<>(null));
        when(workmateRepository.getWorkmateListLiveData()).thenReturn(new MutableLiveData<>(FAKE_WORKMATE_LIST));

        mapFragmentViewModel = new MapFragmentViewModel(locationRepository, restaurantRepository, searchRepository, workmateRepository);
    }

    @Test
    public void getMapCenterLocationLiveDataTest() throws InterruptedException {
        // Given
        @SuppressWarnings("redundant") final Location expectedLocation = FAKE_LOCATION;

        // When
        LiveData<Location> actualLocationLiveData = mapFragmentViewModel.getMapCenterLocationLiveData();

        // Then
        Location actualLocation = LiveDataTestUtils.getValue(actualLocationLiveData);
        assertEquals(expectedLocation.getLatitude(), actualLocation.getLatitude(), 0.00000000000001);
        assertEquals(expectedLocation.getLongitude(), actualLocation.getLongitude(), 0.00000000000001);
    }

    @Test
    public void getRestaurantMapMarkerListLiveDataTest() throws InterruptedException {
        // Given
        final List<Restaurant> expectedRestaurantList = FAKE_RESTAURANTS_LIST;
        @SuppressWarnings("redundant") final List<Workmate> expectedWorkmateList = FAKE_WORKMATE_LIST;

        // When
        LiveData<List<RestaurantMapMarker>> actualRestaurantMapMarkerListLiveData = mapFragmentViewModel.getRestaurantMapMarkerListLiveData();

        // Then
        List<RestaurantMapMarker> actualRestaurantMapMarkerList = LiveDataTestUtils.getValue(actualRestaurantMapMarkerListLiveData);
        for (int i = 0; i < expectedRestaurantList.size(); i++) {
            final Restaurant expectedRestaurant = expectedRestaurantList.get(i);
            final RestaurantMapMarker actualRestaurantMapMarker = actualRestaurantMapMarkerList.get(i);

            assertEquals(expectedRestaurant, actualRestaurantMapMarker.getRestaurant());

            boolean expectedIsRestaurantSelectedByAnyoneForLunch = false;
            for (Workmate workmate : expectedWorkmateList) {
                if (workmate.getRestaurantSelectedUid() != null && workmate.getRestaurantSelectedUid() == expectedRestaurant.getId()) {
                    expectedIsRestaurantSelectedByAnyoneForLunch = true;
                    break;
                }
            }
            assertEquals(expectedIsRestaurantSelectedByAnyoneForLunch, actualRestaurantMapMarker.getWorkmateLunchOnRestaurant());
        }
    }

    @Test
    public void updateLocation() {
        // Given
        double expectedLatitude = 48.85834269238794;
        double expectedLongitude = 2.294392951104722;
        Location expectedLocation = getFakeLocation(expectedLatitude, expectedLongitude);

        // When
        mapFragmentViewModel.updateLocation(expectedLocation);

        // Then
        verify(locationRepository).setLocation(expectedLocation);
        verify(restaurantRepository).updateLocationRestaurantList(expectedLocation);
    }


    private static final List<Restaurant> FAKE_RESTAURANTS_LIST = Arrays.asList(
            new Restaurant(0L, "Restaurant 0", "type 0", 1000, 2000, "1 chemin de la Paix, 75002 Paris", "0h00", "0600000000", "http://website0.fr"),
            new Restaurant(1L, "Restaurant 1 SearchTest", "type 1", 1111, 2111, "1 rue de la Paix, 75002 Paris", "1h00", "0600000001", "http://website1.fr"),
            new Restaurant(2L, "Restaurant 2", "type 2", 1222, 2222, "2 rue de la Paix, 75002 Paris", "2h00", "0600000002", "http://website2.com"),
            new Restaurant(3L, "Restaurant 3", "type 3", 1333, 2333, "3 rue de la Paix, 75002 Paris", "3h00", "0600000003", "http://website3.com"));

    private static final List<Workmate> FAKE_WORKMATE_LIST = Arrays.asList(
            new Workmate("ec4e8cr", "Didier Durant", "http:...", 0L),
            new Workmate("ec4hrthtr", "Olivier Martin", "http:...", 3L),
            new Workmate("tyhgreyhryh", "Emilie Dupont", "http:...", 2L),
            new Workmate("rtherhhre", "Frederique Leblanc", "http:...", 2L));

    @NonNull
    private Location getFakeLocation(double expectedLatitude, double expectedLongitude) {
        final Location expectedLocation = Mockito.spy(new Location("Expected Location"));
        doReturn(expectedLatitude).when(expectedLocation).getLatitude();
        doReturn(expectedLongitude).when(expectedLocation).getLongitude();
        return expectedLocation;
    }
}
