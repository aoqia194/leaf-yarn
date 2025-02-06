package net.aoqia.filament;

import javax.inject.Inject;
import java.io.File;

import net.aoqia.filament.util.ZomboidVersionMetaHelper;
import net.aoqia.loom.configuration.providers.zomboid.ZomboidVersionMeta;
import net.aoqia.loom.util.MirrorUtil;
import org.gradle.api.Project;
import org.gradle.api.file.Directory;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFile;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;

public abstract class FilamentExtension {
    private final ZomboidVersionMetaHelper metaHelper;
    private final Provider<ZomboidVersionMeta> metaProvider;

    @Inject
    public FilamentExtension() {
        getZomboidVersion().finalizeValueOnRead();
        getZomboidVersionManifestUrl().convention(MirrorUtil.getClientVersionManifests(getProject()))
            .finalizeValueOnRead();

        metaHelper = getProject().getObjects().newInstance(ZomboidVersionMetaHelper.class, this);
        metaProvider = getProject().provider(metaHelper::setup);
    }

    @Inject
    protected abstract Project getProject();

    public abstract Property<String> getZomboidVersion();

    public abstract Property<String> getZomboidVersionManifestUrl();

    public static FilamentExtension get(Project project) {
        return project.getExtensions().getByType(FilamentExtension.class);
    }

    public Provider<RegularFile> getZomboidFile(String filename) {
        return getZomboidDirectory().map(directory -> directory.file(filename));
    }

    public Provider<Directory> getZomboidDirectory() {
        return getCacheDirectory().dir(getZomboidVersion());
    }

    public DirectoryProperty getCacheDirectory() {
        return getProject().getObjects()
            .directoryProperty()
            .fileValue(new File(getProject().getRootDir(), ".gradle/filament"));
    }

    public Provider<ZomboidVersionMeta> getZomboidVersionMetadata() {
        return metaProvider;
    }
}
