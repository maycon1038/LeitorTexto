

package com.example.leitorplacascomcameraxvision;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import androidx.annotation.StringRes;


/** Configures live preview demo settings. */
public class LivePreviewPreferenceFragment extends PreferenceFragment {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    addPreferencesFromResource(R.xml.preference_live_preview_quickstart);
    setUpCameraPreferences();
    setUpFaceDetectionPreferences();
  }

  void setUpCameraPreferences() {
   /* PreferenceCategory cameraPreference = (PreferenceCategory) findPreference(getString(R.string.pref_category_key_camera));
    cameraPreference.removePreference(
        findPreference(getString(R.string.pref_key_camerax_rear_camera_target_resolution)));
    cameraPreference.removePreference(
        findPreference(getString(R.string.pref_key_camerax_front_camera_target_resolution)));
    setUpCameraPreviewSizePreference(
        R.string.pref_key_rear_camera_preview_size,
        R.string.pref_key_rear_camera_picture_size,
        CameraSource.CAMERA_FACING_BACK);
    setUpCameraPreviewSizePreference(
        R.string.pref_key_front_camera_preview_size,
        R.string.pref_key_front_camera_picture_size,
        CameraSource.CAMERA_FACING_FRONT);*/
  }



  private void setUpFaceDetectionPreferences() {
    setUpListPreference(R.string.pref_key_live_preview_face_detection_landmark_mode);
    setUpListPreference(R.string.pref_key_live_preview_face_detection_contour_mode);
    setUpListPreference(R.string.pref_key_live_preview_face_detection_classification_mode);
    setUpListPreference(R.string.pref_key_live_preview_face_detection_performance_mode);

    EditTextPreference minFaceSizePreference =
        (EditTextPreference)
            findPreference(getString(R.string.pref_key_live_preview_face_detection_min_face_size));
    minFaceSizePreference.setSummary(minFaceSizePreference.getText());
    minFaceSizePreference.setOnPreferenceChangeListener(
        (preference, newValue) -> {
          try {
            float minFaceSize = Float.parseFloat((String) newValue);
            if (minFaceSize >= 0.0f && minFaceSize <= 1.0f) {
              minFaceSizePreference.setSummary((String) newValue);
              return true;
            }
          } catch (NumberFormatException e) {
            // Fall through intentionally.
          }

          Toast.makeText(
                  getActivity(), R.string.pref_toast_invalid_min_face_size, Toast.LENGTH_LONG)
              .show();
          return false;
        });
  }

  private void setUpListPreference(@StringRes int listPreferenceKeyId) {
    ListPreference listPreference = (ListPreference) findPreference(getString(listPreferenceKeyId));
    listPreference.setSummary(listPreference.getEntry());
    listPreference.setOnPreferenceChangeListener(
        (preference, newValue) -> {
          int index = listPreference.findIndexOfValue((String) newValue);
          listPreference.setSummary(listPreference.getEntries()[index]);
          return true;
        });
  }
}
