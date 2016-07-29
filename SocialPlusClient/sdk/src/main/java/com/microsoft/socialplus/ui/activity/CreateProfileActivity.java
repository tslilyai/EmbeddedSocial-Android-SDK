/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *
 */

package com.microsoft.socialplus.ui.activity;

import com.microsoft.socialplus.ui.activity.base.BaseActivity;
import com.microsoft.socialplus.ui.fragment.CreateProfileFragment;

/**
 * Activity for creating a user's profile.
 */
public class CreateProfileActivity extends BaseActivity {

    @Override
    protected void setupFragments() {
        CreateProfileFragment createProfileFragment = new CreateProfileFragment();
        setActivityContent(createProfileFragment);
        createProfileFragment.setArguments(getIntent().getExtras());
    }
}