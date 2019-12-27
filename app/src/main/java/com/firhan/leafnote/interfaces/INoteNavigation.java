package com.firhan.leafnote.interfaces;

import android.os.Bundle;

public interface INoteNavigation {
    void navigateFragment(int destinationId, Bundle bundle);
    void showDeleteMenuIcon(boolean isShow, boolean isTrashCan);
    void showEditMenuIcon(boolean isShow);
    void setPageTitle(String title);
}
