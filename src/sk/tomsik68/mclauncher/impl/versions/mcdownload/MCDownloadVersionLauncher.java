package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.servers.ISavedServer;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionLauncher;
import sk.tomsik68.mclauncher.util.StringSubstitutor;

public class MCDownloadVersionLauncher implements IVersionLauncher {
    public String[] getMinecraftArguments(IMinecraftInstance mc, ISession session, ILaunchSettings settings, MCDownloadVersion version) {
        // TODO tooo lazy to finish options
        String[] args = version.getMinecraftArgs().split(" ");
        StringSubstitutor subst = new StringSubstitutor("${%s}");
        subst.setVariable("auth_session", session.getSessionID());
        subst.setVariable("auth_access_token", "cant_figure_out_this_one"); // NOT_SURE
        subst.setVariable("auth_player_name", session.getUsername());
        subst.setVariable("auth_uuid", session.getUUID());
        subst.setVariable("version_name", version.getDisplayName());
        subst.setVariable("game_directory", mc.getLocation().getAbsolutePath());
        subst.setVariable("game_assets", "assets directory here"); // NOT_SURE
        subst.setVariable("assets_root", "assets directory here probably aswell"); // NOT_SURE
        subst.setVariable("assets_index_name", "legacy"); // NOT_SURE

        for (int i = 0; i < args.length; i++) {
            args[i] = subst.substitute(args[i]);
        }
        return args;
    }

    @Override
    public Process launch(ISession session, IMinecraftInstance mc, ISavedServer server, IVersion v, ILaunchSettings settings) throws Exception {
        // get JSON information about the version
        File jsonFile = new File(mc.getJarProvider().getVersionFile(v.getUniqueID()).getParent(), "info.json");
        if (!jsonFile.exists()) {
            throw new FileNotFoundException("You need to download the version at first! (JSON description file not found!)");
        }

        MCDownloadVersion version = new MCDownloadVersion((JSONObject) JSONValue.parse(new FileInputStream(jsonFile)));
        File jarFile = mc.getJarProvider().getVersionFile(version.getUniqueID());

        if (!version.isCompatible()) {
            throw new VersionIncompatibleException(version);
        }
        if (version.getMinimumLauncherVersion() > MCLauncherAPI.MC_LAUNCHER) {
            throw new RuntimeException("You need to update MCLauncher-API to run this minecraft version!");
        }

        return null;
    }

}