package com.startup.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.net.Uri;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.firebase.auth.FirebaseUser;
import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.SearchRepository;
import com.startup.go4lunch.repository.WorkmateRepository;
import com.startup.go4lunch.ui.MainActivityViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityViewModelTest {

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

    private MainActivityViewModel mainActivityViewModel;



    @Before
    public void setUp() {
        mainActivityViewModel = new MainActivityViewModel(restaurantRepository, locationRepository, workmateRepository, searchRepository);
    }

    @Test
    public void setMapFragmentSearchTest() {
        // Given
        final String expectedString = "MapFragmentSearch Test";

        // When
        mainActivityViewModel.setMapFragmentSearch(expectedString);

        // Then
        verify(searchRepository).setMapFragmentSearch(expectedString);
    }

    @Test
    public void setRestaurantListSearchTest() {
        // Given
        final String expectedString = "RestaurantListSearch Test";

        // When
        mainActivityViewModel.setRestaurantListSearch(expectedString);

        // Then
        verify(searchRepository).setRestaurantListSearch(expectedString);
    }

    @Test
    public void setWorkmateListSearchTest() {
        // Given
        final String expectedString = "WorkmateList Test";

        // When
        mainActivityViewModel.setWorkmateListSearch(expectedString);

        // Then
        verify(searchRepository).setWorkmateListSearch(expectedString);
    }

    @Test
    public void setCreateWorkmateTest() {
        // Given
        FirebaseUser firebaseUser = mock(FirebaseUser.class);
        String expectedFirebaseUserUid = "dcdcsscsrerfvreg";
        String expectedFirebaseUserName = "Name FirebaseUser Test";
        String expectedFirebaseUserPhotoUrl = "http://www.test.fr";
        Uri expectedFirebaseUserPhotoUrlUri = mock(Uri.class);

        when(expectedFirebaseUserPhotoUrlUri.toString()).thenReturn(expectedFirebaseUserPhotoUrl);
        when(firebaseUser.getUid()).thenReturn(expectedFirebaseUserUid);
        when(firebaseUser.getDisplayName()).thenReturn(expectedFirebaseUserName);
        when(firebaseUser.getPhotoUrl()).thenReturn(expectedFirebaseUserPhotoUrlUri);
        ArgumentCaptor<Workmate> captor = ArgumentCaptor.forClass(Workmate.class);

        // When
        mainActivityViewModel.createWorkmate(firebaseUser);

        // Then
        verify(workmateRepository).createWorkmate(captor.capture());
        Workmate actualWorkmate = captor.getValue();
        assertEquals(expectedFirebaseUserUid, actualWorkmate.getUid());
        assertEquals(expectedFirebaseUserName, actualWorkmate.getName());
        assertEquals(expectedFirebaseUserPhotoUrl, actualWorkmate.getAvatarUri());
    }

    @Test

    public void getWorkmateRestaurantSelectedUidTest() {
        // Given
        @SuppressWarnings("redundant")
        List<Workmate> expectedWorkmateList = FAKE_WORKMATE_LIST;
        Workmate expectedWorkmate = expectedWorkmateList.get(1);
        String expectedWorkmateUid = expectedWorkmate.getUid();
        Long expectedWorkmateRestaurantSelectedUid = expectedWorkmate.getRestaurantSelectedUid();
        when(workmateRepository.getWorkmateFromUid(expectedWorkmateUid)).thenReturn(expectedWorkmate);

        // When
        Long actualWorkmateRestaurantSelectedUid = mainActivityViewModel.getWorkmateRestaurantSelectedUid(expectedWorkmateUid);

        // Then
        assertEquals(expectedWorkmateRestaurantSelectedUid, actualWorkmateRestaurantSelectedUid);
    }

    private static final List<Workmate> FAKE_WORKMATE_LIST = Arrays.asList(
            new Workmate("ec4e8cr", "Didier Durant", "http:...", 0L),
            new Workmate("ec4hrthtr", "Olivier Martin", "http:...", 3L),
            new Workmate("tyhgreyhryh", "Emilie Dupont", "http:...", 2L),
            new Workmate("rtherhhre", "Frederique Leblanc", "http:...", 2L));
}
