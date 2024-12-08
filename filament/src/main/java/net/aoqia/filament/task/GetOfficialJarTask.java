package net.aoqia.filament.task;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

import net.aoqia.filament.task.base.FilamentTask;
import net.aoqia.filament.task.base.WithFileOutput;
import net.aoqia.loom.util.copygamefile.CopyGameFile;
import net.aoqia.loom.util.copygamefile.CopyGameFileBuilder;
import org.gradle.api.Project;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.TaskAction;

public abstract class GetOfficialJarTask extends FilamentTask implements WithFileOutput {
    @TaskAction
    public void run() throws IOException {
        final Project project = getProject();
        final String zomboidVersion = Objects.requireNonNull(getZomboidVersion().getOrNull());

        final Path loomCache = Path.of(getGradleUserHomeDir().get()).resolve("caches").resolve("leaf-loom");
        if (!loomCache.toFile().exists()) {
            throw new IOException("Failed to get official Zomboid jar from leaf-loom!");
        }

        final Path loomZomboidVersionCacheDir = loomCache.resolve(zomboidVersion);
        final Path inputJar = loomZomboidVersionCacheDir.resolve(getJarName().get());

        // Copy client jar from loom cache
        CopyGameFileBuilder builder = CopyGameFile.create(inputJar);
        if (project.getGradle().getStartParameter().isRefreshDependencies() || Boolean.getBoolean("loom.refresh")) {
            builder.forced();
        }
        builder.copyGameFileFromPath(getOutputPath());
    }

    @Input
    public abstract Property<String> getGradleUserHomeDir();

    @Input
    public abstract Property<String> getJarName();

    @Input
    public abstract Property<String> getZomboidVersion();

    @InputDirectory
    public abstract DirectoryProperty getFilamentCacheDir();
}
