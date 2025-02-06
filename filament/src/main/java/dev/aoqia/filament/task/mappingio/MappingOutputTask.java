package net.aoqia.filament.task.mappingio;

import java.io.IOException;

import net.aoqia.filament.task.base.FilamentTask;
import net.aoqia.filament.task.base.WithFileOutput;
import net.fabricmc.mappingio.MappingWriter;
import net.fabricmc.mappingio.format.MappingFormat;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

public abstract class MappingOutputTask extends FilamentTask implements WithFileOutput {
    @TaskAction
    public final void run() throws IOException {
        try (MappingWriter mappingWriter = MappingWriter.create(getOutputPath(), getOutputFormat().get())) {
            run(mappingWriter);
        }
    }

    @Input
    public abstract Property<MappingFormat> getOutputFormat();

    abstract void run(MappingWriter writer) throws IOException;
}
