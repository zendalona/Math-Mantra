package com.zendalona.mathmantra.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PermissionManagerTest {

    @Mock
    private Activity mockActivity;
    
    @Mock
    private PermissionManager.PermissionCallback mockCallback;
    
    private PermissionManager permissionManager;

    @Before
    public void setUp() {
        permissionManager = new PermissionManager(mockActivity, mockCallback);
    }

    @Test
    public void testRequestMicrophonePermission_AlreadyGranted() {
        try (MockedStatic<ContextCompat> mockedContextCompat = mockStatic(ContextCompat.class)) {
            mockedContextCompat.when(() -> ContextCompat.checkSelfPermission(mockActivity, Manifest.permission.RECORD_AUDIO))
                    .thenReturn(PackageManager.PERMISSION_GRANTED);

            permissionManager.requestMicrophonePermission();

            verify(mockCallback).onPermissionGranted();
            verify(mockCallback, never()).onPermissionDenied();
        }
    }

    @Test
    public void testRequestAccelerometerPermission_AlreadyGranted() {
        try (MockedStatic<ContextCompat> mockedContextCompat = mockStatic(ContextCompat.class)) {
            mockedContextCompat.when(() -> ContextCompat.checkSelfPermission(mockActivity, Manifest.permission.BODY_SENSORS))
                    .thenReturn(PackageManager.PERMISSION_GRANTED);

            permissionManager.requestAccelerometerPermission();

            verify(mockCallback).onPermissionGranted();
            verify(mockCallback, never()).onPermissionDenied();
        }
    }

    @Test
    public void testHandlePermissionsResult_MicrophoneGranted() {
        String[] permissions = {Manifest.permission.RECORD_AUDIO};
        int[] grantResults = {PackageManager.PERMISSION_GRANTED};

        permissionManager.handlePermissionsResult(PermissionManager.REQUEST_CODE_MICROPHONE, permissions, grantResults);

        verify(mockCallback).onPermissionGranted();
        verify(mockCallback, never()).onPermissionDenied();
    }

    @Test
    public void testHandlePermissionsResult_AccelerometerDenied() {
        String[] permissions = {Manifest.permission.BODY_SENSORS};
        int[] grantResults = {PackageManager.PERMISSION_DENIED};

        permissionManager.handlePermissionsResult(PermissionManager.REQUEST_CODE_ACCELEROMETER, permissions, grantResults);

        verify(mockCallback).onPermissionDenied();
        verify(mockCallback, never()).onPermissionGranted();
    }

    @Test
    public void testHandlePermissionsResult_UnknownRequestCode() {
        String[] permissions = {Manifest.permission.CAMERA};
        int[] grantResults = {PackageManager.PERMISSION_GRANTED};

        permissionManager.handlePermissionsResult(999, permissions, grantResults);

        // Should not call any callback for unknown request codes
        verify(mockCallback, never()).onPermissionGranted();
        verify(mockCallback, never()).onPermissionDenied();
    }

    @Test
    public void testHandlePermissionsResult_EmptyResults() {
        String[] permissions = {Manifest.permission.RECORD_AUDIO};
        int[] grantResults = {};

        permissionManager.handlePermissionsResult(PermissionManager.REQUEST_CODE_MICROPHONE, permissions, grantResults);

        verify(mockCallback).onPermissionDenied();
        verify(mockCallback, never()).onPermissionGranted();
    }
}