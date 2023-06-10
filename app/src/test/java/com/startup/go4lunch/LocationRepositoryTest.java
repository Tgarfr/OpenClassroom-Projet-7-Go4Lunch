package com.startup.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LocationRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private LocationRepository locationRepository;


    @Before
    public void setUp() {
        locationRepository = new LocationRepository();
    }

    @Test
    public void setLocationAndGetLocationLiveData() throws InterruptedException {
        // Given
        final double expectedLatitude = 48.85834269238794;
        final double expectedLongitude = 2.294392951104722;
        final Location expectedLocation = Mockito.spy(new Location("Expected Location"));
        doReturn(expectedLatitude).when(expectedLocation).getLatitude();
        doReturn(expectedLongitude).when(expectedLocation).getLongitude();

        // When
        locationRepository.setLocation(expectedLocation);

        // Then
        LiveData<Location> actualLocationLiveData = locationRepository.getLocationLiveData();
        Location actualLocation = LiveDataTestUtils.getValue(actualLocationLiveData);
        assertEquals(expectedLatitude, actualLocation.getLatitude(), 0.00000000000001);
        assertEquals(expectedLongitude, actualLocation.getLongitude(), 0.00000000000001);
    }
}
