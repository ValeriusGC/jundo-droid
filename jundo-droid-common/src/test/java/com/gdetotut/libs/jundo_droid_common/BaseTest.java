package com.gdetotut.libs.jundo_droid_common;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * If error like:
 * java.lang.annotation.AnnotationFormatError at Android Studio
 * delete /.gradle dir!
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public abstract class BaseTest {}
