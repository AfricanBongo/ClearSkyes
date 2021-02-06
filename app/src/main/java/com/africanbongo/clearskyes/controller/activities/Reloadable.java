package com.africanbongo.clearskyes.controller.activities;

import android.view.View;

/**
 * Allows certain UI components to be reloaded,
 * used in the case of removing error pages from the screen
 */
public interface Reloadable {
    /**
     * When implemented it should show the error page.
     * Necessary UI changes to show the error page should be done in here.
     * @return {@link View} which contains the error page
     */
    View showError();

    /**
     * When implemented it refreshes UI components and removes the error page
     * Necessary UI changes to remove the error page should be done in here.
     */
    void reload();
}
