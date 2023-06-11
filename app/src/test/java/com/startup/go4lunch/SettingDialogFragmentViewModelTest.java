package com.startup.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.startup.go4lunch.repository.SettingsRepository;
import com.startup.go4lunch.ui.SettingsDialogFragmentViewModel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SettingDialogFragmentViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private SettingsRepository settingsRepository;

    @InjectMocks
    private SettingsDialogFragmentViewModel settingsDialogFragmentViewModel;

    @SuppressWarnings("ConstantConditions")
    @Test
    public void setAreNotificationsEnableTest() {
        // Given
        final boolean expectedBoolean = true;

        // When
        settingsDialogFragmentViewModel.setAreNotificationsEnable(null, expectedBoolean);

        // Then
        verify(settingsRepository).setAreNotificationsEnable(null, expectedBoolean);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void getAreNotificationsEnableTest() {
        // Given
        final boolean expectedBoolean = false;
        when(settingsRepository.getAreNotificationsEnable(null)).thenReturn(expectedBoolean);

        // When
        final boolean actualBoolean = settingsDialogFragmentViewModel.getAreNotificationsEnable(null);

        // Then
        assertEquals(expectedBoolean, actualBoolean);
    }
}

