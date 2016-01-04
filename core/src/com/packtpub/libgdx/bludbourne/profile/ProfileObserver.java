package com.packtpub.libgdx.bludbourne.profile;

/**
 * Created by blindweasel on 1/2/16.
 */
public interface ProfileObserver
{
    public enum ProfileEvent
    {
        PROFILE_LOADED, SAVING_PROFILE
    }

    void onNotify(final ProfileManager profileManager, ProfileEvent event);
}
