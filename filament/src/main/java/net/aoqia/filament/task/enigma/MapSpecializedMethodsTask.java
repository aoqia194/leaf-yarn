package net.aoqia.filament.task.enigma;

import javax.inject.Inject;
import java.util.List;

import cuchaz.enigma.command.Command;
import cuchaz.enigma.command.MapSpecializedMethodsCommand;
import net.aoqia.filament.task.base.WithFileOutput;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.InputFile;

public abstract class MapSpecializedMethodsTask extends EnigmaCommandTask implements WithFileOutput {
    @Inject
    public MapSpecializedMethodsTask() {
        getInputMappingsFormat().convention("engima");
        getOutputMappingsFormat().convention("tinyv2:official:named");
    }

    @Input
    public abstract Property<String> getInputMappingsFormat();

    @Input
    public abstract Property<String> getOutputMappingsFormat();

    @Override
    public Class<? extends Command> getCommandClass() {
        return MapSpecializedMethodsCommand.class;
    }

    @Override
    protected List<String> getArguments() {
        return List.of(
            getInputJarFile().get().getAsFile().getAbsolutePath(),
            getInputMappingsFormat().get(),
            getMappings().get().getAsFile().getAbsolutePath(),
            getOutputMappingsFormat().get(),
            getOutputFile().getAbsolutePath()
        );
    }

    @InputFile
    public abstract RegularFileProperty getInputJarFile();

    @InputDirectory
    public abstract DirectoryProperty getMappings();
}
