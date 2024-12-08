package net.aoqia.filament.task.mappingio;

import java.io.IOException;

import net.aoqia.filament.task.base.WithFileInput;
import net.fabricmc.mappingio.MappingReader;
import net.fabricmc.mappingio.MappingWriter;

public abstract class ConvertMappingsTask extends MappingOutputTask implements WithFileInput {
    @Override
    void run(MappingWriter writer) throws IOException {
        MappingReader.read(getInputPath(), writer);
    }
}
