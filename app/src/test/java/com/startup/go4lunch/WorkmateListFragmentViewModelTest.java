package com.startup.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.model.WorkmateListItem;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.SearchRepository;
import com.startup.go4lunch.repository.WorkmateRepository;
import com.startup.go4lunch.ui.WorkmateListFragmentViewModel;
import com.startup.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WorkmateListFragmentViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private WorkmateRepository workmateRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private SearchRepository searchRepository;

    private WorkmateListFragmentViewModel workmateListFragmentViewModel;

    @Before
    public void setUp() {
        when(workmateRepository.getWorkmateListLiveData()).thenReturn(new MutableLiveData<>(FAKE_WORKMATE_LIST));
        when(searchRepository.getWorkmateListFragmentSearchLiveData()).thenReturn(new MutableLiveData<>(null));
        when(restaurantRepository.getRestaurantFromId(FAKE_RESTAURANTS_LIST.get(2).getId())).thenReturn(FAKE_RESTAURANTS_LIST.get(2));
        when(restaurantRepository.getRestaurantFromId(FAKE_RESTAURANTS_LIST.get(3).getId())).thenReturn(FAKE_RESTAURANTS_LIST.get(3));

        workmateListFragmentViewModel = new WorkmateListFragmentViewModel(workmateRepository, restaurantRepository, searchRepository);
    }

    @Test
    public void getWorkmateListLiveDataTest() throws InterruptedException {
        // Given
        List<Workmate> expectedWorkmateList = FAKE_WORKMATE_LIST;
        @SuppressWarnings("redundant")
        List<Restaurant> expectedRestaurantList = FAKE_RESTAURANTS_LIST;

        // When
        LiveData<List<WorkmateListItem>> actualWorkmateListItemListLiveData = workmateListFragmentViewModel.getWorkmateListLiveData();

        // Then
        List<WorkmateListItem> actualWorkmateListItemList = LiveDataTestUtils.getValue(actualWorkmateListItemListLiveData);
        for (int i = 0; i < expectedWorkmateList.size(); i++) {
            Workmate expectedWorkmate = expectedWorkmateList.get(i);
            WorkmateListItem actualWorkmateListItem = actualWorkmateListItemList.get(i);

            assertEquals(expectedWorkmate, actualWorkmateListItem.getWorkmate());
            if (expectedWorkmate.getRestaurantSelectedUid() == null) {
                assertEquals(WorkmateListItem.DISPLAY_TEXT_NOT_DECIDED, actualWorkmateListItem.getDisplayTextType());
            } else {
                assertEquals(WorkmateListItem.DISPLAY_TEXT_EATING, actualWorkmateListItem.getDisplayTextType());
                for (Restaurant restaurant: expectedRestaurantList) {
                    if (expectedWorkmate.getRestaurantSelectedUid() == restaurant.getId()) {
                        assertEquals(restaurant, actualWorkmateListItem.getRestaurantChoice());
                    }
                }
            }
        }
    }

    private static final List<Restaurant> FAKE_RESTAURANTS_LIST = Arrays.asList(
            new Restaurant(0L, "Restaurant 0", "type 0", 1000, 2000, "1 chemin de la Paix, 75002 Paris", "0h00","0600000000","http://website0.fr"),
            new Restaurant(1L, "Restaurant 1", "type 1", 1111, 2111, "1 rue de la Paix, 75002 Paris", "1h00","0600000001","http://website1.fr"),
            new Restaurant(2L, "Restaurant 2", "type 2", 1222, 2222, "2 rue de la Paix, 75002 Paris", "2h00","0600000002","http://website2.com"),
            new Restaurant(3L, "Restaurant 3", "type 3", 1333, 2333, "3 rue de la Paix, 75002 Paris", "3h00","0600000003","http://website3.com"));

    private static final List<Workmate> FAKE_WORKMATE_LIST = Arrays.asList(
            new Workmate("ec4e8cr", "Didier Durant", "http:...", null),
            new Workmate("ec4hrthtr", "Olivier Martin", "http:...", 3L),
            new Workmate("tyhgreyhryh", "Emilie Dupont", "http:...", 2L),
            new Workmate("rtherhhre", "Frederique Leblanc", "http:...", 2L));

}
