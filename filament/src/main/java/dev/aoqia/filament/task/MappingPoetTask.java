package net.aoqia.filament.task;

import java.io.File;

import net.aoqia.filament.mappingpoet.MappingPoet;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

public abstract class MappingPoetTask extends DefaultTask {
    @TaskAction
    public void run() {
        MappingPoet.generate(
            getMappings().get().getAsFile().toPath(),
            getZomboidJar().get().getAsFile().toPath(),
            getOutput().get().getAsFile().toPath(),
            getLibraries().getFiles().stream().map(File::toPath).toList()
        );
    }

    @InputFile
    public abstract RegularFileProperty getMappings();

    @InputFile
    public abstract RegularFileProperty getZomboidJar();

    @InputFiles
    public abstract ConfigurableFileCollection getLibraries();

    @OutputDirectory
    public abstract DirectoryProperty getOutput();
}
