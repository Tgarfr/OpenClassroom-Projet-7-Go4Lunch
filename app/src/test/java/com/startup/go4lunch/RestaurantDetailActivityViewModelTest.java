package com.startup.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.RestaurantWorkmateVote;
import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.model.WorkmateListItem;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.WorkmateRepository;
import com.startup.go4lunch.ui.RestaurantDetailActivityViewModel;
import com.startup.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantDetailActivityViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private WorkmateRepository workmateRepository;

    @Mock
    private FirebaseUser firebaseUser;

    private Workmate user;

    private RestaurantDetailActivityViewModel restaurantDetailActivityViewModel;

    @Before
    public void setUp() {
        user = FAKE_WORKMATE_LIST.get(1);
        when(firebaseUser.getUid()).thenReturn(user.getUid());

        restaurantDetailActivityViewModel = new RestaurantDetailActivityViewModel(restaurantRepository, workmateRepository, firebaseUser);

        when(restaurantRepository.getRestaurantListLiveData()).thenReturn(new MutableLiveData<>(FAKE_RESTAURANTS_LIST));
        when(workmateRepository.getWorkmateListLiveData()).thenReturn(new MutableLiveData<>(FAKE_WORKMATE_LIST));
        when(workmateRepository.getRestaurantWorkmateVoteListLiveData()).thenReturn(new MutableLiveData<>(FAKE_RESTAURANT_WORKMATE_VOTE_LIST));
    }

    @Test
    public void getRestaurantLiveDataTest() throws InterruptedException {
        // Given
        Restaurant expectedRestaurant = FAKE_RESTAURANTS_LIST.get(1);

        // When
        LiveData<Restaurant> actualRestaurantLiveData = restaurantDetailActivityViewModel.getRestaurantLiveData(expectedRestaurant.getId());

        // Then
        Restaurant actualRestaurant = LiveDataTestUtils.getValue(actualRestaurantLiveData);
        assertEquals(expectedRestaurant, actualRestaurant);
    }

    @Test
    public void UserWorkmateLiveDataTest() throws InterruptedException {
        // Given
        Workmate expectedWorkmate = user;

        // When
        LiveData<Workmate> actualWorkmateLiveData = restaurantDetailActivityViewModel.getUserWorkmateLiveData();

        // Then
        Workmate actualWorkmate = LiveDataTestUtils.getValue(actualWorkmateLiveData);
        assertEquals(expectedWorkmate, actualWorkmate);
    }

    @Test
    public void getWorkmateListItemLiveDataTest() throws InterruptedException {
        // Given
        long expectedRestaurantId = FAKE_RESTAURANTS_LIST.get(2).getId();
        List<Workmate> expectedWorkmateList = FAKE_WORKMATE_LIST;

        // When
        LiveData<List<WorkmateListItem>> actualWorkmateListItemListLiveData = restaurantDetailActivityViewModel.getWorkmateListItemLiveData(expectedRestaurantId);

        // Then
        List<WorkmateListItem> actualWorkmateListItemList = LiveDataTestUtils.getValue(actualWorkmateListItemListLiveData);
        int indexActualWorkmateListItemList = 0;
        for (int i = 0; i < expectedWorkmateList.size(); i++) {
            Workmate expectedWorkmate = expectedWorkmateList.get(i);
            if ( expectedWorkmate.getRestaurantSelectedUid() != null && expectedWorkmate.getRestaurantSelectedUid() == expectedRestaurantId) {
                WorkmateListItem actualWorkmateListItem = actualWorkmateListItemList.get(indexActualWorkmateListItemList);
                assertEquals(expectedWorkmate, actualWorkmateListItem.getWorkmate());
                assertNull(actualWorkmateListItem.getRestaurantChoice());
                assertEquals(WorkmateListItem.DISPLAY_TEXT_JOINING, actualWorkmateListItem.getDisplayTextType());
                indexActualWorkmateListItemList++;
            }
        }
    }

    @Test
    public void getRestaurantWorkmateVoteLiveDataTest() throws InterruptedException {
        // Given
        @SuppressWarnings("redundant")
        List<RestaurantWorkmateVote> restaurantWorkmateVoteList = FAKE_RESTAURANT_WORKMATE_VOTE_LIST;
        long expectedRestaurantId = FAKE_RESTAURANTS_LIST.get(3).getId();
        String expectedUserUid = user.getUid();

        // When
        LiveData<Boolean> actualBooleanLivedata = restaurantDetailActivityViewModel.getRestaurantWorkmateVoteLiveData(expectedUserUid, expectedRestaurantId);

        // Then
        boolean actualBoolean = LiveDataTestUtils.getValue(actualBooleanLivedata);
        boolean expectedBoolean = false;
        for (RestaurantWorkmateVote restaurantWorkmateVote : restaurantWorkmateVoteList) {
            if (Objects.equals(restaurantWorkmateVote.getWorkmateUid(), expectedUserUid) & restaurantWorkmateVote.getRestaurantUid() == expectedRestaurantId) {
                assertTrue(actualBoolean);
                expectedBoolean = true;
            }
        }
        if (!expectedBoolean) {
            assertFalse(actualBoolean);
        }
    }

    @Test
    public void setRestaurantWorkmateVoteTest() {
        // Given
        String expectedUserUid = user.getUid();
        long expectedRestaurantId = FAKE_RESTAURANTS_LIST.get(3).getId();

        // When
        restaurantDetailActivityViewModel.setRestaurantWorkmateVote(expectedUserUid, expectedRestaurantId);

        // Then
        verify(workmateRepository).createRestaurantWorkmateVote(expectedUserUid, expectedRestaurantId);
    }

    @Test
    public void removeRestaurantWorkmateVoteTest() {
        // Given
        String expectedUserUid = user.getUid();
        long expectedRestaurantId = FAKE_RESTAURANTS_LIST.get(3).getId();

        // When
        restaurantDetailActivityViewModel.removeRestaurantWorkmateVote(expectedUserUid, expectedRestaurantId);

        // Then
        verify(workmateRepository).removeRestaurantWorkmateVote(expectedUserUid, expectedRestaurantId);
    }

    @Test
    public void setRestaurantSelectedTest() {
        // Given
        String expectedUserUid = user.getUid();
        long expectedRestaurantId = FAKE_RESTAURANTS_LIST.get(3).getId();

        // When
        restaurantDetailActivityViewModel.setRestaurantSelected(expectedUserUid, expectedRestaurantId);

        // Then
        verify(workmateRepository).setRestaurantSelectedToWorkmate(expectedUserUid, expectedRestaurantId);
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

    private static final List<RestaurantWorkmateVote> FAKE_RESTAURANT_WORKMATE_VOTE_LIST = Arrays.asList(
            new RestaurantWorkmateVote("ec4e8cr",2L),
            new RestaurantWorkmateVote("ec4hrthtr",3L));
}
