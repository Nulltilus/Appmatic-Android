package com.appmatic.baseapp.utils;

import com.appmatic.baseapp.contact.ContactFragment;
import com.appmatic.baseapp.content_container.ContentContainerFragment;
import com.appmatic.baseapp.gallery.GalleryFragment;

/**
 * Created by grender on 13/04/17.
 */

public class FragmentUtils {
    public static String getTagByMenuId(int menuId) {
        switch (menuId) {
            case Constants.MENU_CONTACT_ID:
                return ContactFragment.class.toString();
            case Constants.MENU_GALLERY_ID:
                return GalleryFragment.class.toString();
            default:
                return ContentContainerFragment.class.toString();
        }
    }
}
