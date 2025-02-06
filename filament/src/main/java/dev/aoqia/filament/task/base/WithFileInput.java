package net.aoqia.filament.task.base;

import java.io.File;
import java.nio.file.Path;

import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.Internal;

public interface WithFileInput {
    @Internal
    default Path getInputPath() {
        return getInputFile().toPath();
    }

    @Internal
    default File getInputFile() {
        return getInput().get().getAsFile();
    }

    @InputFile
    RegularFileProperty getInput();
}
