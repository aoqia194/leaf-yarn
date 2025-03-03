package dev.aoqia.leaf.filament.util;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

import dev.aoqia.leaf.filament.FilamentExtension;
import dev.aoqia.leaf.filament.FilamentGradlePlugin;
import dev.aoqia.leaf.loom.configuration.providers.zomboid.VersionsManifest;
import dev.aoqia.leaf.loom.configuration.providers.zomboid.ZomboidVersionManifest;
import dev.aoqia.leaf.loom.util.download.Download;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;

public abstract class ZomboidVersionMetaHelper {
    @Inject
    public ZomboidVersionMetaHelper(FilamentExtension extension) {
        // Use the Zomboid version as an input to ensure the task re-runs on upgrade
        getZomboidVersion().set(extension.getZomboidVersion());
        getZomboidVersionManifestUrl().set(extension.getZomboidVersionManifestUrl());

        getVersionManifestFile().set(extension.getZomboidFile("version_manifest.json"));
        getVersionMetadataFile().set(extension.getZomboidFile("dummy.file"));
    }

    public abstract Property<String> getZomboidVersion();

    public abstract Property<String> getZomboidVersionManifestUrl();

    public abstract RegularFileProperty getVersionManifestFile();

    public abstract RegularFileProperty getVersionMetadataFile();

    public ZomboidVersionManifest setup() throws IOException, URISyntaxException {
        // Only needed to access it at setup time because for whatever reason, getZomboidVersion
        final String zomboidVersion = getVersionMetadataFile().getAsFile().get().getParent();

        final Path versionManifestPath = getVersionManifestFile().getAsFile().get().toPath();
        final Path versionMetadataPath = getVersionMetadataFile().getAsFile()
            .get()
            .toPath()
            .getParent()
            .resolve(getZomboidVersion().get() + ".json");

        final String versionManifestRaw = Download.create(getZomboidVersionManifestUrl().get())
            .defaultCache()
            .downloadString(versionManifestPath);

        final VersionsManifest versionManifest = FilamentGradlePlugin.GSON.fromJson(versionManifestRaw,
            VersionsManifest.class);

        VersionsManifest.Version version = versionManifest.versions().stream()
            .filter(versions -> versions.id.equalsIgnoreCase(getZomboidVersion().get()))
            .findFirst()
            .orElse(null);

        if (version == null) {
            throw new RuntimeException("Failed to find Zomboid version: " + getZomboidVersion().get());
        }

        final String versionMetadata = Download.create(version.url)
            .sha1(version.sha1)
            .downloadString(versionMetadataPath);

        return FilamentGradlePlugin.GSON.fromJson(versionMetadata, ZomboidVersionManifest.class);
    }
}
