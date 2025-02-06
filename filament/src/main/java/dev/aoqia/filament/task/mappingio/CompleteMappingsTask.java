package net.aoqia.filament.task.mappingio;

import java.io.IOException;
import java.util.Map;

import net.aoqia.filament.task.base.WithFileInput;
import net.fabricmc.mappingio.MappingReader;
import net.fabricmc.mappingio.MappingWriter;
import net.fabricmc.mappingio.adapter.MappingNsCompleter;

public abstract class CompleteMappingsTask extends MappingOutputTask implements WithFileInput {
    @Override
    void run(MappingWriter writer) throws IOException {
        var nsCompleter = new MappingNsCompleter(writer, Map.of("named", "official"), true);
        MappingReader.read(getInputPath(), nsCompleter);
    }
}
