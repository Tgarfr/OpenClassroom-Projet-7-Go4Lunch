package com.startup.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.RestaurantListItem;
import com.startup.go4lunch.model.RestaurantWorkmateVote;
import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.SearchRepository;
import com.startup.go4lunch.repository.WorkmateRepository;
import com.startup.go4lunch.ui.RestaurantListFragmentViewModel;
import com.startup.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantListFragmentViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private WorkmateRepository workmateRepository;

    @Mock
    private SearchRepository searchRepository;

    private RestaurantListFragmentViewModel restaurantListFragmentViewModel;

    private final Location FAKE_LOCATION = new Location("Fake Location");


    @Before
    public void setUp() {
        FAKE_LOCATION.setLatitude(48.85834269238794);
        FAKE_LOCATION.setLongitude(2.294392951104722);

        when(restaurantRepository.getRestaurantListLiveData()).thenReturn(new MutableLiveData<>(FAKE_RESTAURANTS_LIST));
        when(locationRepository.getLocationLiveData()).thenReturn(new MutableLiveData<>(FAKE_LOCATION));
        when(workmateRepository.getWorkmateListLiveData()).thenReturn(new MutableLiveData<>(FAKE_WORKMATE_LIST));
        when(workmateRepository.getRestaurantWorkmateVoteListLiveData()).thenReturn(new MutableLiveData<>(FAKE_RESTAURANT_WORKMATE_VOTE_LIST));
        when(searchRepository.getRestaurantListFragmentSearchLiveData()).thenReturn(new MutableLiveData<>(null));

        restaurantListFragmentViewModel = new RestaurantListFragmentViewModel(restaurantRepository, searchRepository, locationRepository, workmateRepository);
    }

    @Test
    public void getRestaurantListItemListLiveDataTest() throws InterruptedException {
        // Given

        // When
        LiveData<List<RestaurantListItem>> result = restaurantListFragmentViewModel.getRestaurantListItemListLiveData();

        // Then
        List<RestaurantListItem> restaurantListItemListResult = LiveDataTestUtils.getValue(result);
        if (restaurantListItemListResult == null) {
            fail("restaurantListItemListResult null");
        }
        for (int i = 0; i < restaurantListItemListResult.size(); i++) {
            Restaurant restaurant = restaurantListItemListResult.get(i).getRestaurant();

            assertEquals(restaurant, FAKE_RESTAURANTS_LIST.get(i));

            Location restaurantLocation = new Location(restaurant.getName());
            restaurantLocation.setLatitude(restaurant.getLatitude());
            restaurantLocation.setLongitude(restaurant.getLongitude());
            assertEquals(restaurantLocation.distanceTo(FAKE_LOCATION), restaurantListItemListResult.get(i).getDistance(), 1);

            int numberOfWorkmateLunchInRestaurant = 0;
            for (Workmate workmate : FAKE_WORKMATE_LIST) {
                if (workmate.getRestaurantSelectedUid() != null && workmate.getRestaurantSelectedUid() == restaurant.getId()) {
                    numberOfWorkmateLunchInRestaurant++;
                }
            }
            assertEquals(numberOfWorkmateLunchInRestaurant, restaurantListItemListResult.get(i).getNumberOfWorkmate());

            int restaurantScore = 0;
            for (RestaurantWorkmateVote restaurantWorkmateVote : FAKE_RESTAURANT_WORKMATE_VOTE_LIST) {
                if (restaurantWorkmateVote.getRestaurantUid() == restaurant.getId()) {
                    restaurantScore++;
                }
            }
            assertEquals(restaurantScore, restaurantListItemListResult.get(i).getScore());
        }
    }

    @Test
    public void sortRestaurantListItemListLiveData_sortByDistance() throws InterruptedException {
        // Given
        List<RestaurantListItem> restaurantListItemList =
                LiveDataTestUtils.getValue(restaurantListFragmentViewModel.getRestaurantListItemListLiveData());
        List<RestaurantListItem> expectedRestaurantListItemListSortByDistance =
                getRestaurantListItemListSorted(RestaurantListItem.SORT_BY_DISTANCE, new ArrayList<>(restaurantListItemList));

        // When
        restaurantListFragmentViewModel.sortRestaurantListItemListLiveData(RestaurantListItem.SORT_BY_DISTANCE);

        // Then
        List<RestaurantListItem> actualRestaurantListItemListSortByDistance =
                LiveDataTestUtils.getValue(restaurantListFragmentViewModel.getRestaurantListItemListLiveData());
        assertEquals(expectedRestaurantListItemListSortByDistance, actualRestaurantListItemListSortByDistance);
    }

    @Test
    public void sortRestaurantListItemListLiveData_sortByName() throws InterruptedException {
        // Given
        List<RestaurantListItem> restaurantListItemList =
                LiveDataTestUtils.getValue(restaurantListFragmentViewModel.getRestaurantListItemListLiveData());
        List<RestaurantListItem> expectedRestaurantListItemListSortByName =
                getRestaurantListItemListSorted(RestaurantListItem.SORT_BY_NAME, new ArrayList<>(restaurantListItemList));

        // When
        restaurantListFragmentViewModel.sortRestaurantListItemListLiveData(RestaurantListItem.SORT_BY_NAME);

        // Then
        List<RestaurantListItem> actualRestaurantListItemListSortByName =
                LiveDataTestUtils.getValue(restaurantListFragmentViewModel.getRestaurantListItemListLiveData());
        assertEquals(expectedRestaurantListItemListSortByName, actualRestaurantListItemListSortByName);
    }

    @Test
    public void sortRestaurantListItemListLiveData_sortByType() throws InterruptedException {
        // Given
        List<RestaurantListItem> restaurantListItemList =
                LiveDataTestUtils.getValue(restaurantListFragmentViewModel.getRestaurantListItemListLiveData());
        List<RestaurantListItem> expectedRestaurantListItemListSortByType =
                getRestaurantListItemListSorted(RestaurantListItem.SORT_BY_TYPE, new ArrayList<>(restaurantListItemList));

        // When
        restaurantListFragmentViewModel.sortRestaurantListItemListLiveData(RestaurantListItem.SORT_BY_TYPE);

        // Then
        List<RestaurantListItem> actualRestaurantListItemListSortByType =
                LiveDataTestUtils.getValue(restaurantListFragmentViewModel.getRestaurantListItemListLiveData());
        assertEquals(expectedRestaurantListItemListSortByType, actualRestaurantListItemListSortByType);
    }

    @Test
    public void sortRestaurantListItemListLiveData_sortByScore() throws InterruptedException {
        // Given
        List<RestaurantListItem> restaurantListItemList =
                LiveDataTestUtils.getValue(restaurantListFragmentViewModel.getRestaurantListItemListLiveData());
        List<RestaurantListItem> expectedRestaurantListItemListSortByScore =
                getRestaurantListItemListSorted(RestaurantListItem.SORT_BY_SCORE, new ArrayList<>(restaurantListItemList));

        // When
        restaurantListFragmentViewModel.sortRestaurantListItemListLiveData(RestaurantListItem.SORT_BY_SCORE);

        // Then
        List<RestaurantListItem> actualRestaurantListItemListSortByScore =
                LiveDataTestUtils.getValue(restaurantListFragmentViewModel.getRestaurantListItemListLiveData());
        assertEquals(expectedRestaurantListItemListSortByScore, actualRestaurantListItemListSortByScore);
    }

    private static final List<Restaurant> FAKE_RESTAURANTS_LIST = Arrays.asList(
            new Restaurant(0L, "Restaurant 0", "type 0", 1000, 2000, "1 chemin de la Paix, 75002 Paris", "0h00","0600000000","http://website0.fr"),
            new Restaurant(1L, "Restaurant 1", "type 1", 1111, 2111, "1 rue de la Paix, 75002 Paris", "1h00","0600000001","http://website1.fr"),
            new Restaurant(2L, "Restaurant 2", "type 2", 1222, 2222, "2 rue de la Paix, 75002 Paris", "2h00","0600000002","http://website2.com"),
            new Restaurant(3L, "Restaurant 3", "type 3", 1333, 2333, "3 rue de la Paix, 75002 Paris", "3h00","0600000003","http://website3.com"));

    private static final List<Workmate> FAKE_WORKMATE_LIST = Arrays.asList(
            new Workmate("ec4e8cr", "Didier Durant", "http:...", 0L),
            new Workmate("ec4hrthtr", "Olivier Martin", "http:...", 3L),
            new Workmate("tyhgreyhryh", "Emilie Dupont", "http:...", 2L),
            new Workmate("rtherhhre", "Frederique Leblanc", "http:...", 2L));

    private static final List<RestaurantWorkmateVote> FAKE_RESTAURANT_WORKMATE_VOTE_LIST = Arrays.asList(
            new RestaurantWorkmateVote("ec4e8cr",2L),
            new RestaurantWorkmateVote("ec4hrthtr",3L));

    @NonNull
    private List<RestaurantListItem> getRestaurantListItemListSorted(int sortMethod, @NonNull List<RestaurantListItem> restaurantListItemList) {
        switch (sortMethod) {
            case RestaurantListItem.SORT_BY_NAME:
                Collections.sort(restaurantListItemList, new RestaurantListItem.RestaurantListItemNameComparator());
                break;
            case RestaurantListItem.SORT_BY_DISTANCE:
                Collections.sort(restaurantListItemList, new RestaurantListItem.RestaurantListItemDistanceComparator());
                break;
            case RestaurantListItem.SORT_BY_TYPE:
                Collections.sort(restaurantListItemList, new RestaurantListItem.RestaurantListItemTypeComparator());
                break;
            case RestaurantListItem.SORT_BY_SCORE:
                Collections.sort(restaurantListItemList, new RestaurantListItem.RestaurantListItemScoreComparator());
                break;
        }
        return restaurantListItemList;
    }
}