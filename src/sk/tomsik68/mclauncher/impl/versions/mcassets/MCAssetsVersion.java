package sk.tomsik68.mclauncher.impl.versions.mcassets;

import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionInstaller;
import sk.tomsik68.mclauncher.api.versions.IVersionLauncher;

public class MCAssetsVersion implements IVersion {
    private final String id;
    private static final MCAssetsVersionInstaller installer = new MCAssetsVersionInstaller();
    private static final MCAssetsVersionLauncher launcher = new MCAssetsVersionLauncher();
    public MCAssetsVersion(String vid) {
        id = vid;
    }

    @Override
    public int compareTo(IVersion o) {
        return getId().compareTo(o.getId());
    }

    @Override
    public String getDisplayName() {
        String type = "release";
        if(MCAssetsVersionList.isSnapshot(this))
            type = "snapshot";
        return type.concat(" ").concat(getId());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getUniqueID() {
        return getId();
    }

    @Override
    public IVersionInstaller<? extends IVersion> getInstaller() {
        return installer;
    }

    @Override
    public IVersionLauncher<? extends IVersion> getLauncher() {
        return launcher;
    }

}