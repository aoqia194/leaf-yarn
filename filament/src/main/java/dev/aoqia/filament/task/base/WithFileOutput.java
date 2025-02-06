package net.aoqia.filament.task.base;

import java.io.File;
import java.nio.file.Path;

import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.OutputFile;

public interface WithFileOutput {
    @Internal
    default Path getOutputPath() {
        return getOutputFile().toPath();
    }

    @Internal
    default File getOutputFile() {
        return getOutput().get().getAsFile();
    }

    @OutputFile
    RegularFileProperty getOutput();
}
