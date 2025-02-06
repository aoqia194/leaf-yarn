package net.aoqia.filament.task;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import daomephsta.unpick.constantmappers.datadriven.parser.v2.UnpickV2Reader;
import daomephsta.unpick.constantmappers.datadriven.parser.v2.UnpickV2Writer;
import net.aoqia.filament.util.FileUtil;
import net.aoqia.filament.util.UnpickUtil;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.workers.WorkAction;
import org.gradle.workers.WorkParameters;
import org.gradle.workers.WorkQueue;
import org.gradle.workers.WorkerExecutor;

public abstract class CombineUnpickDefinitionsTask extends DefaultTask {
    @TaskAction
    public void run() {
        WorkQueue workQueue = getWorkerExecutor().noIsolation();
        workQueue.submit(CombineAction.class, parameters -> {
            parameters.getInput().set(getInput());
            parameters.getOutput().set(getOutput());
        });
    }

    @InputDirectory
    public abstract DirectoryProperty getInput();

    @OutputFile
    public abstract RegularFileProperty getOutput();

    @Inject
    protected abstract WorkerExecutor getWorkerExecutor();

    public interface CombineParameters extends WorkParameters {
        @InputDirectory
        DirectoryProperty getInput();

        @OutputFile
        RegularFileProperty getOutput();
    }

    public abstract static class CombineAction implements WorkAction<CombineParameters> {
        @Inject
        public CombineAction() {
        }

        @Override
        public void execute() {
            try {
                File output = getParameters().getOutput().getAsFile().get();
                FileUtil.deleteIfExists(output);

                UnpickV2Writer writer = new UnpickV2Writer();

                // Sort inputs to get reproducible outputs (also for testing)
                List<File> files = new ArrayList<>(getParameters().getInput().getAsFileTree().getFiles());
                files.sort(Comparator.comparing(File::getName));

                files = files.stream().filter(f -> f.getName().endsWith(".unpick")).toList();
                // This means no unpick files exist. So don't create the output file.
                if (files.isEmpty()) {
                    System.out.println("No unpick files found.");
                    return;
                }

                for (File file : files) {
                    try (UnpickV2Reader reader = new UnpickV2Reader(new FileInputStream(file))) {
                        reader.accept(writer);
                    }
                }

                FileUtil.write(output, UnpickUtil.getLfOutput(writer));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
