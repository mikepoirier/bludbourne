package com.packtpub.libgdx.bludbourne.profile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Hashtable;

/**
 * Created by blindweasel on 1/2/16.
 */
public class ProfileManager extends ProfileSubject
{
    private static final String TAG = ProfileManager.class.getSimpleName();
    public static final String DEFAULT_PROFILE = "default";
    private static final String SAVEGAME_SUFFIX = ".sav";
    private static ProfileManager profileManager;
    private Json json;
    private Hashtable<String, FileHandle> profiles;
    private ObjectMap<String, Object> profileProperties;
    private String profileName;

    private ProfileManager()
    {
        json = new Json();
        profiles = new Hashtable<>();
        profiles.clear();
        profileName = DEFAULT_PROFILE;
        profileProperties = new ObjectMap<>();
        storeAllProfiles();
    }

    public void storeAllProfiles()
    {
        if(Gdx.files.isLocalStorageAvailable())
        {
            FileHandle[] files = Gdx.files.local(".").list(SAVEGAME_SUFFIX);
            for(FileHandle file : files)
            {
                profiles.put(file.nameWithoutExtension(), file);
            }
        } else
        {
            //TODO: try extrenal directory here
            return;
        }
    }

    public static ProfileManager getInstance()
    {
        if(profileManager == null)
        {
            profileManager = new ProfileManager();
        }
        return profileManager;
    }

    public Array<String> getProfileList()
    {
        Array<String> profiles = new Array<>();
        this.profiles.keySet().forEach(profiles::add);
        return profiles;
    }

    public FileHandle getProfileFile(String profile)
    {
        if(!profileExists(profile))
        {
            return null;
        }
        return profiles.get(profile);
    }

    public  boolean profileExists(String profileName)
    {
        return profiles.containsKey(profileName);
    }

    public void writeProfileToStorage(String profileName, String fileData, boolean overwrite)
    {
        String fullFilename = profileName + SAVEGAME_SUFFIX;
        boolean localFileExists = Gdx.files.internal(fullFilename).exists();

        if(localFileExists && !overwrite)
        {
            return;
        }

        FileHandle file = null;

        if(Gdx.files.isLocalStorageAvailable())
        {
            file = Gdx.files.local(fullFilename);
            file.writeString(fileData, !overwrite);
        }

        profiles.put(profileName, file);
    }

    public void setProperty(String key, Object object)
    {
        profileProperties.put(key, object);
    }

    public <T> T getProperty(String key, Class<T> type)
    {
        if(!profileProperties.containsKey(key))
        {
            return null;
        }

        return (T) profileProperties.get(key);
    }

    public void saveProfile()
    {
        notify(this, ProfileObserver.ProfileEvent.SAVING_PROFILE);
        String text = json.prettyPrint(json.toJson(profileProperties));
        writeProfileToStorage(profileName, text, true);
    }

    public void loadProfile()
    {
        String fullProfileFileName = profileName + SAVEGAME_SUFFIX;
        boolean doesProfileFileExist = Gdx.files.internal(fullProfileFileName).exists();

        if(!doesProfileFileExist)
        {
            Gdx.app.debug(TAG, String.format("File does not exist: %s", fullProfileFileName));
            return;
        }

        profileProperties = json.fromJson(ObjectMap.class, profiles.get(profileName));
        notify(this, ProfileObserver.ProfileEvent.PROFILE_LOADED);
        Gdx.app.debug(TAG, String.format("Loaded Profile: %s", fullProfileFileName));
    }

    public void setCurrentProfile(String profileName)
    {
        if(profileExists(profileName))
        {
            this.profileName = profileName;
        } else {
            this.profileName = DEFAULT_PROFILE;
        }
    }
}
